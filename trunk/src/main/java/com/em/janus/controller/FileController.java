package com.em.janus.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.BookFilesAO;
import com.em.janus.model.Book;
import com.em.janus.model.FileInfo;

/**
 * Servlet implementation class FileController
 */
@WebServlet(description = "get files from the file system", urlPatterns = { "/file" })
public class FileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doAction(request, response);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//get the servlet context
		ServletContext context = request.getServletContext();
		
		//type
		String ext = request.getParameter("ext");
		if(ext == null || ext.isEmpty()) ext = "epub";
		//if the extension starts with "." then we can safely remove that one character with substring
		if(ext.startsWith(".")) ext = ext.substring(1);
		
		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			id = 0;
		}
		
		//get the file from the file access object
		FileInfo fileInfo = BookFilesAO.INSTANCE.getBookFile(context, DAOFactory.INSTANCE.getDAO(Book.class), id, ext);
		
		//if something is wrong with the file, then bail with a 404
		if(fileInfo.getFile() == null || fileInfo.getFile().isDirectory() || fileInfo.getFile().length() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		//file from file info
		File file = fileInfo.getFile();
		
		//output writer
		OutputStream out = response.getOutputStream();
		
		//get the input stream for the file
		InputStream in = new FileInputStream(file);
		//set the length and mime-type of the response
		response.setContentLength((int)file.length());
		response.setContentType(fileInfo.getMimeType());
		//set the return file name
		response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
		// copy the contents of the file to the output stream
		byte[] buf = new byte[8192];
		int count = 0;
		while ((count = in.read(buf)) >= 0) {
		    out.write(buf, 0, count);
		}
		in.close();
		
		//flush the output stream when done
		out.flush();
	}

}
