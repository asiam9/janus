package com.em.janus.model;

import java.util.HashSet;
import java.util.Set;

public class Author extends Entity implements Comparable<Author>{

	private String name;
	
	private String sortName;
	
	private Set<Book> books = new HashSet<Book>();
	
	private Set<Series> series = new HashSet<Series>();
	
	private Set<Tag> tags = new HashSet<Tag>();

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

	public Set<Series> getSeries() {
		return series;
	}

	public void setSeries(Set<Series> series) {
		this.series = series;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int compareTo(Author o) {
		if(o == null) return 1;
		return this.getSortName().compareTo(o.getSortName());
	}
	
}
