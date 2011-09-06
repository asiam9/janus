package com.em.janus.dao;

import java.util.HashMap;
import java.util.Map;

import com.em.janus.dao.cache.CachedDAO;
import com.em.janus.dao.calibre.AuthorDAO;
import com.em.janus.dao.calibre.BookDAO;
import com.em.janus.dao.calibre.SeriesDAO;
import com.em.janus.dao.calibre.TagDAO;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Entity;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;

public enum DAOFactory {

	INSTANCE;
	
	private IDataAccessObject<Book> book = new CachedDAO<Book>(Book.class, BookDAO.INSTANCE);
	private IDataAccessObject<Tag> tag = new CachedDAO<Tag>(Tag.class, TagDAO.INSTANCE);
	private IDataAccessObject<Series> series = new CachedDAO<Series>(Series.class, SeriesDAO.INSTANCE);
	private IDataAccessObject<Author> author = new CachedDAO<Author>(Author.class, AuthorDAO.INSTANCE);
		
	private Map<Class<?>,IDataAccessObject<?>> factoryMap = new HashMap<Class<?>, IDataAccessObject<?>>();
	
	private DAOFactory() {
		this.factoryMap.put(Author.class, this.author);
		this.factoryMap.put(Tag.class, this.tag);
		this.factoryMap.put(Series.class, this.series);
		this.factoryMap.put(Book.class, this.book);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> IDataAccessObject<T> getDAO(Class<T> forClass) {
		return (IDataAccessObject<T>)this.factoryMap.get(forClass);
	}
		
}
