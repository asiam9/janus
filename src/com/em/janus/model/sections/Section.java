package com.em.janus.model.sections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.em.janus.model.Entity;

public class Section<T extends Entity> implements Comparable<Section<T>>{

	private String name = "";
	
	private List<T> contents = new ArrayList<T>();
	
	private String id = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<T> getContents() {
		return contents;
	}

	public void setContents(List<T> contents) {
		this.contents = contents;
	}  
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int compareTo(Section<T> o) {
		return this.getName().compareTo(o.getName());
	}
	
	public static <T extends Entity> Map<String,Section<T>> generateAlphabeticalSections() {
		
		Map<String,Section<T>> sections = new HashMap<String, Section<T>>();
		
		//cheap and dirty, probably won't work right in other locales, etc
		for(int i = (int)'A'; i <= (int)'Z'; i++) {
			String letter = ""+((char)i);
			Section<T> section = new Section<T>();
			section.setName("Starting with " + letter);
			section.setId(letter);
			sections.put(letter, section);
		}
	
		return sections;
	}

}
