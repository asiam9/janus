package com.em.janus.dao.proxy;

import java.util.Set;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Entity;

public class TagProxy<T extends Entity> extends AbstractProxy<T>{

	private int id = -1;
	
	public TagProxy(IDataAccessObject<T> dataAccessObject, int id) {
		super(dataAccessObject);
		this.id = id;
	}

	@Override
	public Set<T> get() {
		return this.dao.getByTagId(this.id);
	}

	public String key() {
		return "by_tag_id:" + id;
	}
	
}
