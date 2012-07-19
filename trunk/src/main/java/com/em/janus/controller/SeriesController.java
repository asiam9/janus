package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.BookFilesAO;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class SeriesController
 */
@WebServlet(description = "everything for one series", urlPatterns = { "/series", "/series.xml", "/series.html" })
public class SeriesController extends JanusController {
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
		
		Set<Series> seriesList = DAOFactory.INSTANCE.getDAO(Series.class).getBySeriesId(id);
		Series series = null;
		if(seriesList != null && seriesList.size() > 0) {
			series = seriesList.iterator().next();
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		//create elements map
		Map<String,Object> elements = new HashMap<String, Object>();
	
		//book id and node
		elements.put("series", series);
		elements.put("mode", mode);
		
		List<Book> books = new ArrayList<Book>(DAOFactory.INSTANCE.getDAO(Book.class).getBySeriesId(series.getId()));
		Collections.sort(books);
		for(Book book : books) {
			book.getFileInfo().addAll(BookFilesAO.INSTANCE.getEbookFiles(request.getServletContext(), book));
		}		
		
		//add books within this series to the template, should sort naturally by book id
		elements.put("books", books);
		elements.put("authors", DAOFactory.INSTANCE.getDAO(Author.class).getBySeriesId(series.getId()));
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/series.ftl");
		
		JanusResponse janusResponse = new JanusResponse();
		
		return janusResponse;		
	}


}
