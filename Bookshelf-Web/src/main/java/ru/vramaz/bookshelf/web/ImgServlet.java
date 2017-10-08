package ru.vramaz.bookshelf.web;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.vramaz.bookshelf.core.ImageService;

/**
 *  /images/books/[name]
 *	/images/users/[name]
 */
@WebServlet(name="ImgServlet", urlPatterns={"/images/*"})
public class ImgServlet extends HttpServlet {

	private static final long serialVersionUID = -3646897369894171598L;

	@EJB
	private ImageService imgService;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String imgPath = req.getRequestURI().split("/images/")[1];
		res.setHeader("Content-Type", "image/jpeg");
		InputStream in = imgService.findImg(imgPath);

		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = in.read(buffer)) != -1) {
			res.getOutputStream().write(buffer, 0, bytesRead);;
		}
		
		in.close();
	}

}
