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
import com.em.janus.model.Tag;
import com.em.janus.model.sorting.TagBookCountComparator;
import com.em.janus.model.sorting.TagSeriesCountComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListAuthorsController
 */
@WebServlet(description = "lists tags based on given criteria", urlPatterns = { "/tag_list", "/tag_list.xml", "/tag_list.html" })
public class ListTagsController extends JanusController {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode
		String sort = request.getParameter("sort");
		//sort is a potential SQL injection problem.  it must equal "name" or "books" or "series"
		if(sort == null || sort.isEmpty() || (!"books".equals(sort) && !"series".equals(sort))) sort = "name";

		//starts with
		String starts = request.getParameter("starts");
		if(starts == null || starts.isEmpty()) starts = "";
		
		//create template map
		Map<String, Object> elements = new HashMap<String, Object>();

		//create null authors list
		List<Tag> tags = null;
		
		if(starts.isEmpty() || "books".equals(sort) || "series".equals(sort)) {			
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
			tags = new ArrayList<Tag>(DAOFactory.INSTANCE.getDAO(Tag.class).get());
			
			//grab books and authors for counts
			for(Tag t : tags) {
				t.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getByTagId(t.getId()));
				t.setAuthors(DAOFactory.INSTANCE.getDAO(Author.class).getByTagId(t.getId()));
				t.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByTagId(t.getId()));
			}
			
			//create default comparator
			Comparator<Tag> comparator = null;
			if("books".equals(sort)) {
				comparator = new TagBookCountComparator();
			} else if("series".equals(sort)) {
				comparator = new TagSeriesCountComparator();
			}
			
			//only if a comparator is provided.  this is used to keep the natural sort
			//when no "sort" parameter is provided
			if(comparator != null) {
				//sort using comparator
				Collections.sort(tags,comparator);
			} else {
				Collections.sort(tags);
			}
				
			//grab size
			int end = index + size;
			int nextIndex = index;
			if(end > tags.size()) {
				end = tags.size();
			} else {
				nextIndex += size;
			}
			
			//chop the list, when not in json mode
			if(!"json".equals(mode)){
				tags = tags.subList(index, end);
			}
			
			//update size with previously calculated (in bounds) next index
			index = nextIndex;
			
			//add index and size back to template
			elements.put("index", index);
			elements.put("size",size);			
		} else {
			//get authors
			tags = new ArrayList<Tag>(DAOFactory.INSTANCE.getDAO(Tag.class).queryStartsWith(sort, starts));
			//use only one type of "natural" sort
			Collections.sort(tags);
			
			//grab books and authors for counts
			for(Tag t : tags) {
				t.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getByTagId(t.getId()));
				t.setAuthors(DAOFactory.INSTANCE.getDAO(Author.class).getByTagId(t.getId()));
				t.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByTagId(t.getId()));
			}
		}
		
		//put elements into template map
		elements.put("tags",tags);
		elements.put("mode",mode);
		elements.put("sort",sort);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/tag_list.ftl");		
	}
       
 
}
