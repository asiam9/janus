package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Book;

public class BookTagsComparator implements Comparator<Book> {
	
	private static Comparator<Book> titleCompare = new BookTitleComparator();

	@Override
	public int compare(Book book1, Book book2) {

		if(book1 == null && book2 == null) return 0;
		if(book1 == null) return 1;
		if(book2 == null) return -1;
		
		if(book1.equals(book2)) return 0;
		
		Integer tags1 = book1.getTags().size();
		Integer tags2 = book2.getTags().size();
		
		if(tags1 == null && tags2 == null) return 0;
		if(tags1 == null) return 1;
		if(tags2 == null) return -1;
		
		//fall back to comparing this way
		if(tags1.compareTo(tags2) == 0) {
			return titleCompare.compare(book1, book2);
		}
		
		return tags1.compareTo(tags2);
	}

}
