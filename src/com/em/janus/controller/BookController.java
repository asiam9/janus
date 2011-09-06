package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.BookFilesAO;
import com.em.janus.model.Author;
import com.em.janus.model.Book;
import com.em.janus.model.FileInfo;
import com.em.janus.model.Series;
import com.em.janus.model.Tag;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class BookController
 */
@WebServlet(description = "A single book's details", urlPatterns = { "/book", "/book.xml", "/book.html" })
public class BookController extends JanusController {

	private static final long serialVersionUID = 1L;
       
	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {

		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		//get book
		Book book = DAOFactory.INSTANCE.getDAO(Book.class).getByBookId(id).iterator().next();
		
		//get other data
		book.setAuthors(DAOFactory.INSTANCE.getDAO(Author.class).getByBookId(id));
		book.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByBookId(id));
		book.setTags(DAOFactory.INSTANCE.getDAO(Tag.class).getByBookId(id));
		
		List<FileInfo> ebookFiles = BookFilesAO.INSTANCE.getEbookFiles(request.getServletContext(), book); 
		
		//create elements map
		Map<String,Object> elements = new HashMap<String, Object>();
	
		//book id and node
		elements.put("book", book);
		elements.put("mode", mode);
		elements.put("files",ebookFiles);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/book.ftl");
	}

}
