package com.em.janus.dao.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.config.ServletConfigUtility;
import com.em.janus.dao.IDataAccessObject;
import com.em.janus.model.Book;
import com.em.janus.model.FileInfo;

public enum BookFilesAO {

	INSTANCE;
	
	private BookFilesAO() {
		
	}
	
	public List<FileInfo> getEbookFiles(ServletContext context, Book book) {
		List<FileInfo> results = new ArrayList<FileInfo>();

		//get configuration
		final JanusConfiguration config = ServletConfigUtility.getConfigurationFromContext(context);
		
		//get path
		File bookDir = FileAO.getBookDirectory(config, book);
		
		if(bookDir.exists() && bookDir.isDirectory()) {
		
			File[] ebooks = bookDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					//get acceptable extensions from configuration object
					List<String> extensions = config.getAllowedExtensions();
					
					String ext = name.substring(name.lastIndexOf(".")+1);
					
					return extensions.contains(ext);
				}
			});
			
			for(File file : ebooks) {
				FileInfo info = new FileInfo();
				info.setFile(file);
				String mimeType = context.getMimeType(file.getAbsolutePath());
				
				//fix for defective mime type detection based ONLY on extension. 
				//WARNING: could provide malicious files by extension
				if(mimeType == null || mimeType.isEmpty()) {
					String name = file.getName();
					String ext = name.substring(name.lastIndexOf(".")+1);
					if("lit".equals(ext) || name.endsWith("lit")) {
						mimeType = "application/x-ms-reader, application/x-obak";
					} else if("mobi".equals(ext) || name.endsWith("mobi")) {
						mimeType = "application/x-mobipocket-ebook";
					} else if("epub".equals(ext) || name.endsWith("epub")) {
						mimeType = "application/epub+zip";
					} else if("pdf".equals(ext) || name.endsWith("pdf")) {
						mimeType = "application/pdf";
					} else {
						mimeType = "binary/octet-stream";
					}
				}
				
				info.setMimeType(mimeType);
				info.setExtension(file.getName().substring(file.getName().lastIndexOf(".")+1));	
				
				//add info
				results.add(info);
			}
		}		
		
		return results;
	}
	
	public FileInfo getBookFile(ServletContext context, IDataAccessObject<Book> bookDAO, int bookId, final String extension) {
		
		FileInfo info = new FileInfo();
		info.setExtension(extension);
		
		final JanusConfiguration config = ServletConfigUtility.getConfigurationFromContext(context);
		
		//bail if the extension isn't in the extension list
		if(!config.getAllowedExtensions().contains(extension)) return info;
		
		//get book
		Set<Book> books = bookDAO.getByBookId(bookId);
		Book book = null;
		if(books != null && books.size() > 0) {
			book = books.iterator().next();
		}
		
		if(book != null) {
			//get path file, and if path exists... do the rest of our logic
			File coverPath = FileAO.getBookDirectory(config, book);
			
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
					
					String mimeType = context.getMimeType(info.getFile().getAbsolutePath());
					
					//fix mime type if container can't get it
					if(mimeType == null || mimeType.isEmpty()) {
						if("epub".equalsIgnoreCase(extension)) {
							mimeType = "application/epub+zip";
						} else if("mobi".equalsIgnoreCase(extension)) {
							mimeType = "application/x-mobipocket-ebook";
						} else if("lit".equalsIgnoreCase(extension)) {
							mimeType = " application/x-ms-reader";
						} else if("pdf".equalsIgnoreCase(extension)) {
							mimeType = "application/pdf";
						} else {
							//generic binary
							mimeType = "application/octet-stream";
						}
					}
							
					//get mime type
					info.setMimeType(mimeType);					
					
				}
				
			}		
		}
		
		return info;
	}
}
