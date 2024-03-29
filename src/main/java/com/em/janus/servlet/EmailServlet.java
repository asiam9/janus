package com.em.janus.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.slf4j.Logger;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.config.ServletConfigUtility;
import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.BookFilesAO;
import com.em.janus.model.Book;
import com.em.janus.model.FileInfo;

@WebServlet("/email")
public class EmailServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmailServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.sendEmail(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.sendEmail(request, response);
	}

	private void sendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//get the servlet context
		ServletContext context = request.getServletContext();
		
		JanusConfiguration config = ServletConfigUtility.getConfigurationFromContext(request.getServletContext());
		
		Enumeration<String> e = request.getAttributeNames();
		while(e.hasMoreElements()) {
			String s = e.nextElement();
			System.out.println("!!!" + s);
		}
		
		//get email properties from config
		String from = config.getEmailFrom();
		String smtp = config.getSmtp();
		int port = config.getPort();
		String user = config.getEmailUser();
		String password = config.getEmailPassword();
		String security = config.getSecurity();
		boolean ssl = false;
		boolean tls = false;	
		
		//set up ssl/tls booleans from configured security type
		ssl = "ssl".equalsIgnoreCase(security);
		tls = "tls".equalsIgnoreCase(security);
		
		//get if this is an ajax call or not
		String ajaxString = request.getParameter("ajax");
		boolean ajax = false;
		if("true".equalsIgnoreCase(ajaxString)) {
			ajax = true;
		}

		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			this.logger.error("Invalid ID or ID not found.  No email sent.");

			if(ajax) {
				//respond
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("fail");
				response.getWriter().flush();
			}
			
			return;
		}
		
		//target email address
		String to = request.getParameter("to");
		if(to == null || to.isEmpty()) {
			this.logger.error("No 'to' address specified.  No email sent.");

			if(ajax) {
				//respond
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("fail");
				response.getWriter().flush();
			}
			
			return;
		}
		
		String ext = request.getParameter("ext");
		if(ext == null || ext.isEmpty()) {
			ext = "mobi";
		} else {
			//if it starts with a ".", chop it
			if(ext.startsWith(".")) {
				ext = ext.substring(1);
			}
		}
		
		this.logger.info("to={}, id={}, ext={}", new Object[]{to,id,ext});

		//get the file from the file access object
		FileInfo fileInfo = BookFilesAO.INSTANCE.getBookFile(context, DAOFactory.INSTANCE.getDAO(Book.class), id, ext);
		
		//if something is wrong with the file, then bail with a 404
		if(fileInfo.getFile() == null || fileInfo.getFile().isDirectory() || fileInfo.getFile().length() == 0) {
			this.logger.error("File not found, file is directory, or the file is empty.  No email sent.");
			
			//an exception was caught, do not redirect, but throw it back as a 404.
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("fail");
			response.getWriter().flush();
			
			return;
		}

		//file from file info
		File file = fileInfo.getFile();

		//copy file to byte array
		byte[] fileBytes = FileUtils.readFileToByteArray(file);
		this.logger.debug("Attachment read into byte array.");
		
		//use simple-java-mail to send email (with attachment)
		final Email email = new Email();
		
		//set subject and text
		email.setFromAddress(from, from);
		email.addRecipient(to, to, RecipientType.TO);
		email.setSubject("Janus eBook Delivery: " + file.getName());
		email.setText("Find your eBook, " + file.getName() + ", attached.");
		
		//add attachment
		email.addAttachment(file.getName(), fileBytes, fileInfo.getMimeType());
		
		this.logger.debug("Mail for '{}' created with file '{}' (mime type = '{}') and ready to send.", new Object[]{to, file.getName(), fileInfo.getMimeType()});
		
		//choose transport strategy based on security
		TransportStrategy transport = TransportStrategy.SMTP_PLAIN;
		if(ssl) {
			this.logger.debug("Using ssl transport.");
			transport = TransportStrategy.SMTP_SSL;
		} else if(tls) {
			this.logger.debug("Using tls transport.");
			transport = TransportStrategy.SMTP_TLS;
		}
		
		//create sender
		Mailer mailer = null;
		if(user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
			mailer = new Mailer(smtp, port, user, password, transport);
		} else {
			mailer = new Mailer(smtp, port, "", "", transport);
		}
		
		//get success or fail
		String send = null;
		
		//send email or log failure
		try {
			mailer.sendMail(email);
			//log send
			this.logger.info("Mail sent to: '{}' with attachment '{}'", to, file.getName());
			send = "true";
		} catch (Exception ex) {
			this.logger.error("Could not send mail to '{}'",to,ex);
			send = "fail";
			
			//if an error occurs, fail
			if(ajax) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("fail");
				response.getWriter().flush();
				return;
			}
		}
		
		//regardless, return
		if(ajax) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("ok");
			response.getWriter().flush();
		} else {
			String referer = request.getHeader("Referer");
			response.sendRedirect(referer + "?send=" + send);
		}
	}	
	
}
