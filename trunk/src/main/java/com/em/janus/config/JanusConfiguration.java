package com.em.janus.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;

public enum JanusConfiguration {

	INSTANCE;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private String configFilePath = null;
	
	//calibre data
	private String basePath = "/var/books";
	private String database = "metadata.db";
	private List<String> ebookExtensions = new ArrayList<String>();
	
	//cache mechanism
	private boolean useEhCache = true;

	//page size
	private int pageSize = 50;
	
	//email
	private String smtp = "smtp.mail.com";
	private String from = "janus@books.com";
	private int port = 25;
	private String security = "none";
	private String emailUser = "";
	private String emailPassword = "";
	
	private JanusConfiguration() {
		//load environment config by jndi
		Context initCtx;
		//use context to look up config file path through jndi
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			this.configFilePath = (String)envCtx.lookup("configFile");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//need a config file
		if(this.configFilePath == null || this.configFilePath.isEmpty()) {
			this.configFilePath = "/var/janus.conf";
		}
		File configFile = new File(this.configFilePath);
		
		//load properties
		Properties props = new Properties();
		if(configFile != null && configFile.exists() && !configFile.isDirectory()) {
			InputStream configStream;
			try {
				configStream = new FileInputStream(configFile);
				props.load(configStream);
				this.logger.info("Loaded config file: {}", configFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.logger.error("Config file '{}' not found.", this.configFilePath);
		}
		
		//load values, using defaults
		this.basePath = props.getProperty("basePath",this.basePath);
		this.database = props.getProperty("database", this.database);
		this.smtp = props.getProperty("email.smtp",this.smtp);
		try {
			this.port = Integer.parseInt((props.getProperty("email.port",Integer.toString(this.port))));
		} catch (NumberFormatException nfe) {
			this.port = 25;
		}
		this.emailUser = props.getProperty("email.user",this.emailUser);
		this.emailPassword = props.getProperty("email.password",this.emailPassword);
		this.from = props.getProperty("email.from",this.from);
		this.security = props.getProperty("email.security",this.security);
		
		//build extensions by hand
		this.ebookExtensions.add("epub");
		this.ebookExtensions.add("pdf");
		this.ebookExtensions.add("lit");
		this.ebookExtensions.add("mobi");		
		
		//log
		this.logger.info("Base path: {}",this.basePath);
		this.logger.info("Database: {}",this.database);
		this.logger.info("Email SMTP: {}",this.smtp);
		this.logger.info("Email PORT: {}",this.port);
		this.logger.info("Email USER: {}",this.emailUser);
		this.logger.info("Email FROM: {}",this.from);
		this.logger.info("Email SECURITY: {}",this.security);
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

	public String getSmtp() {
		return smtp;
	}

	public int getPort() {
		return port;
	}

	public String getSecurity() {
		return security;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public String getEmailPassword() {
		return emailPassword;
	}	
	
	public String getEmailFrom() {
		return from;
	}
}
