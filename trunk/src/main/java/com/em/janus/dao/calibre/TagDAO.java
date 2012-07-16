package com.em.janus.dao.calibre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.em.janus.dao.QueryManager;
import com.em.janus.model.Tag;

public class TagDAO extends BaseCalibreDAO<Tag> {
		
	public TagDAO(String dbPath) {
		super(dbPath);
	}

	public Set<Tag> get() {
		return this.simpleSearch("");
	}
	
	public Set<Tag> getByTagId(int tagId) {
		return this.simpleSearch("id=?",tagId);
	}
	
	public Set<Tag> getByBookId(int bookId) {
		return this.query("select t.id, t.name from tags t join books_tags_link b on b.tag=t.id where b.book=?",bookId);
	}	
	
	public Set<Tag> getBySeriesId(int seriesId) {
		return this.query("select t.id, t.name from tags t join books_tags_link b join books_series_link s on b.tag=t.id and b.book=s.book where s.series=?",seriesId);
	}
	
	public Set<Tag> getByAuthorId(int authorId) {
		return this.query("select t.id, t.name from tags t join books_tags_link b join books_authors_link a on b.tag=t.id and b.book=a.book where a.author=?",authorId);
	}
	
	@Override
	public Set<Tag> queryStartsWith(String property, String prefix) {
		if(!prefix.endsWith("%")) {
			prefix = prefix + "%";
		}
		return this.simpleSearch(property + " like ?", prefix);
	}

	private Set<Tag> simpleSearch(String whereClause, Object ... parameters) {
		//basic clause
		String queryString = "select id, name from tags";
		
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
	
	private Set<Tag> query(String queryString, Object ... parameters) {
		return QueryManager.INSTANCE.query(this.getDbPath(), this, queryString, parameters);
	}
	
	public Tag build(ResultSet fromResults) throws SQLException {
		
		Tag tag = new Tag();
		
		tag.setId(fromResults.getInt("id"));
		tag.setName(fromResults.getString("name"));
	
		if(tag.getName() == null || tag.getName().isEmpty()) return null;
		
		return tag;
	}
	
}

