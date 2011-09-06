package com.em.janus.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public enum TemplateController {
	
	INSTANCE;
	
	private Configuration configuration = null;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private TemplateController() {
		
		this.configuration = new Configuration();
		this.configuration.setClassForTemplateLoading(this.getClass(), "");
		//this.configuration.setObjectWrapper(new DefaultObjectWrapper());		
	}
	
	public void process(Writer out, Map<String,Object> objectMap, String templatePath) {
		try {
			Template template = this.configuration.getTemplate(templatePath);
			
			Environment env = template.createProcessingEnvironment(objectMap, out);
			
			env.setOutputEncoding("UTF8");
			
			env.process();
		} catch (IOException e) {
			logger.error("IOerror with template mechanism for {}.", templatePath, e);
		} catch (TemplateException e) {
			logger.error("Error with template mechanism for {}.", templatePath, e);
		}		
	}

}
