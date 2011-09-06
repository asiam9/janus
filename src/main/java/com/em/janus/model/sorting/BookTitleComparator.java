package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Book;

public class BookTitleComparator implements Comparator<Book>{

	@Override
	public int compare(Book book1, Book book2) {
		
		if(book1 == null && book2 == null) return 0;
		if(book1 == null) return 1;
		if(book2 == null) return -1;
		
		if(book1.equals(book2)) return 0;
		
		String title1 = book1.getSortTitle();
		String title2 = book2.getSortTitle();
		
		if(title1 == null && title2 == null) return 0;
		if(title1 == null) return 1;
		if(title2 == null) return -1;
		
		return title1.compareTo(title2);
	}

}
