package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Author;

public class AuthorTagCountComparator implements Comparator<Author> {

	@Override
	public int compare(Author arg0, Author arg1) {
		Integer count0 = arg0.getTags().size();
		Integer count1 = arg1.getTags().size();
		
		return count1.compareTo(count0);		
	}
	
}
