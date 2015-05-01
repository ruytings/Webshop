package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ProductDB;
import domain.DomainException;
import domain.Product;
import domain.Shop;
import domain.User;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Shop shop;

    public RegisterServlet() {
        super();
        shop = Shop.getInstance();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		register(request, response);
		if(!register(request, response)) {
			String result = toXML(request);
			response.setContentType("text/xml");
			response.getWriter().write(result);
		}

	}
	
	public boolean register(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String userName = request.getParameter("userName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmation = request.getParameter("confirmation");
		try {
			shop.registerUser(userName, email, password, confirmation);
			return true;
		} catch (Exception e) {
			request.setAttribute("error", e.getMessage());
			return false;
			
		}
	}
	
	private String toXML(HttpServletRequest request) {
		StringBuffer xmlDoc = new StringBuffer();
		
		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<error>\n");
			xmlDoc.append("<message>\n");
				xmlDoc.append(request.getAttribute("error"));
			xmlDoc.append("</message>\n");
		xmlDoc.append("</error>\n");

		return xmlDoc.toString();
	}


}
