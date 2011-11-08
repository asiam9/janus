package com.em.janus.dao.proxy;

import java.util.Set;

import com.em.janus.model.Entity;

public interface IProxy<T extends Entity> {

	public Set<T> get();
	
	public String key();
	
}
