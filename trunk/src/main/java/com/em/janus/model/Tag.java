package com.em.janus.model;

import java.util.Set;
import java.util.TreeSet;


public class Tag extends Entity implements Comparable<Tag> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Set<Book> books = new TreeSet<Book>();
	
	private Set<Tag> subTags = new TreeSet<Tag>();
	
	private Set<Author> authors = new TreeSet<Author>();
	
	private Set<Series> series = new TreeSet<Series>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Tag o) {
		if(o == null) return 1;
		return this.getName().compareTo(o.getName());
	}	
	
	public Set<Series> getSeries() {
		return series;
	}

	public void setSeries(Set<Series> series) {
		this.series = series;
	}
	
	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public Set<Tag> getSubTags() {
		return subTags;
	}

	public void setSubTags(Set<Tag> subTags) {
		this.subTags = subTags;
	}	
}
