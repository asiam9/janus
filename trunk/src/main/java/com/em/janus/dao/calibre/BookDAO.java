package com.em.janus.dao.calibre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import com.em.janus.dao.QueryManager;
import com.em.janus.model.Book;

public class BookDAO extends BaseCalibreDAO<Book> {
	
	public BookDAO(String dbPath) {
		super(dbPath);
	}

	private String selectFields = "b.id, b.title, b.sort, b.timestamp, b.pubdate, b.series_index, b.author_sort, b.isbn, b.lccn, b.path, b.uuid, b.has_cover, b.last_modified, c.text";

	public Set<Book> get() {
		return this.simpleSearch("");
	}
	
	public Set<Book> getByBookId(int bookId) {
		return this.simpleSearch("id=?",bookId);
	}
	
	public Set<Book> getByAuthorId(int authorId) {
		return this.query("select " + this.selectFields + " from books b join books_authors_link a on a.book=b.id left outer join comments c on c.book=b.id where a.author=?",authorId);
	}
	
	public Set<Book> getBySeriesId(int seriesId) {
		return this.query("select " + this.selectFields + " from books b join books_series_link s on s.book=b.id left outer join comments c on c.book=b.id where s.series=?",seriesId);
	}
	
	public Set<Book> getByTagId(int tagId) {
		return this.query("select " + this.selectFields + " from books b join books_tags_link t on t.book=b.id left outer join comments c on c.book=b.id where t.tag=?",tagId);
	}
	
	@Override
	public Set<Book> queryStartsWith(String property, String prefix) {
		if(!prefix.endsWith("%")) {
			prefix = prefix + "%";
		}
		return this.simpleSearch(property + " like ?", prefix);
	}
	
	private Set<Book> simpleSearch(String whereClause, Object ... parameters) {
		//basic clause
		String queryString = "select " + this.selectFields + " from books b left outer join comments c on c.book=b.id";
		
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
	
	private Set<Book> query(String queryString, Object ... parameters) {
		return QueryManager.INSTANCE.query(this.getDbPath(), this, queryString, parameters);
	}
	
	public Book build(ResultSet fromResults) throws SQLException {
		
		Book book = new Book();

		//get id and title
		book.setId(fromResults.getInt("id"));
		book.setTitle(fromResults.getString("title"));

		//bail early if title is null
		if(book.getTitle() == null || book.getTitle().isEmpty()) return null;
		
		//rest of properties
		book.setSortTitle(fromResults.getString("sort"));
		book.setSeriesIndex(fromResults.getDouble("series_index"));
		book.setAuthorSortName(fromResults.getString("author_sort"));
		book.setISBN(fromResults.getString("isbn"));
		book.setLCCN(fromResults.getString("lccn"));
		book.setPathToFile(fromResults.getString("path"));
		book.setUUID(fromResults.getString("uuid"));
		book.setHasCover(fromResults.getBoolean("has_cover"));
		book.setCalibreComments(fromResults.getString("text"));
		
		//do date parsing
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
		
		try {
			book.setTimestamp(sdf.parse(fromResults.getString("timestamp")));
		} catch (ParseException e) {
			
		}
		
		try {
			book.setPubdate(sdf.parse(fromResults.getString("pubdate")));
		} catch (ParseException e) {
		
		}
		
		try {
			book.setLastModified(sdf.parse(fromResults.getString("last_modified")));
		} catch (ParseException e) {
			
		}
	
		return book;
	}
}
