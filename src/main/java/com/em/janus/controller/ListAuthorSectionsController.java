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
import com.em.janus.model.Author;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.model.sections.Section;
import com.em.janus.model.sorting.AuthorNameComparator;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListAuthorSections
 */
@WebServlet(description = "lists the alphabetical sections for the authors", urlPatterns = { "/author_sections", "/author_sections.xml", "/author_sections.html" })
public class ListAuthorSectionsController extends JanusController {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected JanusResponse janusAction(HttpServletRequest request,	HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode
		String sort = request.getParameter("sort");
		if(sort == null || sort.isEmpty()) sort = "name";
		
		//get all authors
		Set<Author> authors = DAOFactory.INSTANCE.getDAO(Author.class).get();
		
		//get section map
		Map<String,Section<Author>> sections = Section.generateAlphabeticalSections();
		
		Section<Author> other = new Section<Author>();
		other.setName("Starting with numbers or special characters");
		other.setId("OTHER");
		
		//begin putting authors in given sections
		for(Author author : authors) {
			String value = author.getName();
			if("sort".equals(sort)) {
				value = author.getSortName();
			} 
			String first = value.substring(0, 1);
			Section<Author> target = sections.get(first);
			if(target != null) {
				target.getContents().add(author);
			} else {
				other.getContents().add(author);
			}
		}
		
		//comparator
		Comparator<Author> comparator = new AuthorNameComparator();
		
		//sort all lists
		for(Section<Author> section : sections.values()) {
			if("sort".equals(sort)) {
				Collections.sort(section.getContents());
			} else {
				Collections.sort(section.getContents(), comparator);
			}
		}
		
		//create list of sections and sort, by string
		List<Section<Author>> authorSections = new ArrayList<Section<Author>>(sections.values());
		Collections.sort(authorSections);

		//add other section if it has contents
		if(other.getContents().size() > 0) {
			if("sort".equalsIgnoreCase(sort)) {
				Collections.sort(other.getContents());
			} else {
				Collections.sort(other.getContents(), comparator);
			}
			Collections.sort(other.getContents());
			authorSections.add(0,other);
		}
		
		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		//put elements into template map
		elements.put("sections",authorSections);
		elements.put("mode",mode);
		elements.put("sort",sort);
		
		//create response
		JanusResponse janusResponse = new JanusResponse();
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/author_sections.ftl");
		
		return janusResponse;
	}

}
