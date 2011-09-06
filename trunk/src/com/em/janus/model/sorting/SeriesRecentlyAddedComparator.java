package com.em.janus.model.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.em.janus.model.Book;
import com.em.janus.model.Series;

public class SeriesRecentlyAddedComparator implements Comparator<Series> {
	
	@Override
	public int compare(Series series1, Series series2) {

		if(series1 == null && series2 == null) return 0;
		if(series1 == null) return 1;
		if(series2 == null) return -1;
		
		if(series1.equals(series2)) return 0;
		
		List<Book> books1 = new ArrayList<Book>(series1.getBooks());
		List<Book> books2 = new ArrayList<Book>(series2.getBooks());
		
		if(books1.isEmpty() && books2.isEmpty()) return series1.compareTo(series2);
		if(books1.isEmpty()) return 1;
		if(books2.isEmpty()) return -1;
		
		Collections.sort(books1,new BookRecentlyAddedComparator());
		Collections.sort(books2,new BookRecentlyAddedComparator());
	
		Book book1 = books1.get(0);
		Book book2 = books2.get(0);
		
		int result = (new BookRecentlyAddedComparator()).compare(book1, book2);
		
		if(result == 0) {
			return series1.compareTo(series2);
		}
		
		return result;
	}

}
