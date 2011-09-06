package com.em.janus.dao.calibre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.dao.QueryManager;
import com.em.janus.model.Author;

public enum AuthorDAO implements IDataAccessObject<Author>{
	
	INSTANCE;
	
	@Override
	public Set<Author> get() {
		return this.simpleSearch("");
	}
	
	@Override
	public Set<Author> getByAuthorId(int authorId) {
		return this.simpleSearch("id=?",authorId);
	}
	
	@Override
	public Set<Author> getByBookId(int bookId) {
		return this.query("select a.id, a.name, a.sort from authors a join books_authors_link b on a.id=b.author where b.book=?", bookId);
	}
	
	@Override
	public Set<Author> getBySeriesId(int seriesId) {
		return this.query("select a.id, a.name, a.sort from authors a join books_authors_link b join books_series_link s on s.book=b.book and a.id=b.author where s.series=?", seriesId);
	}
	
	@Override
	public Set<Author> getByTagId(int tagId) {
		return this.query("select a.id, a.name, a.sort from authors a join books_authors_link b join books_tags_link t on t.book=b.book and a.id=b.author where t.tag=?", tagId);
	}

	@Override
	public Set<Author> queryStartsWith(String property, String prefix) {
		if(!prefix.endsWith("%")) {
			prefix = prefix + "%";
		}
		return this.simpleSearch(property + " like ?", prefix);
	}
	
	private Set<Author> simpleSearch(String whereClause, Object ... parameters) {
		//basic clause
		String queryString = "select id, name, sort from authors";
		
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
	
	private Set<Author> query(String queryString, Object ... parameters) {
		return QueryManager.INSTANCE.query(this, queryString, parameters);
	}
	
	@Override
	public Author build(ResultSet fromResults) throws SQLException {
		
		Author author = new Author();
		
		author.setId(fromResults.getInt("id"));
		author.setName(fromResults.getString("name"));
		author.setSortName(fromResults.getString("sort"));
		
		if(author.getName() == null || author.getName().isEmpty()) return null;
			
		return author;
		
	}
		
}
