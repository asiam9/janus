package com.em.janus.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.sqlite.SQLiteConfig;

import com.em.janus.config.JanusConfiguration;

public enum ConnectionManager {
	
	INSTANCE;

	private String dbPath = JanusConfiguration.INSTANCE.getDatabasePath();
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private ConnectionManager() {
		String sqlDriverName = "org.sqlite.JDBC";
		
		//load the driver
		try {
			Class.forName(sqlDriverName);			
			logger.debug("Loaded \"{}\" driver.",sqlDriverName);
		} catch (Exception ex) {
			logger.error("Could not load the \"{}\" driver.",sqlDriverName);		
		}
	}
	
	public Connection getConnection() {
		Connection connection = null;

		String connectionURI = "jdbc:sqlite:" + this.dbPath;

		//create config
		SQLiteConfig config = new SQLiteConfig();
		//read only, we do not EVER want to corrupt the database by hook or crook, sql injection or otherwise
		config.setReadOnly(true);
		//shared cache
		config.setSharedCache(true);
		
		try {
			connection = DriverManager.getConnection(connectionURI, config.toProperties());
			this.logger.debug("Retrieved connection from connection pool.");
		} catch (SQLException e) {
			this.logger.error("Could not get conncetion from connection pool.",e);			
		}
		
		return connection;
	}
	
}
