package com.em.janus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.em.janus.model.Entity;

public interface IDataAccessObject<T extends Entity> {

	public Set<T> get();
	public Set<T> getByBookId(int bookId);
	public Set<T> getByAuthorId(int authorId);
	public Set<T> getBySeriesId(int seriesId);
	public Set<T> getByTagId(int tagId);
	public Set<T> queryStartsWith(String property, String prefix);
	
	public T build(ResultSet results) throws SQLException;
	
}
