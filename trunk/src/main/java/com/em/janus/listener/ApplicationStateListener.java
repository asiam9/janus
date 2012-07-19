package com.em.janus.listener;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.config.ServletConfigUtility;
import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.cache.EhCacheManager;
import com.em.janus.watcher.DatabaseWatcher;

/**
 * Application Lifecycle Listener implementation class StartupDatabaseWatcher
 *
 */
@WebListener
public class ApplicationStateListener implements ServletContextListener {

	private static String JANUS_CONFIG_PROPERTY = "com.em.janus.configFile";
	
	private Logger log = LoggerFactory.getLogger(ApplicationStateListener.class);
	
	@Resource(name="com.em.janus.configFile", mappedName="com.em.janus.configFile")
	private String injectedConfigFile;
	
	private DatabaseWatcher watcher = null;
	private Thread watcherThread = null;
		
    /**
     * Default constructor. 
     */
    public ApplicationStateListener() {

    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	//look for config location in the following order
    	//system property
    	//injected resource
    	//raw jndi
    	
    	String configFileLocation = null;

		String systemProperty = System.getProperty(ApplicationStateListener.JANUS_CONFIG_PROPERTY);
		if(systemProperty != null && !systemProperty.isEmpty() && !"null".equalsIgnoreCase(systemProperty)) {
			configFileLocation = systemProperty;
			this.log.info("Resolved config file from system property: '{}'", systemProperty);
		}
   	
    	if((configFileLocation == null || configFileLocation.isEmpty()) && this.injectedConfigFile != null && !this.injectedConfigFile.isEmpty()) {
    		configFileLocation = this.injectedConfigFile;
    		this.log.info("Resolved injected config file: '{}'", this.injectedConfigFile);
    	}
    	
    	//try jndi, won't work on some servers, some situations
    	if(configFileLocation == null) {
            //load environment config by jndi
            Context initCtx;
            //use context to look up config file path through jndi (from web.xml)
            try {
                initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                configFileLocation = (String)envCtx.lookup("configFile");
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if(configFileLocation == null) {
            	this.log.warn("Could not resolve config file location, using default '/var/janus.conf'");
            } else {
            	this.log.info("Resolved config file from JNDI: '{}'", configFileLocation);
            }
    	}    	
    	
    	//get configuration
    	JanusConfiguration config = null;
    	if(configFileLocation == null) {
    		config = new JanusConfiguration();
    	} else {
        	config = new JanusConfiguration(configFileLocation);    		
    	}
    	
    	//save configuration to DAOFactory
    	DAOFactory.INSTANCE.setConfig(config);
    	
    	//save configuration to context
    	ServletConfigUtility.saveConfigurationInContext(arg0.getServletContext(), config);
    	
    	//create the watcher runnable and build a thread from that
    	this.watcher = new DatabaseWatcher(config.getDatabasePath());
    	this.watcherThread = new Thread(this.watcher);

    	
    	//if the watcher thread isn't alive, start it
    	if(!this.watcherThread.isAlive()) {
    		this.watcherThread.start();
    	}    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	if(this.watcher != null) {
    		//stop
	    	this.watcher.stop();
	
	    	//wait for watcher thread to stop
	    	try {
				this.watcherThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
		
		//close the query cache
   		EhCacheManager.close();
    }
	
}
