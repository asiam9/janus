package com.em.janus.dao.calibre;

import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Entity;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;

public class CalibreDAOFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Entity> IDataAccessObject<T> getDAO(Class<T> forClass) {
		
		IDataAccessObject<T> dao = null;
		
		if(Author.class.equals(forClass)) {
			dao = (IDataAccessObject<T>)AuthorDAO.INSTANCE;
		} else if(Book.class.equals(forClass)) {
			dao = (IDataAccessObject<T>)BookDAO.INSTANCE;
		} else if(Series.class.equals(forClass)) {
			dao = (IDataAccessObject<T>)SeriesDAO.INSTANCE;
		} else if(Tag.class.equals(forClass)) {
			dao = (IDataAccessObject<T>)TagDAO.INSTANCE;
		}
		
		return dao;
	}
	
}
