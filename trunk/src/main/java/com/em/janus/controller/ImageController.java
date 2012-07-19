package com.em.janus.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr;
import org.slf4j.Logger;

import com.em.janus.config.JanusConfiguration;
import com.em.janus.config.ServletConfigUtility;
import com.em.janus.dao.DAOFactory;
import com.em.janus.dao.filesystem.CoverFileAO;
import com.em.janus.model.Book;

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
		//set up
		long start = (new Date()).getTime();
		
		//get the servlet context
		ServletContext context = request.getServletContext();
		
		//get configuration from context
		final JanusConfiguration config = ServletConfigUtility.getConfigurationFromContext(context);
		
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
			
			coverFile = CoverFileAO.INSTANCE.getCoverImage(config, book);
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
			
			BufferedImage image = ImageIO.read(coverFile);
			
			//decide on resize
			int currentWidth = image.getWidth();
			int currentHeight = image.getHeight();
			if(resize && (currentWidth > w || currentHeight > h)) {
				resize = true;
			} else {
				resize = false;
			}
			
			//load and resize image
			if("thumbnail".equals(type) || resize == true) {
				image = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, w, h);
			}
			
			//write image to output stream
			ImageIO.write(image, "jpg", out);

			//set image name in output header
			response.setHeader( "Content-Disposition", "attachment; filename=\""+ type +".jpg\"" );
			
			//close and flush
			out.flush();

		} else {
			//feed back error image
			this.logger.debug("No cover found for book id={}, failover to error image of type={}",id,type);
			//redirect to cover.jpg
		    String urlWithSessionID = response.encodeRedirectURL("./img/"+type+".jpg");
		    response.sendRedirect(urlWithSessionID);
		}
		
		//end, and log
		long end = (new Date()).getTime();
		this.logger.debug("Image id={}, type={}, served in {}ms", new Object[]{Integer.toString(id), type, Long.toString(end-start)});
		
	}

}
