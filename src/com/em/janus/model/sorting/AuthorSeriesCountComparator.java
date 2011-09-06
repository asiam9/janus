package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Author;

public class AuthorSeriesCountComparator implements Comparator<Author> {

	@Override
	public int compare(Author arg0, Author arg1) {
		Integer count0 = arg0.getSeries().size();
		Integer count1 = arg1.getSeries().size();
		
		return count1.compareTo(count0);		
	}
	
}
