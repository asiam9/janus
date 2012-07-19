package com.em.janus.watcher;

import java.io.File;

import org.slf4j.Logger;

import com.em.janus.dao.CacheManager;

public class DatabaseWatcher implements Runnable {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private boolean keepRunning = true;
	
	private String path = null;
	
	public DatabaseWatcher(String path) {
		this.logger.info("Created metadata database watcher runnable.");
		this.path = path;
	}
	
	@Override
	public void run() {
		
		//log
		this.logger.info("Watcher thread started.");

		//get path to database file
		String path = this.path;
		
		//get file object for database
		File database = new File(path);
		
		long size = 0; 
		long modified = 0;
		
		//initialize if database exists
		if(database.exists()) {
			//found database
			this.logger.info("Found database at path='{}'", this.path);
			
			size = database.length();
			modified = database.lastModified();
			
			//init the cache for first run
			CacheManager.INSTANCE.init();
		} else {
			//did not find database
			this.logger.error("Could not find database at path='{}'", this.path);
		}
		
		//loop until and unless thread is interrupted
		while(this.keepRunning) {

			//keep waiting for the database if it does not exist
			if(database.exists()) {
				//get new values
				long newSize = database.length();
				long newModified = database.lastModified();
				
				//if the values have changed update them and send a message to the database/cache management bits
				if(size != newSize || modified != newModified) {
					//update values
					size = newSize;
					modified = newModified;
					
					//send message
					this.logger.info("Database file \"{}\" has changed!",path);
					CacheManager.INSTANCE.evict();
					
					//re-initialize cache
					CacheManager.INSTANCE.init();
				}
			}
			
			//sleep for 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				keepRunning = false;
			}
		}
		
	}
	
	
	public void stop() {
		CacheManager.INSTANCE.unregisterAll();
		this.keepRunning = false;
	}
}
