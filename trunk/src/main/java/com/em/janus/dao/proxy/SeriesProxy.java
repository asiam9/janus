package com.em.janus.dao.proxy;

import java.util.Set;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Entity;

public class SeriesProxy<T extends Entity> extends AbstractProxy<T>{

	private int id = -1;
	
	public SeriesProxy(IDataAccessObject<T> dataAccessObject, int id) {
		super(dataAccessObject);
		this.id = id;
	}

	@Override
	public Set<T> get() {
		return this.dao.getBySeriesId(this.id);
	}
	
	public String key() {
		return "by_series_id:" + id;
	}

}
