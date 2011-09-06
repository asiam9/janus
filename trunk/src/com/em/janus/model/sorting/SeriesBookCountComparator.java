package com.em.janus.model.sorting;

import java.util.Comparator;

import com.em.janus.model.Series;

public class SeriesBookCountComparator implements Comparator<Series> {
	
	@Override
	public int compare(Series series1, Series series2) {

		if(series1 == null && series2 == null) return 0;
		if(series1 == null) return 1;
		if(series2 == null) return -1;
		
		if(series1.equals(series2)) return 0;
		
		Integer books1 = series1.getBooks().size();
		Integer books2 = series2.getBooks().size();
		
		if(books1 == null && books2 == null) return 0;
		if(books2 == null) return 1;
		if(books1 == null) return -1;
		
		//fall back to comparing this way
		if(books2.compareTo(books1) == 0) {
			return series1.getName().compareTo(series2.getName());
		}
		
		return books2.compareTo(books1);
	}

}
