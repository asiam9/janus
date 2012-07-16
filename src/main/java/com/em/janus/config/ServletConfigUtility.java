package com.em.janus.config;

import javax.servlet.ServletContext;

import org.slf4j.Logger;

public class ServletConfigUtility {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(ServletConfigUtility.class);
	
	private ServletConfigUtility() {
		
	}
	
	public static String SERVLET_CONTEXT_CONFIGURATION_KEY = "com.em.janus.config.JanusConfig-servlet-key";
	
	public static JanusConfiguration getConfigurationFromContext(ServletContext context) {
		JanusConfiguration config = null;
		try {
			config = (JanusConfiguration)context.getAttribute(ServletConfigUtility.SERVLET_CONTEXT_CONFIGURATION_KEY);
			ServletConfigUtility.logger.debug("Got the configuration from the servlet context");
		} catch (Exception ex) {
			ServletConfigUtility.logger.error("An error occurred while getting the configuration from the servlet context.");
			ex.printStackTrace();
			return null;
		}		
		return config;
	}
	
	public static void saveConfigurationInContext(ServletContext context, JanusConfiguration config) {
		context.setAttribute(ServletConfigUtility.SERVLET_CONTEXT_CONFIGURATION_KEY, config);
		ServletConfigUtility.logger.info("Configuration saved to: {}", ServletConfigUtility.SERVLET_CONTEXT_CONFIGURATION_KEY);
	}
	
}
