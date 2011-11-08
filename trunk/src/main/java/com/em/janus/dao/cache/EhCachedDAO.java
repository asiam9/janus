package com.em.janus.dao.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.em.janus.dao.CacheManager;
import com.em.janus.dao.IDataAccessObject;
import com.em.janus.dao.proxy.AuthorProxy;
import com.em.janus.dao.proxy.BookProxy;
import com.em.janus.dao.proxy.IProxy;
import com.em.janus.dao.proxy.QueryPrefixProxy;
import com.em.janus.dao.proxy.SeriesProxy;
import com.em.janus.dao.proxy.TagProxy;
import com.em.janus.model.Entity;

public class EhCachedDAO <T extends Entity> implements IDataAccessObject<T>, ICachedDAO {

	private Class<?> primaryType = null;
	
	private IDataAccessObject<T> dao = null;
	
	private final Ehcache cache;
	
	private final Ehcache queryCache;
	
	public EhCachedDAO(Class<T> primaryType, IDataAccessObject<T> daoToCache) {
		//decorate the static dao
		this.dao = daoToCache;

		//Decisions based on output class
		this.primaryType = primaryType;
		String cacheName = this.primaryType.getName() + "_object_cache";
		String queryCacheName = this.primaryType.getName() + "_query_cache";
		
		//get cache (from manager)
		this.cache = EhCacheManager.getCache(cacheName);
		this.queryCache = EhCacheManager.getCache(queryCacheName);
		
		//register to be managed for eviction notices
		CacheManager.INSTANCE.register(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<T> get() {
		Set<T> result = null;
		
		if(this.cache != null && this.cache.getSize() > 0 && this.cache.getKeys() != null && !this.cache.getKeys().isEmpty()) {
			result = new HashSet<T>();
			for(Object key : this.cache.getKeys()) {
				Object item = this.get(this.cache, key);
				
				//only entity items should be returned.  this may be legacy now that cache and queryCache are seperate.
				if(item instanceof Entity) {
					result.add((T)item);
				}
			}
		} else {
			result = this.dao.get();
			//cache result since it wasn't in the cache
			for(T item : result) {
				this.put(this.cache, item);
			}
		}
		
		return result;
	}

	@Override
	public Set<T> getByBookId(int bookId) {
		IProxy<T> proxy = new BookProxy<T>(this.dao, bookId);
		return this.getFromCache(proxy);	

	}

	@Override
	public Set<T> getByAuthorId(int authorId) {
		IProxy<T> proxy = new AuthorProxy<T>(this.dao, authorId);
		return this.getFromCache(proxy);	

	}

	@Override
	public Set<T> getBySeriesId(int seriesId) {
		IProxy<T> proxy = new SeriesProxy<T>(this.dao, seriesId);
		return this.getFromCache(proxy);	

	}

	@Override
	public Set<T> getByTagId(int tagId) {
		IProxy<T> proxy = new TagProxy<T>(this.dao, tagId);
		return this.getFromCache(proxy);	

	}

	@Override
	public Set<T> queryStartsWith(String property, String prefix) {
		IProxy<T> proxy = new QueryPrefixProxy<T>(this.dao, property, prefix);
		return this.getFromCache(proxy);	
	}
	
	@SuppressWarnings("unchecked")
	private Set<T> getFromCache(IProxy<T> proxy) {
		//create key
		String key = proxy.key();
		
		Object found = this.get(this.queryCache, key);
		
		Set<T> result = null;		
		
		if(found == null) {
			result = proxy.get();
			
			Set<Object> keySet = new HashSet<Object>();
			for(T item : result) {
				//add item to key set
				keySet.add(item.getId());
				
				//add item to cache (ensure item is in cache)
				this.put(this.cache, item);
			}
			
			//add whole key set to map
			Element keySetElement = new Element(key, keySet);
			this.queryCache.put(keySetElement);			
		} else {
			result = new HashSet<T>();
			
			//get from key
			Object value = this.get(this.queryCache, key);
			
			Set<Object> keySet = null;
			
			if(value != null && value instanceof Set) {
				keySet = (Set<Object>)value; 
				
				for(Object itemKey : keySet) {
					Object itemValue = this.get(this.cache, itemKey);
					result.add((T)itemValue);				
				}
			} 
		}
		
		return result;		
	}
	
	private void put(Ehcache cache, T item) {
		if(item == null) return;
		cache.put(new Element(item.getId(), item));
	}

	private Object get(Ehcache cache, Object key) {
		Element got = cache.get(key);
		if(got == null) {
			return null;
		}
		return got.getValue();
	}
	
	@Override
	public T build(ResultSet results) throws SQLException {
		return this.dao.build(results);
	}

	@Override
	public void evict() {
		this.cache.removeAll();
	}

	@Override
	public void init() {
		this.get();
	}
}
