package com.em.janus.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;

public enum JanusConfiguration {

	INSTANCE;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private String basePath = "/var/books";
	private String database = "metadata.db";
	private List<String> ebookExtensions = new ArrayList<String>();
	
	private boolean useEhCache = true;

	private int pageSize = 50;
	
	private JanusConfiguration() {
		//load environment config by jndi
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			String basePath = (String)envCtx.lookup("basePath");
			String database = (String)envCtx.lookup("databaseName");
			
			if(basePath != null && !basePath.isEmpty()) this.basePath = basePath;
			if(database != null && !database.isEmpty()) this.database = database;		
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//build extensions by hand
		this.ebookExtensions.add("epub");
		this.ebookExtensions.add("pdf");
		this.ebookExtensions.add("lit");
		this.ebookExtensions.add("mobi");		
		
		//log
		this.logger.info("Base path: {}",this.basePath);
		this.logger.info("Database: {}",this.database);
	}
	
	public String getBasePath() {
		return this.basePath;
	}
	
	public String getDatabasePath() {
		return this.basePath + File.separatorChar + this.database;
	}
	
	public int getPageSize() {
		return this.pageSize;
	}

	public List<String> getAllowedExtensions() {
		return ebookExtensions;
	}
	
	public boolean useEhCache() {
		return this.useEhCache;
	}
}
