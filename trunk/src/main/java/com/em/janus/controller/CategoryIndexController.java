package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class CategoryIndexController
 */
@WebServlet(
		description = "individual indexes", 
		urlPatterns = { 
				"/book_index", "/book_index.xml", "/book_index.html", 
				"/author_index", "/author_index.xml", "/author_index.html", 
				"/series_index", "/series_index.xml", "/series_index.html", 
				"/tag_index", "/tag_index.xml", "/tag_index.html"
		})
public class CategoryIndexController extends JanusController {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected JanusResponse janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode
		String category = "book";
		IDataAccessObject<?> dao = null;
		if(request.getServletPath().toLowerCase().contains("author")) {
			category="author";
			dao = DAOFactory.INSTANCE.getDAO(Author.class);
		} else if(request.getServletPath().toLowerCase().contains("book")) {
			category="book";
			dao = DAOFactory.INSTANCE.getDAO(Book.class);
		}
		else if(request.getServletPath().toLowerCase().contains("series")) {
			category="series";
			dao = DAOFactory.INSTANCE.getDAO(Series.class);
		}
		else if(request.getServletPath().toLowerCase().contains("tag")) {
			category="tag";
			dao = DAOFactory.INSTANCE.getDAO(Tag.class);
		}
		
		//create response
		JanusResponse janusResponse = new JanusResponse();
		
		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		//add mode to knowledge
		elements.put("mode", mode);
		
		//add all of data objects to elements
		elements.put("category", dao.get());
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/"+category+"_index.ftl");
		
		return janusResponse;
	}

}
