package com.em.janus.dao.filesystem;

import java.io.File;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.model.Book;

public class FileAO {

	public static File getBookDirectory(JanusConfiguration config, Book book) {
		
		String path = config.getBasePath() + File.separatorChar + book.getPathToFile();
		
		File originalFile = new File(path);
		File parentFile = null;
		File bookDir = null;
		if(!originalFile.exists()) {
			InsensitiveFileFilter filter = new InsensitiveFileFilter(originalFile);
			parentFile = originalFile.getParentFile();
			File[] files = parentFile.listFiles(filter);
			if(files != null && files.length > 0) {
				bookDir = files[0];
			} else {
				bookDir = originalFile;
			}
		} else {
			bookDir = originalFile;
		}
		
		return bookDir;
	}
	
}
