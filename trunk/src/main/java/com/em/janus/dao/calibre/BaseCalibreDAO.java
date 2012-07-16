package com.em.janus.dao.calibre;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Entity;

public abstract class BaseCalibreDAO<T extends Entity> implements IDataAccessObject<T> {

	private String dbPath = null;
	
	public BaseCalibreDAO(String dbPath) {
		this.dbPath = dbPath;
	}
	
	public String getDbPath() {
		return this.dbPath;
	}
	
}
