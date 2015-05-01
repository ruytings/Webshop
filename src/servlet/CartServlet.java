package servlet;

import java.io.IOException;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.DomainException;
import domain.Order;
import domain.Product;
import domain.Shop;
import domain.User;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Shop shop;

    public CartServlet() {
        super();
        shop = Shop.getInstance();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = toXML(request);
		response.setContentType("text/xml");
		response.getWriter().write(result);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = toXML(request);
		String action = request.getParameter("action");
		
		if(action.equals("cancel")) {
			long id = Long.parseLong(request.getParameter("id"));
			Order o = shop.getOrders().getOrder(id);
			User user = o.getUser();
			Product product = o.getProduct();
				try {
					product.setNumberStock(product.getNumberStock() + o.getQuantity());
					user.cancelOrder(o);
					shop.getOrders().deleteOrder(o);
					result = toXML(request);
				} catch (DomainException e) {
					request.setAttribute("error", e.getMessage());
					result = toXMLError(request);
				}
			}
		if(action.equals("buy")) {
			Long id = (Long) request.getSession().getAttribute("user");
			User user = shop.getUsers().getUser(id);
			for(Order order : user.getOrders()) {
				user.getHistory().add(order);
				order.setStatus("Betaald");
			}
			user.getOrders().clear();
			result = toXML(request);
		}

		response.setContentType("text/xml");
		response.getWriter().write(result);
	}
	

	private String toXML(HttpServletRequest request) {
		Long id = (Long) request.getSession().getAttribute("user");
		User user = shop.getUsers().getUser(id);
		StringBuffer xmlDoc = new StringBuffer();

		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<cart>\n");
		xmlDoc.append("<orders>\n");
		for (int i = 0; i < user.getOrders().size(); i++) {
			xmlDoc.append("<order>\n");
			xmlDoc.append("<id>");
			xmlDoc.append(user.getOrders().get(i).getId());
			xmlDoc.append("</id>\n");
			xmlDoc.append("<productId>");
			xmlDoc.append(user.getOrders().get(i).getProduct().getName());
			xmlDoc.append("</productId>\n");
			xmlDoc.append("<quantity>");
			xmlDoc.append(user.getOrders().get(i).getQuantity());
			xmlDoc.append("</quantity>\n");
			xmlDoc.append("</order>\n");
		}
		xmlDoc.append("</orders>\n");
		
		xmlDoc.append("<history>\n");
		for (int i = 0; i < user.getHistory().size(); i++) {
			xmlDoc.append("<horder>\n");
			xmlDoc.append("<hid>");
			xmlDoc.append(user.getHistory().get(i).getId());
			xmlDoc.append("</hid>\n");
			xmlDoc.append("<hproductId>");
			xmlDoc.append(user.getHistory().get(i).getProduct().getName());
			xmlDoc.append("</hproductId>\n");
			xmlDoc.append("<hquantity>");
			xmlDoc.append(user.getHistory().get(i).getQuantity());
			xmlDoc.append("</hquantity>\n");
			xmlDoc.append("</horder>\n");
		}
		xmlDoc.append("</history>\n");
		xmlDoc.append("</cart>\n");
		return xmlDoc.toString();
	}
	
	private String toXMLError(HttpServletRequest request) {
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
