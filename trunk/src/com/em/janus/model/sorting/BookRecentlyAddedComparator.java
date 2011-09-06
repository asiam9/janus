package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Book;

public class BookRecentlyAddedComparator implements Comparator<Book> {
	
	private static Comparator<Book> titleCompare = new BookTitleComparator();

	@Override
	public int compare(Book book1, Book book2) {

		if(book1 == null && book2 == null) return 0;
		if(book1 == null) return 1;
		if(book2 == null) return -1;
		
		if(book1.equals(book2)) return 0;
		
		/*
		Date date1 = book1.getTimestamp();
		Date date2 = book2.getTimestamp();
		
		if(date1 == null && date2 == null) return 0;
		if(date1 == null) return -1;
		if(date2 == null) return 1;
		
		
		//fall back to comparing this way if two dates tie
		if(date1.compareTo(date2) == 0) {
			return titleCompare.compare(book1, book2);
		}
		
		return date2.compareTo(date1);
		*/
		
		Integer id1 = book1.getId();
		Integer id2 = book2.getId();

		if(id1 == null && id2 == null) return 0;
		if(id1 == null) return -1;
		if(id2 == null) return 1;
		
		
		//fall back to comparing this way if two dates tie
		if(id1.compareTo(id2) == 0) {
			return titleCompare.compare(book1, book2);
		}
		
		return id2.compareTo(id1);
	}

}
