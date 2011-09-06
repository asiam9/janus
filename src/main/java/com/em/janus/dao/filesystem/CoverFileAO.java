package com.em.janus.dao.filesystem;

import java.io.File;
import java.io.FileFilter;

import com.em.janus.model.Book;

public enum CoverFileAO {
	
	INSTANCE;
	
	private CoverFileAO() {
		
	}
	
	public File getCoverImage(Book book) {
		
		File file = null;
	
		File bookDir = FileAO.getBookDirectory(book);
		
		//find cover file
		String coverPath = bookDir.getAbsolutePath() + File.separatorChar + "cover.jpg";
		
		//get the cover file
		file = new File(coverPath);
		
		//if the cover file does not exist
		if(!file.exists()) {
			file = null;
			
			if(bookDir.exists() && bookDir.isDirectory()) {
				File[] files = bookDir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return pathname.getAbsolutePath().endsWith("jpg");
					}
				});
				
				if(files != null && files.length > 0) {
					file = files[0];
				}
			} 
		}		
		
		return file;
	}
	
}
