package com.em.janus.dao.filesystem;

import java.io.File;
import java.io.FileFilter;

public class InsensitiveFileFilter implements FileFilter {

	private File likeFile = null;
	
	private InsensitiveFileFilter() {
		
	}
	
	public InsensitiveFileFilter(File likeFile) {
		this();
		
		this.likeFile = likeFile;
	}
	
	@Override
	public boolean accept(File pathname) {
		return pathname.getAbsolutePath().equalsIgnoreCase(this.likeFile.getAbsolutePath());
	}

}
