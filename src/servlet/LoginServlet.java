package servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import javax.servlet.http.HttpSession;

import domain.Product;
import domain.Shop;
import domain.User;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Shop shop;
       
    public LoginServlet() {
        super();
        shop = Shop.getInstance();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Random r = new Random();
		int index = r.nextInt(shop.getProducts().getStock().size());
		Product p = shop.getProducts().getStock().get(index);
		String result = toXmlProduct(p);
		response.setContentType("text/xml");
		response.getWriter().write(result);	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		User user = shop.getUsers().getUser(name);
		String result = null;
		if(user != null) {
			if(user.getPassword().equals(password)) {
				request.getSession(true);
				request.getSession().setAttribute("user", user.getId());
				HttpSession session = request.getSession();
				Long id = (Long) session.getAttribute("user"); 
				result = toXml(user);
				
			} else {
				request.setAttribute("error", "Ongeldige inloggegevens");
				result = toXmlError(request);
			}
		} else {
			request.setAttribute("error", "Ongeldige inloggegevens");
			result = toXmlError(request);
		}
		
		response.setContentType("text/xml");
		response.getWriter().write(result);
	}


	
	protected String toXmlError(HttpServletRequest request) {
		StringBuffer xmlDoc = new StringBuffer();
		
		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<error>\n");
			xmlDoc.append("<message>\n");
				xmlDoc.append(request.getAttribute("error"));
					xmlDoc.append("</message>\n");
			xmlDoc.append("</error>\n");
			
		return xmlDoc.toString();
	}
	
	protected String toXml(User user) {
		StringBuffer xmlDoc = new StringBuffer();
		
		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");

		xmlDoc.append("<user>\n");
		xmlDoc.append("<status>\n");
		if(user.isAdmin()) {
			xmlDoc.append(1);
		} else {
			xmlDoc.append(2);
		}
		xmlDoc.append("</status>\n");
		xmlDoc.append("</user>\n");
		
		return xmlDoc.toString();
	}
	
	private String toXmlProduct(Product p) {
		StringBuffer xmlDoc = new StringBuffer();
		
		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");

		xmlDoc.append("<product>\n");
		xmlDoc.append("<id>");
		xmlDoc.append(p.getId());
		xmlDoc.append("</id>\n");
		xmlDoc.append("<name>");
		xmlDoc.append(p.getName());
		xmlDoc.append("</name>\n");
		xmlDoc.append("<numberinstock>");
		xmlDoc.append(p.getNumberStock());
		xmlDoc.append("</numberinstock>\n");
		xmlDoc.append("<description>");
		xmlDoc.append(p.getDescription());
		xmlDoc.append("</description>\n");
		xmlDoc.append("<price>");
		xmlDoc.append(p.getPrice());
		xmlDoc.append("</price>\n");
		xmlDoc.append("</product>\n");
			
		return xmlDoc.toString();
	}
}
