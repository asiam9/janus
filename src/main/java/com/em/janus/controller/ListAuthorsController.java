package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.config.ServletConfigUtility;
import com.em.janus.dao.DAOFactory;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;
import com.em.janus.model.sorting.AuthorBookCountComparator;
import com.em.janus.model.sorting.AuthorNameComparator;
import com.em.janus.model.sorting.AuthorSeriesCountComparator;
import com.em.janus.model.sorting.AuthorTagCountComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListAuthorsController
 */
@WebServlet(description = "lists authors based on given criteria", urlPatterns = { "/authors", "/authors.xml", "/authors.html" })
public class ListAuthorsController extends JanusController {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//get configuration
		JanusConfiguration config = ServletConfigUtility.getConfigurationFromContext(request.getServletContext());
		
		//get potential series id
		String seriesIdString = request.getParameter("series");
		int seriesId = 0;
		try {
			seriesId = Integer.parseInt(seriesIdString);
		} catch (Exception ex) {
			seriesId = 0;
		}
		
		//get potential tag id
		String tagIdString = request.getParameter("tag");
		int tagId = 0;
		try {
			tagId = Integer.parseInt(tagIdString);
		} catch (Exception ex) {
			tagId = 0;
		}
	
		//sort mode
		String sort = request.getParameter("sort");
		//sort is a potential SQL injection problem.  it must equal "name", "books", "tags", "series", or "sort"
		//sort by name when showing only series authors
		if(seriesId > 0 || tagId > 0 || sort == null || sort.isEmpty() || (!"books".equals(sort) && !"tags".equals(sort) && !"series".equals(sort) && !"name".equals(sort) && !"sort".equals(sort))) {
			sort = "name";
		}
		
		//starts with
		String starts = request.getParameter("starts");
		if(starts == null || starts.isEmpty()) starts = "";
		
		//create template map
		Map<String, Object> elements = new HashMap<String, Object>();

		//create null authors list
		List<Author> authors = null;
		
		if(seriesId == 0 && tagId == 0 && (starts.isEmpty() || "books".equals(sort) || "tags".equals(sort) || "series".equals(sort))) {			
			//get index and size parameters
			String sizeString = request.getParameter("size");
			String indexString = request.getParameter("index");
				
			int size = config.getPageSize();
			int index = 0;
			
			try {
				size = Integer.parseInt(sizeString);
			} catch (Exception e) {
				size = config.getPageSize();
			}
			
			try {
				index = Integer.parseInt(indexString);
			} catch (Exception e) {
				index = 0;
			}
			
			//get all authors
			authors	= new ArrayList<Author>(DAOFactory.INSTANCE.getDAO(Author.class).get());
			
			//get books for each author
			for(Author author : authors) {
				if("tags".equals(sort)) {
					author.setTags(DAOFactory.INSTANCE.getDAO(Tag.class).getByAuthorId(author.getId()));
				} else if("series".equals(sort)) {
					author.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByAuthorId(author.getId()));
				} else {
					author.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getByAuthorId(author.getId()));
				}
			}
			
			//create default comparator
			Comparator<Author> comparator = new AuthorBookCountComparator();
			if("tags".equals(sort)) {
				comparator = new AuthorTagCountComparator();
			} else if("series".equals(sort)) {
				comparator = new AuthorSeriesCountComparator();
			}
			
			//sort using comparator
			Collections.sort(authors,comparator);
			
			//grab size
			int end = index + size;
			int nextIndex = index;
			if(end > authors.size()) {
				end = authors.size();
			} else {
				nextIndex += size;
			}
			
			//chop the list, when not in json mode
			if(!"json".equals(mode)){
				authors = authors.subList(index, end);
			}
			
			//update size with previously calculated (in bounds) next index
			index = nextIndex;
			
			//add index and size back to template
			elements.put("index", index);
			elements.put("size",size);			
		} else {
			
			//if we're showing a series, then only get those authors
			if(seriesId > 0) {
				authors = new ArrayList<Author>(DAOFactory.INSTANCE.getDAO(Author.class).getBySeriesId(seriesId));
			} else if(tagId > 0) {
				authors = new ArrayList<Author>(DAOFactory.INSTANCE.getDAO(Author.class).getByTagId(tagId));
			} else {
				//otherwise, get all authors
				authors = new ArrayList<Author>(DAOFactory.INSTANCE.getDAO(Author.class).queryStartsWith(sort, starts));
			}
			
			//sort authors
			if("sort".equals(sort)) {
				Collections.sort(authors);
			}  else {
				Comparator<Author> comparator = new AuthorNameComparator();
				Collections.sort(authors,comparator);
			}
			
		}
		
		//grab other model items for counts
		for(Author author : authors) {
			author.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getByAuthorId(author.getId()));
			author.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByAuthorId(author.getId()));
			author.setTags(DAOFactory.INSTANCE.getDAO(Tag.class).getByAuthorId(author.getId()));
		}
		
		//put elements into template map
		elements.put("authors",authors);
		elements.put("mode",mode);
		elements.put("sort",sort);
		elements.put("tag",tagId);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/authors.ftl");		
	}
       
 
}
