package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.DomainException;
import domain.Order;
import domain.Product;
import domain.Shop;
import domain.User;

@WebServlet("/AdminOrdersServlet")
public class AdminOrdersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Shop shop;

	public AdminOrdersServlet() {
		super();
		shop = Shop.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = toXML(request);
		response.setContentType("text/xml");
		response.getWriter().write(result);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String result = toXML(request);
		if (action.equals("add")) {
			try {
				long userId = Long.parseLong(request.getParameter("user"));
				long productId = Long.parseLong(request.getParameter("product"));
				int quantity = Integer.parseInt(request.getParameter("quantity"));
				Product p = shop.getProducts().getProduct(productId);
				User u = shop.getUsers().getUser(userId);
				Order o = u.getOrder(p);
				if (u.orderExists(p)) {
					if(quantity <= p.getNumberStock() + o.getQuantity()) {
						p.setNumberStock(p.getNumberStock() + o.getQuantity());
						o.setQuantity(quantity);
						p.setNumberStock(p.getNumberStock() - quantity);
						result = toXML(request);
					} else {
						request.setAttribute("error", "Niet genoeg artikelen meer in stock.");
						result = toXMLError(request);
					}
				} else {
					if(quantity <= p.getNumberStock()) {
						o = new Order(p, u, quantity);
						p.setNumberStock(p.getNumberStock() - quantity);
						shop.getOrders().addOrder(o);
						result = toXML(request);
					} else {
						request.setAttribute("error", "Niet genoeg artikelen meer in stock.");
						result = toXMLError(request);
					}
				}
			} catch (Exception e) {
				request.setAttribute("error", "Invalid input.");
				result = toXMLError(request);
			}
		}
		else if (action.equals("delete")) {
			long id = Long.parseLong(request.getParameter("id"));
			Order o = shop.getOrders().getOrder(id);
			User user = o.getUser();
			Product product = o.getProduct();
			user.getHistory().remove(o);
			shop.getOrders().deleteOrder(o);
				try {
					product.setNumberStock(product.getNumberStock() + o.getQuantity());
				} catch (DomainException e) {
					request.setAttribute("error", e.getMessage());
					result = toXMLError(request);
				}
				user.cancelOrder(o);
				result = toXML(request);
		} else if (action.equals("buy")) {
			long id = Long.parseLong(request.getParameter("id"));
			Order o = shop.getOrders().getOrder(id);
			User user = o.getUser();
			try {
				user.recieveOrder(o);
			} catch (DomainException e) {
				request.setAttribute("error", e.getMessage());
				result = toXMLError(request);
			}
			o.setStatus("Betaald");
			result = toXML(request);
		}

			response.setContentType("text/xml");
			response.getWriter().write(result);
		
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

	private String toXML(HttpServletRequest request) {

		StringBuffer xmlDoc = new StringBuffer();

		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<orders>\n");

		for (int i = 0; i < shop.getOrders().getNumberOfOrders(); i++) {
			xmlDoc.append("<order>\n");
			xmlDoc.append("<id>");
			xmlDoc.append(shop.getOrders().getOrderList().get(i).getId());
			xmlDoc.append("</id>\n");
			xmlDoc.append("<productId>");
			xmlDoc.append(shop.getOrders().getOrderList().get(i).getProduct()
					.getId());
			xmlDoc.append("</productId>\n");
			xmlDoc.append("<userId>");
			xmlDoc.append(shop.getOrders().getOrderList().get(i).getUser()
					.getId());
			xmlDoc.append("</userId>\n");
			xmlDoc.append("<quantity>");
			xmlDoc.append(shop.getOrders().getOrderList().get(i).getQuantity());
			xmlDoc.append("</quantity>\n");
			xmlDoc.append("<status>");
			xmlDoc.append(shop.getOrders().getOrderList().get(i).getStatus());
			xmlDoc.append("</status>\n");
			xmlDoc.append("</order>\n");
		}
		xmlDoc.append("</orders>\n");

		return xmlDoc.toString();
	}

}
