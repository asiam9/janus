package com.em.janus.controller;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.em.janus.model.output.OutputType;
import com.em.janus.model.response.JanusResponse;
import com.em.janus.template.TemplateController;

import freemarker.ext.dom.NodeModel;

public abstract class JanusController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PREVIOUS_PATH_KEY = "com.em.janus.PreviousPath.key";
	
	private static final String PREVIOUS_QUERY_KEY = "com.em.janus.PreviousQuery.key";
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	public JanusController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		this.doAction(req, resp);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//set up
		long start = (new Date()).getTime();
		
		//get user agent
		String userAgent = request.getHeader("user-agent");
		if(userAgent == null || userAgent.isEmpty()) {
			userAgent = "generic";
		}
		userAgent = userAgent.toLowerCase();
		
		//get previous values
		String previousPath = (String)request.getSession().getAttribute(JanusController.PREVIOUS_PATH_KEY);
		String previousQuery = (String)request.getSession().getAttribute(JanusController.PREVIOUS_QUERY_KEY);		
		
		//get sort
		String sort = request.getParameter("sort");
	
		//create mode string
		String mode = request.getParameter("mode");
		
		//get the local name, provides type sensative naming
		String path = request.getServletPath();
		if(path == null) path = "";
		path = path.toLowerCase();
		
		//get the query
		String query = request.getQueryString();
		if(query == null) query = "";
		query = query.toLowerCase();
		
		//set default output type
		OutputType outputType = OutputType.FEED;
		
		//if the user mode is not available
		if(mode == null || mode.isEmpty()) {
			//discern the mode from the user agent string
			if(userAgent.contains("stanza")) {
				mode = "stanza";
			} else if(userAgent.contains("aldiko")) {
				mode = "aldiko";
			} else if(userAgent.contains("fbreader")) {
				mode = "fbreader";
			} else if(userAgent.contains("opds")) {
				mode = "opds";
			} else if(userAgent.contains("kindle") || userAgent.contains("nook")) {
				mode = "epaper";
			} else if((userAgent.contains("safari") && userAgent.contains("ip") && userAgent.contains("mobile")) || userAgent.contains("android")) {
				mode = "mobile";
			} else {
				mode = "none";
			}
		} else {
			mode = mode.toLowerCase();
		}
		
		String type = request.getParameter("type");
		if(type == null || type.isEmpty()) type = "feed";
		if("/book".equals(path) || "/book.xml".equals(path) || "/book.html".equals(path)) type = "book";
		
		//if the local name ends with xml, set xml as the output type
		if(path.endsWith("xml")) {
			outputType = OutputType.XML;
		//same for html
		} else if(path.endsWith("html")) {
			outputType = OutputType.HTML;
		}
		
		//in some cases the mode overrides the local name, as in the case of readers or json  
		if("stanza".equals(mode) || "aldiko".equals(mode) || "fbreader".equals(mode)  || "opds".equals(mode)) {
			outputType = OutputType.FEED;
		} else if("json".equals(mode) && !OutputType.HTML.equals(outputType)) {
			//can't do .html and json
			outputType = OutputType.JSON;
		} else if("mobile".equals(mode)){
			//mobile browsers that weren't detected as a feed based reader (aldiko, stanza, etc) should become mobile output html
			outputType = OutputType.HTML;
		} else {
			//todo: should output type equal html here?  signifies that we don't know what type it is?
		}
		
		//output writer
		Writer out = new StringWriter();
		
		//when the file is an xml file, set it up as a UTF8 atom feed
		if(OutputType.JSON.equals(outputType)) {
			response.setContentType("application/json");
		} else if(OutputType.XML.equals(outputType)) {
			response.setContentType("text/xml");
		} else if(OutputType.HTML.equals(outputType)) {
			response.setContentType("text/html");
		} else if(OutputType.FEED.equals(outputType)){
			if(!"book".equals(type)) {
				response.setContentType("application/atom+xml;type=feed;profile=opds-catalog");				
			} else {
				response.setContentType("application/atom+xml;type=entry;profile=opds-catalog");
			}
		}
		
		//always use utf8, fixed problems with accent marks and the "beta" character and others used in book titles
		response.setCharacterEncoding("UTF8");		
		
		//do action
		JanusResponse janusResponse = null;
		try {
			janusResponse = this.janusAction(request, response, out, mode);
		} catch (Exception ex) {
			//end benchmark
			long end = (new Date()).getTime();
			
			this.logger.error("Servlet failed: {}ms (path={}, query=\"{}\", mode={}, output={}, type={}, useragent=\"{}\")",new Object[]{Long.toString((end-start)),path,query,mode,outputType.toString(),type,userAgent});
			
			//print stack trace
			ex.printStackTrace();			
			
			//an exception was caught, go straight to 500 error
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		//finalize
		if(out != null) {
			out.flush();
		}

		String output = out.toString();
		
		//log output
		this.logger.debug("\nInitial XML Output: \n{}\n", output);
		
		//transform to json and output now, if exists
		if(OutputType.JSON.equals(outputType)) {
			//get output as string
			String outputXML = output;
			
			//transform!
			JSONObject outputObject = null;
			try {
				outputObject = XML.toJSONObject(outputXML);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			output = "{}";
			if(outputObject != null) {
				output = outputObject.toString();
			}  
		} else if(OutputType.HTML.equals(outputType)) {
			//capture xml
			String outputXML = output;
			//default html message
			String outputHTML = "<html></html>";
			
			try {
				//create temlate map
				Map<String,Object> root = new HashMap<String, Object>(10);
			
				//use html template with xml message as input
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				InputSource is = new InputSource(new StringReader(outputXML));
								
				Document document = builder.parse(is);
				
				Node node = document.getDocumentElement();
				NodeModel.simplify(node);
				NodeModel model = NodeModel.wrap(node);				
				
				//put that in the document model
				root.put("doc", model);
				root.put("mode", mode);
				//output writer
				Writer htmlOut = new StringWriter();
				//decide on template type
				String template = "feed";
				if("book".equals(type)) {
					template = "entry";
				}
				
				//get template root
				String htmlType = "enhanced";
				if("mobile".equals(mode)) {
					htmlType = "mobile";
				} else if("epaper".equals(mode)) {
					htmlType = "simple";
				} 
				
				//put other useful values in template controller
				root.put("path", path);
				root.put("query", query);
				if(previousPath != null && !previousPath.isEmpty()) {
					root.put("previousPath", previousPath);
					root.put("previousQuery", "?" + previousQuery);
				}
				
				root.put("type", htmlType);
				if(sort != null) {
					root.put("sort", sort);
				}
				
				//get response values from other pages
				if(janusResponse != null) {
					int currentIndex = janusResponse.getCurrentIndex();
					int pageSize = janusResponse.getPageSize();
					int total = janusResponse.getItems();
					
					if(currentIndex < 0) {
						currentIndex = 0;
					}
					
					//calculate pages if pages exist
					if(pageSize < total) {
						int pages = (int)Math.ceil((total / (pageSize * 1.0f))) - 1;
						int page = currentIndex / pageSize;
						
						//put in templates model
						root.put("currentPage", new Integer(page));
						root.put("totalPages", new Integer(pages));
						root.put("pageSize", new Integer(pageSize));
					}
				} 
				
				//process template
				TemplateController.INSTANCE.process(htmlOut, root, htmlType + "/" + template + ".ftl");
				
				//recover string from output writer
				outputHTML = htmlOut.toString();
			} catch (SAXException e) {
				this.logger.error("There was an error parsing the input xml.",e);
			} catch (ParserConfigurationException e) {
				this.logger.error("There was an error parsing the input xml.",e);
			} 
			
			if(outputHTML != null && outputHTML.length() > 0) {
				output = outputHTML;
			}
		}
		
		//responses to make no-cache happen, causes problems with jQuery
		// Set to expire far in the past.
		response.addHeader("Expires", "0");
		// Set standard HTTP/1.1 no-cache headers. (And extended no-cache headers.)
		response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.addHeader("Pragma", "no-cache");		
		
		//set output size in bytes
		response.setContentLength(output.getBytes().length);
		
		//write output of xml/json/html and flush
		Writer responseWriter = response.getWriter();
		responseWriter.write(output);
		responseWriter.flush();
		
		//save these values to session
		request.getSession().setAttribute(JanusController.PREVIOUS_PATH_KEY, path);
		request.getSession().setAttribute(JanusController.PREVIOUS_QUERY_KEY, query);
		
		//end benchmark
		long end = (new Date()).getTime();
		
		//finally output for metrics
		this.logger.info("Servlet took: {}ms (path={}, query=\"{}\", mode={}, output={}, type={}, useragent=\"{}\")",new Object[]{Long.toString((end-start)),path,query,mode,outputType.toString(),type,userAgent});
	}

	protected abstract JanusResponse janusAction(HttpServletRequest request, HttpServletResponse response, Writer out, String mode) throws ServletException, IOException;	
}
