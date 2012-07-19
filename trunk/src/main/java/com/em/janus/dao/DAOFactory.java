package com.em.janus.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.dao.cache.InfinispanCachedDAO;
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
	
	private Logger log = LoggerFactory.getLogger(DAOFactory.class);
	
	private JanusConfiguration config = null;
	
	private IDataAccessObject<Book> book = null;
	private IDataAccessObject<Tag> tag = null;
	private IDataAccessObject<Series> series = null;
	private IDataAccessObject<Author> author = null;
		
	private Map<Class<?>,IDataAccessObject<?>> factoryMap = new HashMap<Class<?>, IDataAccessObject<?>>();
	
	private DAOFactory() {

	}
	
	public void setConfig(JanusConfiguration config) {
		this.config = config;
		
		//clear
		this.factoryMap.clear();
		
		BookDAO bookDAO = new BookDAO(config.getDatabasePath());
		TagDAO tagDAO = new TagDAO(config.getDatabasePath());
		SeriesDAO seriesDAO = new SeriesDAO(config.getDatabasePath());
		AuthorDAO authorDAO = new AuthorDAO(config.getDatabasePath());
		
		//get provider
		String provider = this.config.getCacheProvider();
		
		//build dao based on provider
		if("eh".equalsIgnoreCase(provider) || "ehcache".equalsIgnoreCase(provider)) {
			this.book = new EhCachedDAO<Book>(Book.class, bookDAO);
			this.tag = new EhCachedDAO<Tag>(Tag.class, tagDAO);
			this.series = new EhCachedDAO<Series>(Series.class, seriesDAO);
			this.author = new EhCachedDAO<Author>(Author.class, authorDAO);
		} else if ("infinispan".equalsIgnoreCase(provider)){
			this.book = new InfinispanCachedDAO<Book>(Book.class, bookDAO);
			this.tag = new InfinispanCachedDAO<Tag>(Tag.class, tagDAO);
			this.series = new InfinispanCachedDAO<Series>(Series.class, seriesDAO);
			this.author = new InfinispanCachedDAO<Author>(Author.class, authorDAO);
		} else if(!"none".equalsIgnoreCase(provider)){
			this.book = new NaiveCachedDAO<Book>(Book.class, bookDAO);
			this.tag = new NaiveCachedDAO<Tag>(Tag.class, tagDAO);
			this.series = new NaiveCachedDAO<Series>(Series.class, seriesDAO);
			this.author = new NaiveCachedDAO<Author>(Author.class, authorDAO);
		} else {
			this.book = bookDAO;
			this.tag = tagDAO;
			this.series = seriesDAO;
			this.author = authorDAO;
		}
		
		//add constructed caches to the factory
		this.factoryMap.put(Author.class, this.author);
		this.factoryMap.put(Tag.class, this.tag);
		this.factoryMap.put(Series.class, this.series);
		this.factoryMap.put(Book.class, this.book);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> IDataAccessObject<T> getDAO(Class<T> forClass) {
		IDataAccessObject<T> dao = (IDataAccessObject<T>)this.factoryMap.get(forClass);
		this.log.debug("Using DAO Class: {} for {}", dao.getClass(), forClass.getName());
		return dao;
	}
		
}
