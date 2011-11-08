package com.em.janus.dao.cache;

public interface ICachedDAO {

	public void evict();
	
	public void init();	
}
