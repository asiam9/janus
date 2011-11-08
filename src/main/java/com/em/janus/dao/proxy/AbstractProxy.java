package com.em.janus.dao.proxy;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Entity;

public abstract class AbstractProxy<T extends Entity> implements IProxy<T> {

	protected IDataAccessObject<T> dao = null;
	
	private AbstractProxy() {
		
	}
	
	public AbstractProxy(IDataAccessObject<T> dataAccessObject) {
		this();
		this.dao = dataAccessObject;
	}
	
}
