package com.em.janus.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.CoverFileAO;
import com.em.janus.model.Book;
import com.thebuzzmedia.imgscalr.Scalr;

/**
 * Servlet implementation class CoverController
 */
@WebServlet("/image")
public class ImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.serveImage(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.serveImage(request, response);
	}
	
	protected void serveImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get the servlet context
		ServletContext context = request.getServletContext();
		
		//type
		String type = request.getParameter("type");
		if(type == null || type.isEmpty()) type = "cover";
		if(type.contains("thumb")) type = "thumbnail";		

		//get id
		String idString = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (Exception ex) {
			id = 0;
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		//get w(idth) and h(eight)
		int w = 65;
		int h = 108;
		boolean resize = false;
		
		String wString = request.getParameter("w");
		String hString = request.getParameter("h");
		
		try {
			w = Integer.parseInt(wString);
			resize = true;
		} catch (Exception ex) {
			//do nothing, keep default width
		}
		
		try {
		 	h = Integer.parseInt(hString);
		 	resize = true;
		} catch (Exception ex) {
			//do nothing, keep default height
		}
		
		//get book
		Set<Book> books = DAOFactory.INSTANCE.getDAO(Book.class).getByBookId(id);
		
		File coverFile = null;
		
		if(books.size() > 0) {
			Book book = books.iterator().next();
			
			coverFile = CoverFileAO.INSTANCE.getCoverImage(book);
		}

		//if the cover file is available
		if(coverFile != null) {
			
			//pull mime type from the cover file
			String mimeType = context.getMimeType(coverFile.getAbsolutePath());
			if(mimeType == null || mimeType.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			    return;
			}
			
			//set content type and length
			response.setContentType(mimeType);

			//log path
			this.logger.debug("Cover: \"{}\".",coverFile.getAbsolutePath());
			
			//get response output stream
			OutputStream out = response.getOutputStream();
			
			//load and resize image
			if("thumbnail".equals(type) || resize == true) {
				BufferedImage cover = ImageIO.read(coverFile);
				BufferedImage thumbnail = Scalr.resize(cover, Scalr.Method.SPEED, w, h);
				ImageIO.write(thumbnail, "jpg", out);
				//set image name in output header
				response.setHeader( "Content-Disposition", "attachment; filename=\"thumbnail.jpg\"" );
			} else {
				InputStream in = new FileInputStream(coverFile);
				response.setContentLength((int)coverFile.length());
				// copy the contents of the file to the output stream
				byte[] buf = new byte[8192];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
				    out.write(buf, 0, count);
				}
				in.close();
				//set image name in output header
				response.setHeader( "Content-Disposition", "attachment; filename=\"cover.jpg\"" );
			}
			
			//close and flush
			out.flush();

		} else {
			//feed back error image
			this.logger.debug("No cover found for book id={}, failover to error image of type={}",id,type);
			//redirect to cover.jpg
		    String urlWithSessionID = response.encodeRedirectURL("./images/"+type+".jpg");
		    response.sendRedirect(urlWithSessionID);
		}
	}

}
