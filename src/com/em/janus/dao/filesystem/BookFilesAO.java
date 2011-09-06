package com.em.janus.dao.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.dao.calibre.BookDAO;
import com.em.janus.model.Book;
import com.em.janus.model.FileInfo;

public enum BookFilesAO {

	INSTANCE;
	
	private BookFilesAO() {
		
	}
	
	public List<FileInfo> getEbookFiles(ServletContext context, Book book) {
		List<FileInfo> results = new ArrayList<FileInfo>();

		//get path
		File bookDir = FileAO.getBookDirectory(book);
		
		if(bookDir.exists() && bookDir.isDirectory()) {
		
			File[] ebooks = bookDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					//get acceptable extensions from configuration object
					List<String> extensions = JanusConfiguration.INSTANCE.getAllowedExtensions();
					
					String ext = name.substring(name.lastIndexOf(".")+1);
					
					return extensions.contains(ext);
				}
			});
			
			for(File file : ebooks) {
				FileInfo info = new FileInfo();
				info.setFile(file);
				info.setMimeType(context.getMimeType(file.getAbsolutePath()));
				info.setExtension(file.getName().substring(file.getName().lastIndexOf(".")+1));	
				
				//add info
				results.add(info);
			}
		}		
		
		return results;
	}
	
	public FileInfo getBookFile(ServletContext context, int bookId, final String extension) {
		
		FileInfo info = new FileInfo();
		info.setExtension(extension);
		
		//bail if the extension isn't in the extension list
		if(!JanusConfiguration.INSTANCE.getAllowedExtensions().contains(extension)) return info;
		
		//get book
		Set<Book> books = BookDAO.INSTANCE.getByBookId(bookId);
		Book book = null;
		if(books != null && books.size() > 0) {
			book = books.iterator().next();
		}
		
		if(book != null) {
			//get path file, and if path exists... do the rest of our logic
			File coverPath = FileAO.getBookDirectory(book);
			
			if(coverPath.exists() && coverPath.isDirectory()) {
				File[] ebooks = coverPath.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(extension);
					}
				});
				
				//first instance becomes file to return
				if(ebooks.length > 0) {
					//set file
					info.setFile(ebooks[0]);
					
					//get mime type
					info.setMimeType(context.getMimeType(info.getFile().getAbsolutePath()));
				}
				
			}		
		}
		
		return info;
	}
}
