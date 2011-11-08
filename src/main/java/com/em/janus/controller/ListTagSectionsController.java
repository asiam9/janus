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
import com.em.janus.model.Tag;
import com.em.janus.model.sections.Section;
import com.em.janus.template.TemplateController;

/**
 * Servlet implementation class ListTagSections
 */
@WebServlet(description = "lists the alphabetical sections for the tags", urlPatterns = { "/tag_sections", "/tag_sections.xml", "/tag_sections.html" })
public class ListTagSectionsController extends JanusController {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void janusAction(HttpServletRequest request,	HttpServletResponse response, Writer out, String mode) throws ServletException, IOException {
		//sort mode (only name sorting is allowed)		
		String sort = "name";
		
		//get all authors
		Set<Tag> tags = DAOFactory.INSTANCE.getDAO(Tag.class).get();
		
		//get section map
		Map<String,Section<Tag>> sections = Section.generateAlphabeticalSections();
		
		Section<Tag> other = new Section<Tag>();
		other.setName("Tags with numbers or special characters");
		other.setId("OTHER");
		
		//begin putting authors in given sections
		for(Tag t : tags) {
			String value = t.getName();
			String first = value.substring(0, 1);
			Section<Tag> target = sections.get(first);
			if(target == null) {
				other.getContents().add(t);
			} else {
				target.getContents().add(t);
			}
		}
		
		//sort all lists
		for(Section<Tag> section : sections.values()) {
			Collections.sort(section.getContents());
		}
		
		//create list of sections and sort, by string
		List<Section<Tag>> orderedSections = new ArrayList<Section<Tag>>(sections.values());
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
		TemplateController.INSTANCE.process(out, elements, "xml/tag_sections.ftl");		
	}

}
