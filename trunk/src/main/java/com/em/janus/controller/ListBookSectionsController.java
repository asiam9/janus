package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.model.Book;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.model.sections.Section;
import com.em.janus.model.sorting.BookTitleComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListBookSections
 */
@WebServlet(description = "lists the alphabetical sections for the books", urlPatterns = { "/book_sections", "/book_sections.xml", "/book_sections.html" })
public class ListBookSectionsController extends JanusController {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected JanusResponse janusAction(HttpServletRequest request,	HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//get all books
		Set<Book> books = DAOFactory.INSTANCE.getDAO(Book.class).get();
		
		//get section map
		Map<String,Section<Book>> sections = Section.generateAlphabeticalSections();

		Section<Book> other = new Section<Book>();
		other.setName("Starting with numbers or special characters");
		other.setId("OTHER");
		
		//begin putting books in given sections
		for(Book book : books) {
			String value = book.getSortTitle();
			String first = value.substring(0, 1);
			Section<Book> target = sections.get(first);
			if(target == null) {
				other.getContents().add(book);
			} else {
				target.getContents().add(book);
			}
		}
		
		//use book title comparator
		Comparator<Book> bookComparator = new BookTitleComparator();
		
		//sort all lists
		for(Section<Book> section : sections.values()) {
			Collections.sort(section.getContents(),bookComparator);
		}
		
		//create list of sections and sort, by string
		List<Section<Book>> bookSections = new ArrayList<Section<Book>>(sections.values());
		Collections.sort(bookSections);
		
		//if something had special characters, use the special character section to show what's going on
		if(other.getContents().size() > 0) {
			Collections.sort(other.getContents(),bookComparator);
			bookSections.add(0, other);
		}
		
		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		//put elements into template map
		elements.put("sections",bookSections);
		elements.put("mode",mode);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/book_sections.ftl");		
		
		JanusResponse janusResponse = new JanusResponse();
		
		return janusResponse;
	}

}
