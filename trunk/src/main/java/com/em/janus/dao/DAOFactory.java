package com.em.janus.dao;

import java.util.HashMap;
import java.util.Map;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.dao.cache.NaiveCachedDAO;
import com.em.janus.dao.cache.EhCachedDAO;
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
	
	private IDataAccessObject<Book> book = null;
	private IDataAccessObject<Tag> tag = null;
	private IDataAccessObject<Series> series = null;
	private IDataAccessObject<Author> author = null;
		
	private Map<Class<?>,IDataAccessObject<?>> factoryMap = new HashMap<Class<?>, IDataAccessObject<?>>();
	
	private DAOFactory() {
		if(JanusConfiguration.INSTANCE.useEhCache()) {
			this.book = new EhCachedDAO<Book>(Book.class, BookDAO.INSTANCE);
			this.tag = new EhCachedDAO<Tag>(Tag.class, TagDAO.INSTANCE);
			this.series = new EhCachedDAO<Series>(Series.class, SeriesDAO.INSTANCE);
			this.author = new EhCachedDAO<Author>(Author.class, AuthorDAO.INSTANCE);
		} else {
			this.book = new NaiveCachedDAO<Book>(Book.class, BookDAO.INSTANCE);
			this.tag = new NaiveCachedDAO<Tag>(Tag.class, TagDAO.INSTANCE);
			this.series = new NaiveCachedDAO<Series>(Series.class, SeriesDAO.INSTANCE);
			this.author = new NaiveCachedDAO<Author>(Author.class, AuthorDAO.INSTANCE);
		}	
		
		//add constructed caches to the factory
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
