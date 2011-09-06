package com.em.janus.dao.calibre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.dao.QueryManager;
import com.em.janus.model.Series;

public enum SeriesDAO implements IDataAccessObject<Series>{

	INSTANCE;

	public Set<Series> get() {
		return this.simpleSearch("");
	}
	
	public Set<Series> getBySeriesId(int seriesId) {
		return this.simpleSearch("id=?",seriesId);
	}
	
	public Set<Series> getByAuthorId(int authorId) {
		return this.query("select s.id, s.name, s.sort from series s join books_series_link b join books_authors_link a on a.book=b.book and s.id=b.series where a.author=?",authorId);
	}
	
	public Set<Series> getByBookId(int bookId) {
		return this.query("select s.id, s.name, s.sort from series s join books_series_link b on s.id=b.series where b.book=?",bookId);
	}
	
	public Set<Series> getByTagId(int tagId) {
		return this.query("select s.id, s.name, s.sort from series s join books_series_link b join books_tags_link t on t.book=b.book and s.id=b.series where t.tag=?",tagId);
	}
	
	@Override
	public Set<Series> queryStartsWith(String property, String prefix) {
		if(!prefix.endsWith("%")) {
			prefix = prefix + "%";
		}
		return this.simpleSearch(property + " like ?", prefix);
	}
	
	private Set<Series> simpleSearch(String whereClause, Object ... parameters) {
		//basic clause
		String queryString = "select id, name, sort from series";
		
		//add where clause when and if it should be added
		if(whereClause != null && !whereClause.isEmpty()) {
			whereClause = whereClause.trim();
			if(whereClause.toLowerCase().startsWith("where")) {
				whereClause = whereClause.replace("where", "");
			}
			queryString += " where " + whereClause;
		}
		return this.query(queryString, parameters);
	}
	
	private Set<Series> query(String queryString, Object ... parameters) {
		return QueryManager.INSTANCE.query(this, queryString, parameters);
	}
	
	public Series build(ResultSet fromResults) throws SQLException {
		
		Series Series = new Series();
		
		Series.setId(fromResults.getInt("id"));
		Series.setName(fromResults.getString("name"));
		Series.setSortName(fromResults.getString("sort"));
		
		if(Series.getName() == null || Series.getName().isEmpty()) return null;
			
		return Series;
		
	}
	
}
