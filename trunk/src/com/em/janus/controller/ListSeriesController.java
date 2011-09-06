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
import com.em.janus.dao.DAOFactory;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.sorting.SeriesBookCountComparator;
import com.em.janus.model.sorting.SeriesRecentlyAddedComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListAuthorsController
 */
@WebServlet(description = "lists series based on given criteria", urlPatterns = { "/series_list", "/series_list.xml", "/series_list.html" })
public class ListSeriesController extends JanusController {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode
		String sort = request.getParameter("sort");
		//sort is a potential SQL injection problem.  it must equal "name" or "books" or "date"
		if(sort == null || sort.isEmpty() || (!"books".equals(sort) && !"date".equals(sort))) sort = "name";

		//starts with
		String starts = request.getParameter("starts");
		if(starts == null || starts.isEmpty()) starts = "";
		
		//create template map
		Map<String, Object> elements = new HashMap<String, Object>();

		//create null authors list
		List<Series> series = null;
		
		if(starts.isEmpty() || "books".equals(sort) || "date".equals(sort)) {			
			//get index and size parameters
			String sizeString = request.getParameter("size");
			String indexString = request.getParameter("index");
				
			int size = JanusConfiguration.INSTANCE.getPageSize();
			int index = 0;
			
			try {
				size = Integer.parseInt(sizeString);
			} catch (Exception e) {
				size = JanusConfiguration.INSTANCE.getPageSize();
			}
			
			try {
				index = Integer.parseInt(indexString);
			} catch (Exception e) {
				index = 0;
			}
			
			//get and chop and sort
			series = new ArrayList<Series>(DAOFactory.INSTANCE.getDAO(Series.class).get());
			
			//get books for each author
			if("books".equals(sort) || "date".equals(sort)) {
				for(Series s : series) {
					s.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getBySeriesId(s.getId()));
				}
			}
			
			//create default comparator
			Comparator<Series> comparator = null;
			if("books".equals(sort)) {
				comparator = new SeriesBookCountComparator();
			} else if("date".equals(sort)) {
				comparator = new SeriesRecentlyAddedComparator();
			}
			
			//only if a comparator is provided.  this is used to keep the natural sort
			//when no "sort" parameter is provided
			if(comparator != null) {
				//sort using comparator
				Collections.sort(series,comparator);
			}
				
			//grab size
			int end = index + size;
			int nextIndex = index;
			if(end > series.size()) {
				end = series.size();
			} else {
				nextIndex += size;
			}
			
			//chop the list, when not in json mode
			if(!"json".equals(mode)){
				series = series.subList(index, end);
			}
			
			//update size with previously calculated (in bounds) next index
			index = nextIndex;
			
			//add index and size back to template
			elements.put("index", index);
			elements.put("size",size);			
		} else {
			//get authors
			series = new ArrayList<Series>(DAOFactory.INSTANCE.getDAO(Series.class).queryStartsWith(sort, starts));
			//use only one type of "natural" sort
			Collections.sort(series);
		}
		
		//grab books for counts
		for(Series s : series) {
			s.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getBySeriesId(s.getId()));
			s.setAuthors(DAOFactory.INSTANCE.getDAO(Author.class).getBySeriesId(s.getId()));
			//s.setTags(TagDAO.INSTANCE.getBySeriesId(s.getId()));
		}
		
		//put elements into template map
		elements.put("series",series);
		elements.put("mode",mode);
		elements.put("sort",sort);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/series_list.ftl");		
	}
       
 
}
