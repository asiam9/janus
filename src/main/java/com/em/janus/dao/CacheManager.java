package com.em.janus.dao;

import java.util.HashSet;
import java.util.Set;

import com.em.janus.dao.cache.ICachedDAO;

public enum CacheManager {

	INSTANCE;
	
	private Set<ICachedDAO> cachedDaoProviders = new HashSet<ICachedDAO>();
	
	public void register(ICachedDAO cachedDAO) {
		this.cachedDaoProviders.add(cachedDAO);
	}
	
	public void evict() {
		for(ICachedDAO cached : this.cachedDaoProviders) {
			cached.evict();
		}
	}
	
	public void unregisterAll() {
		this.cachedDaoProviders.clear();
	}

	public void init() {
		for(ICachedDAO cached : this.cachedDaoProviders) {
			cached.init();
		}		
	}
}
