package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class TagController
 */
@WebServlet(description = "handles tags and sub tags and sub tags", urlPatterns = { "/tag", "/tag.xml", "/tag.html" })
public class TagController extends JanusController {
	private static final long serialVersionUID = 1L;
  
	@Override
	protected JanusResponse janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			id = 0;
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		//get tag
		Set<Tag> tags = DAOFactory.INSTANCE.getDAO(Tag.class).getByTagId(id);
		Tag tag = null;
		if(tags != null && tags.size() > 0) {
			tag = tags.iterator().next();
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		//get books, authors, and series for detail
		Set<Book> books = DAOFactory.INSTANCE.getDAO(Book.class).getByTagId(tag.getId());
		Set<Author> authors = DAOFactory.INSTANCE.getDAO(Author.class).getByTagId(tag.getId());
		Set<Series> series = DAOFactory.INSTANCE.getDAO(Series.class).getByTagId(tag.getId());
		
		//create data model
		Map<String,Object> elements = new HashMap<String, Object>();
	
		//elements
		elements.put("tag", tag);
		elements.put("mode", mode);
		elements.put("books", books);
		elements.put("series", series);
		elements.put("authors", authors);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/tag.ftl");
		
		JanusResponse janusResponse = new JanusResponse();
		
		return janusResponse;
	}

}
