package com.em.janus.dao;

import java.util.HashSet;
import java.util.Set;

import com.em.janus.dao.cache.CachedDAO;

public enum CacheManager {

	INSTANCE;
	
	private Set<CachedDAO<?>> cachedDaoProviders = new HashSet<CachedDAO<?>>();
	
	public void register(CachedDAO<?> cachedDAO) {
		this.cachedDaoProviders.add(cachedDAO);
	}
	
	public void evict() {
		for(CachedDAO<?> cached : this.cachedDaoProviders) {
			cached.evict();
		}
	}
	
	public void unregisterAll() {
		this.cachedDaoProviders.clear();
	}

	public void init() {
		for(CachedDAO<?> cached : this.cachedDaoProviders) {
			cached.init();
		}		
	}
}
