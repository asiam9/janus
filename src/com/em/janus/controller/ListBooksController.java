package com.em.janus.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.em.janus.model.sections.Section;
import com.em.janus.model.sorting.BookRecentlyAddedComparator;
import com.em.janus.model.sorting.BookTagsComparator;
import com.em.janus.model.sorting.BookTitleComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListBooksController
 */
@WebServlet(description = "Controller that provides the list of books", urlPatterns = { "/books", "/books.xml", "/books.html" })
public class ListBooksController extends JanusController {
	private static final long serialVersionUID = 1L;
  
	@Override
	protected void janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode
		String sort = request.getParameter("sort");
		if(sort == null || sort.isEmpty()) sort = "title";
		
		//get index and size parameters
		String sizeString = request.getParameter("size");
		String indexString = request.getParameter("index");
		
		//get starts with
		String startsWith = request.getParameter("starts");
		if(startsWith != null && startsWith.isEmpty()) startsWith = null;
		
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

		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		//get books
		Set<Book> books = null;
		if(startsWith == null) {
			books = DAOFactory.INSTANCE.getDAO(Book.class).get();
		} else if("OTHER".equalsIgnoreCase(startsWith)) {
			books = DAOFactory.INSTANCE.getDAO(Book.class).get();

			//sort out again, looking for "others" with same method that the controller used.
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
			
			//populate books with "other" before continuing
			books = new HashSet<Book>(other.getContents());
		} else {
			books = DAOFactory.INSTANCE.getDAO(Book.class).queryStartsWith("sort", startsWith);
		}
		List<Book> sorted = new ArrayList<Book>(books);
		
		Comparator<Book> bookComparator = new BookTitleComparator();
		if("date".equals(sort) || "recent".equals(sort)) {
			bookComparator = new BookRecentlyAddedComparator();
		} else if("tags".equals(sort)) {
			//populate tags
			for(Book book : sorted) {
				book.setTags(DAOFactory.INSTANCE.getDAO(Tag.class).getByBookId(book.getId()));
			}
			//set sorter for tags
			bookComparator = new BookTagsComparator();
		}
		//sort using the comparator selected from input options
		Collections.sort(sorted,bookComparator);
		
		//get max end size for books list
		int end = index + size;
		int nextIndex = index;
		if(end > sorted.size()) {
			end = sorted.size();
		} else {
			nextIndex += size;
		}
		
		//chop the book list to size when in any XML format
		if(!"json".equals(mode)) {
			//chop the list
			sorted = sorted.subList(index, end);
		}
		
		//update size with previously calculated (in bounds) next index
		index = nextIndex;
		
		//details on the chosen items
		for(Book b : sorted) {
			b.setAuthors(DAOFactory.INSTANCE.getDAO(Author.class).getByBookId(b.getId()));
			b.setSeries(DAOFactory.INSTANCE.getDAO(Series.class).getByBookId(b.getId()));
		}		

		//list
		elements.put("books", sorted);
		
		//add mode to knowledge
		elements.put("mode", mode);

		//index and size
		elements.put("index", index);
		elements.put("size", size);
		
		//sorting
		elements.put("sort",sort);
		
		//starts with
		elements.put("starts",startsWith);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/books.ftl");		
	}

}
