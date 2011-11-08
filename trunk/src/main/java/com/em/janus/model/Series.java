package com.em.janus.model;

import java.util.Set;
import java.util.TreeSet;

public class Series extends Entity implements Comparable<Series>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String sortName;
	
	private Set<Book> books = new TreeSet<Book>();
	
	private Set<Tag> tags = new TreeSet<Tag>();
	
	private Set<Author> authors = new TreeSet<Author>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
	public int compareTo(Series o) {
		if(o == null) return 1;
		return this.getSortName().compareTo(o.getSortName());
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}		
}
