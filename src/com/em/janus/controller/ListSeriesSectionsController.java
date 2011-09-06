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
import com.em.janus.model.Series;
import com.em.janus.model.sections.Section;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListAuthorSections
 */
@WebServlet(description = "lists the alphabetical sections for the series", urlPatterns = { "/series_sections", "/series_sections.xml", "/series_sections.html" })
public class ListSeriesSectionsController extends JanusController {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void janusAction(HttpServletRequest request,	HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode (only name sorting is allowed)
		
		String sort = "name";
		
		//get all authors
		Set<Series> series = DAOFactory.INSTANCE.getDAO(Series.class).get();
		
		//get section map
		Map<String,Section<Series>> sections = Section.generateAlphabeticalSections();
		
		Section<Series> other = new Section<Series>();
		other.setName("Starting with numbers or special characters");
		other.setId("OTHER");
		
		//begin putting authors in given sections
		for(Series s : series) {
			String value = s.getSortName();
			String first = value.substring(0, 1);
			Section<Series> target = sections.get(first);
			if(target == null) {
				other.getContents().add(s);
			} else {
				target.getContents().add(s);
			}
		}
		
		//sort all lists
		for(Section<Series> section : sections.values()) {
			Collections.sort(section.getContents());
		}
		
		//create list of sections and sort, by string
		List<Section<Series>> orderedSections = new ArrayList<Section<Series>>(sections.values());
		Collections.sort(orderedSections);
		
		//only add "other" if it has contents, add it to the head of the list
		if(other.getContents().size() > 0) {
			Collections.sort(other.getContents());
			orderedSections.add(0,other);
		}
		
		//template object map
		Map<String, Object> elements = new HashMap<String, Object>();
		
		//put elements into template map
		elements.put("sections",orderedSections);
		elements.put("mode",mode);
		elements.put("sort",sort);
		
		//process template into output stream
		TemplateController.INSTANCE.process(out, elements, "xml/series_sections.ftl");		
	}

}
