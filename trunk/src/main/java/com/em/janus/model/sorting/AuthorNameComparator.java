package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Author;

public class AuthorNameComparator implements Comparator<Author> {

	@Override
	public int compare(Author arg0, Author arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
	
}
