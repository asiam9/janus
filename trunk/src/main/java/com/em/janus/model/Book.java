package com.em.janus.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Book extends Entity implements Comparable<Book>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String sortTitle;
	
	private Date timestamp;
	
	private Date pubdate;
	
	private double seriesIndex = 0.0; 
	
	private String authorSortName;
	
	private Set<Author> authors = new HashSet<Author>(); 
	
	private String ISBN;
	
	private String LCCN;
	
	private String pathToFile;
	
	private String UUID;
	
	private boolean hasCover;
	
	private Date lastModified;
	
	private Set<Tag> tags = new HashSet<Tag>();
	
	private Set<Series> series = new HashSet<Series>();
	
	private Set<FileInfo> fileInfo = new HashSet<FileInfo>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSortTitle() {
		return sortTitle;
	}

	public void setSortTitle(String sortTitle) {
		this.sortTitle = sortTitle;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getPubdate() {
		return pubdate;
	}

	public void setPubdate(Date pubdate) {
		this.pubdate = pubdate;
	}

	public String getAuthorSortName() {
		return authorSortName;
	}

	public void setAuthorSortName(String authorSortName) {
		this.authorSortName = authorSortName;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getLCCN() {
		return LCCN;
	}

	public void setLCCN(String lCCN) {
		LCCN = lCCN;
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public boolean isHasCover() {
		return hasCover;
	}

	public void setHasCover(boolean hasCover) {
		this.hasCover = hasCover;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Series> getSeries() {
		return series;
	}

	public void setSeries(Set<Series> series) {
		this.series = series;
	}

	public double getSeriesIndex() {
		return seriesIndex;
	}

	public void setSeriesIndex(double seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

	@Override
	public int compareTo(Book o) {
		if(o == null) return 1;
		Double thisIndex = this.seriesIndex;
		Double thatIndex = o.seriesIndex;
		
		if(thisIndex.compareTo(thatIndex) == 0) {
			if(this.getSortTitle().compareTo(o.getSortTitle()) == 0) {
				
				Integer thisId = this.getId();
				Integer thatId = o.getId();
				
				return thisId.compareTo(thatId);
			}
			
			return this.getSortTitle().compareTo(o.getSortTitle());
		}		
		
		return thisIndex.compareTo(thatIndex);
	}

	public Set<FileInfo> getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(Set<FileInfo> fileInfo) {
		this.fileInfo = fileInfo;
	}
	
}
