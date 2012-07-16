package com.em.janus.dao.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.em.janus.dao.CacheManager;
import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Entity;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;

public class InfinispanCachedDAO<T extends Entity> implements ICachedDAO, IDataAccessObject<T> {

	private IDataAccessObject<T> dao = null;
	
	/*
	private Semaphore cacheUpdateMutex = new Semaphore(1);
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	*/
	
	private Class<?> primaryType = null;
	
	private EmbeddedCacheManager manager = null;
	
	private boolean preloaded = false;
	
	public InfinispanCachedDAO(Class<T> primaryType, IDataAccessObject<T> daoToCache) {
		//decorate the static dao
		this.dao = daoToCache;
		
		//decisions based on output class
		this.primaryType = primaryType;
		
		//configuration
		Configuration config = (new ConfigurationBuilder()).eviction().maxEntries(0).build(); 
		
		//build cache
		this.manager = new DefaultCacheManager();
		this.manager.defineConfiguration(this.primaryType.getName().toLowerCase() + "-book", config);
		this.manager.defineConfiguration(this.primaryType.getName().toLowerCase() + "-series", config);
		this.manager.defineConfiguration(this.primaryType.getName().toLowerCase() + "-tag", config);
		this.manager.defineConfiguration(this.primaryType.getName().toLowerCase() + "-author", config);
		this.manager.defineConfiguration(this.primaryType.getName().toLowerCase() + "-prefix", config);
				
		//register to be managed for eviction notices
		CacheManager.INSTANCE.register(this);
	}
	
	@Override
	public Set<T> get() {
		Set<T> result = Collections.emptySet();

		Cache<String, T> primaryCache = null;
		
		primaryCache = this.manager.getCache(this.getCacheName(this.primaryType));
			
		if(primaryCache.isEmpty()) {
			result = this.dao.get();
			for(T r : result) {
				primaryCache.put(Integer.toString(r.getId()), r);
			}
			this.preloaded = true;
		} else {
			result = Collections.unmodifiableSet(new HashSet<T>(primaryCache.values()));
		}
		
		return result;
	}

	@Override
	public Set<T> getByBookId(int bookId) {
		return this.get(Integer.toString(bookId), Book.class);
	}

	@Override
	public Set<T> getByAuthorId(int authorId) {
		return this.get(Integer.toString(authorId), Author.class);
	}

	@Override
	public Set<T> getBySeriesId(int seriesId) {
		return this.get(Integer.toString(seriesId), Series.class);
	}

	@Override
	public Set<T> getByTagId(int tagId) {
		return this.get(Integer.toString(tagId), Tag.class);	
	}

	@Override
	public Set<T> queryStartsWith(String property, String prefix) {
		String key = property + ":" + prefix;
		return this.get(key, null);
	}
	
	private Set<T> get(String key, Class<?> getClass) {
		//bail if the get class is  null
		if(getClass == null) return Collections.emptySet();
		
		//results
		Set<T> result = Collections.emptySet();
		
		//if the class is the same as the primary cache, reach into the get method
		if(this.primaryType.equals(getClass)) {
			result = new HashSet<T>(1);
			if(!this.preloaded) {
				this.get();
			}
			Cache<String, T> primaryCache = this.manager.getCache(this.getCacheName(this.primaryType));
			if(primaryCache.containsKey(key)) {
				T single = primaryCache.get(key);
				result.add(single);
			}
		} else {
			String cacheName = this.getCacheName(getClass);
			Cache<String, Set<T>> secondaryCache = this.manager.getCache(cacheName);
			
			if(secondaryCache.containsKey(key)) {
				result = secondaryCache.get(key);
			} else {
				if(Book.class.equals(getClass)) {
					result = this.dao.getByBookId(Integer.parseInt(key));
				} else if(Series.class.equals(getClass)) {
					result = this.dao.getBySeriesId(Integer.parseInt(key));
				} else if(Tag.class.equals(getClass)) {
					result = this.dao.getByTagId(Integer.parseInt(key));
				} else if(Author.class.equals(getClass)) {
					result = this.dao.getByAuthorId(Integer.parseInt(key));
				} else {
					result = this.dao.queryStartsWith(key.split(":")[0], key.split(":")[1]);
				}
				secondaryCache.put(key, result);
			}
		}
		
		return result;
	}
	
	@Override
	public T build(ResultSet results) throws SQLException {
		return this.dao.build(results);
	}

	@Override
	public void evict() {
		//for each cache, clear
		for(String name : this.manager.getCacheNames()) {
			this.manager.getCache(name).clear();
		}		
		this.preloaded = false;
	}

	@Override
	public void init() {
		this.get();
	}
	
	private String getCacheName(Class<?> modelType) {
		String suffix = null;
		
		if(modelType == null) {
			suffix = "-prefix";
		} else if(modelType.equals(Book.class)) {
			suffix = "-book";
		} else if(modelType.equals(Series.class)) {
			suffix = "-series";
		} else if(modelType.equals(Tag.class)) {
			suffix = "-tag";
		} else if(modelType.equals(Author.class)) {	
			suffix = "-author";
		} 
		
		return this.primaryType.getName().toLowerCase() + suffix;
	}
	
}
