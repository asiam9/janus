package com.em.janus.dao.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;

import com.em.janus.dao.CacheManager;
import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Entity;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;

public class NaiveCachedDAO<T extends Entity> implements IDataAccessObject<T>, ICachedDAO{

	private IDataAccessObject<T> dao = null;
	
	private Map<Integer, T> cache = new HashMap<Integer, T>();
	
	private Semaphore cacheUpdateMutex = new Semaphore(1);
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private Class<?> primaryType = null;
		
	private Map<Integer, Set<T>> byBookId = new HashMap<Integer, Set<T>>();
	private Map<Integer, Set<T>> byAuthorId = new HashMap<Integer, Set<T>>();
	private Map<Integer, Set<T>> bySeriesId = new HashMap<Integer, Set<T>>();
	private Map<Integer, Set<T>> byTagId = new HashMap<Integer, Set<T>>();
	
	private Map<String, Set<T>> startsWith = new HashMap<String, Set<T>>();
	
	public NaiveCachedDAO(Class<T> primaryType, IDataAccessObject<T> daoToCache) {
		//decorate the static dao
		this.dao = daoToCache;
		
		//descisions based on output class
		this.primaryType = primaryType;
		
		//register to be managed for eviction notices
		CacheManager.INSTANCE.register(this);
	}

	@Override
	public Set<T> get() {
		Set<T> results = new TreeSet<T>();
		
		if(this.cache.size() > 0) {
			results.addAll(this.cache.values());
		} else {

			try {
				this.cacheUpdateMutex.acquire();
			} catch (InterruptedException e) {
				return results;
			}
			
			//get results from sql layer DAO
			results = this.dao.get();
			
			//only update if there are already no elements
			if(this.cache.size() == 0) {
				for(T entity: results) {
					this.cache.put(entity.getId(), entity);
				}
			}
			
			this.cacheUpdateMutex.release();
		}
		
		return results;
	}

	@Override
	public Set<T> getByBookId(int bookId) {
		return this.getByTypeId(Book.class, bookId);
	}

	@Override
	public Set<T> getByAuthorId(int authorId) {
		return this.getByTypeId(Author.class, authorId);
	}

	@Override
	public Set<T> getBySeriesId(int seriesId) {
		return this.getByTypeId(Series.class, seriesId);
	}

	@Override
	public Set<T> getByTagId(int tagId) {
		return this.getByTypeId(Tag.class, tagId);
	}

	private Set<T> getByTypeId(Class<?> type, int id) {
		Set<T> results = new TreeSet<T>();
		
		if(type.equals(this.primaryType)) {
			//if nothing is in the cache, it needs to be initialized
			if(this.cache.size() == 0) {
				//use get to initialize the cache
				this.get();
			}
			results.add(this.cache.get(id));
		} else {
			if(Book.class.equals(type)) {
				results = this.byBookId.get(id);
			} else if(Series.class.equals(type)) {
				results = this.bySeriesId.get(id);
			} else if(Tag.class.equals(type)) {
				results = this.byTagId.get(id);
			} else if(Author.class.equals(type)) {
				results = this.byAuthorId.get(id);
			}
		}
		
		if(results == null || results.isEmpty()) {
			try {
				this.cacheUpdateMutex.acquire();
			} catch (InterruptedException e) {
				return results;
			}
			
			if(Book.class.equals(type)) {
				results = this.dao.getByBookId(id);
			} else if(Series.class.equals(type)) {
				results = this.dao.getBySeriesId(id);
			} else if(Tag.class.equals(type)) {
				results = this.dao.getByTagId(id);
			} else if(Author.class.equals(type)) {
				results = this.dao.getByAuthorId(id);
			}
			
			//only update if there are already no elements
			if(this.byBookId.get(id) == null) {
				if(Book.class.equals(type)) {
					this.byBookId.put(id,results);
				} else if(Series.class.equals(type)) {
					this.bySeriesId.put(id,results);
				} else if(Tag.class.equals(type)) {
					this.byTagId.put(id,results);
				} else if(Author.class.equals(type)) {
					this.byAuthorId.put(id,results);
				}
			}
			
			this.cacheUpdateMutex.release();			
		}
		
		return results;
	}
	
	@Override
	public Set<T> queryStartsWith(String property, String prefix) {
		Set<T> results = new TreeSet<T>();
		try {
			this.cacheUpdateMutex.acquire();
		} catch (InterruptedException e) {
			return results;
		}
		
		String key = property + ":" + prefix;
		
		results = this.startsWith.get(key);
		if(results == null) {
			results = this.dao.queryStartsWith(property, prefix);
			this.startsWith.put(key, results);
		}
		
		this.cacheUpdateMutex.release();
		
		return results;
	}

	@Override
	public T build(ResultSet results) throws SQLException {
		return this.dao.build(results);
	}
	
	public void evict() {
		try {
			this.cacheUpdateMutex.acquire();
		} catch (InterruptedException e) {
			//log interrupted while clearing cache
		}
		
		int size = this.cache.size();
		
		this.cache.clear();
		this.byAuthorId.clear();
		this.byBookId.clear();
		this.bySeriesId.clear();
		this.byTagId.clear();
		
		this.logger.info("Evicted {} of type {} from the cache.",""+size,this.primaryType.getSimpleName());
		
		this.cacheUpdateMutex.release();
	}

	public void init() {
		this.dao.get();		
	}
	
}
