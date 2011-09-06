package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.calibre.SeriesDAO;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.Series;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class AuthorController
 */
@WebServlet(description = "everything for one author", urlPatterns = { "/author","/author.xml","/author.html" })
public class AuthorController extends JanusController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			id = 0;
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		
		Set<Author> authors = DAOFactory.INSTANCE.getDAO(Author.class).getByAuthorId(id);
		Author author = null;
		if(authors != null && authors.size() > 0) {
			author = authors.iterator().next();
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if(author == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		//create elements map
		Map<String,Object> elements = new HashMap<String, Object>();
	
		//book id and node
		elements.put("author", author);
		elements.put("mode", mode);
		
		Set<Series> series = DAOFactory.INSTANCE.getDAO(Series.class).getByAuthorId(id);
		for(Series s : series) {
			s.setBooks(DAOFactory.INSTANCE.getDAO(Book.class).getBySeriesId(s.getId()));
		}
		elements.put("series", series);
		
		//get books without series information
		Set<Book> books = DAOFactory.INSTANCE.getDAO(Book.class).getByAuthorId(id);
		Set<Book> noSeriesBooks = new TreeSet<Book>();
		for(Book book : books) {
			book.setSeries(SeriesDAO.INSTANCE.getByBookId(book.getId()));
			if(book.getSeries().size() == 0) {
				noSeriesBooks.add(book);
			}
		}
		
		//add books with no series to base template
		elements.put("books", noSeriesBooks);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/author.ftl");
		
	}


}
