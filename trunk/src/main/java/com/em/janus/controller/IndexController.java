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
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class IndexController
 */
@WebServlet(description = "Controller that provides home screen and index", urlPatterns = { "/index", "/index.xml", "/index.html" })
public class IndexController extends JanusController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void janusAction(HttpServletRequest request,	HttpServletResponse response, Writer out, String mode)	throws ServletException, IOException {
		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		elements.put("authors", DAOFactory.INSTANCE.getDAO(Author.class).get());
		elements.put("books", DAOFactory.INSTANCE.getDAO(Book.class).get());
		elements.put("series", DAOFactory.INSTANCE.getDAO(Series.class).get());
		elements.put("tags", DAOFactory.INSTANCE.getDAO(Tag.class).get());

		//get index and size parameters
		String sizeString = request.getParameter("size");
		String indexString = request.getParameter("index");
		
		int size = 25;
		int index = 0;
		
		try {
			size = Integer.parseInt(sizeString);
		} catch (Exception e) {
			size = 25;
		}
		
		try {
			index = Integer.parseInt(indexString);
			index += size;
		} catch (Exception e) {
			index = 0;
		}
		
		//add mode to knowledge
		elements.put("mode", mode);
		elements.put("index", index);
		elements.put("size", size);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/index.ftl");
	}
	
}
