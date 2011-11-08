package com.em.janus.dao.proxy;

import java.util.Set;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Entity;

public class QueryPrefixProxy<T extends Entity> extends AbstractProxy<T>{

	private String property;
	
	private String prefix;
	
	public QueryPrefixProxy(IDataAccessObject<T> dataAccessObject, String property, String prefix) {
		super(dataAccessObject);

		this.property = property;
		this.prefix = prefix;
	}

	@Override
	public Set<T> get() {
		return this.dao.queryStartsWith(this.property, this.prefix);
	}
	
	public String key() {
		return "by_proprty_" + this.property + "_with_prefix_" + this.prefix;
	}

}
