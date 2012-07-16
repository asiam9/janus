package com.em.janus.dao.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

public class EhCacheManager {

	private static CacheManager manager = null;
	
	public static Ehcache getCache(String name) {
		CacheManager manager = EhCacheManager.manager;
		
		if(manager == null) {
			//create cache
			Configuration cacheManagerConfig = new Configuration();
			CacheConfiguration cacheConfig = new CacheConfiguration(name, 0);
			cacheConfig.eternal(true);
			//cacheConfig.overflowToDisk(true);
			//cacheConfig.setDiskStorePath(System.getProperty("java.io.tmpdir"));			
			
			//create manager and add new cache to manager
			manager = new net.sf.ehcache.CacheManager(cacheManagerConfig);
			manager.addCache(new Cache(cacheConfig));
		}
		
		Ehcache cache = manager.getCache(name);
		return cache;
	}
	
	public static void close() {
		//only clear and close if there are active caches
		if(EhCacheManager.manager != null && EhCacheManager.manager.getCacheNames().length > 0) {
			EhCacheManager.manager.clearAll();
			EhCacheManager.manager.shutdown();
		}
	}
	
}
