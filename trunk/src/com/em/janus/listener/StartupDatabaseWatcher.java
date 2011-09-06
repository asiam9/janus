package com.em.janus.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.em.janus.watcher.DatabaseWatcher;

/**
 * Application Lifecycle Listener implementation class StartupDatabaseWatcher
 *
 */
@WebListener
public class StartupDatabaseWatcher implements ServletContextListener {

	private Thread watcherThread = null;
	
    /**
     * Default constructor. 
     */
    public StartupDatabaseWatcher() {
    	//get the watcher thread
    	this.watcherThread = new Thread(DatabaseWatcher.INSTANCE);
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	//if the watcher thread isn't alive, start it
    	if(!this.watcherThread.isAlive()) {
    		this.watcherThread.start();
    	}    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	DatabaseWatcher.INSTANCE.stop();

    	//wait for watcher thread to stop
    	try {
			this.watcherThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
}
