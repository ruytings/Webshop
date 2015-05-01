package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.OrderDB;
import db.ProductDB;
import db.UserDB;
import domain.*;

@WebServlet("/ShopServlet")
public class ShopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Shop shop;

	public ShopServlet() {
		super();
		shop = shop.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String result = toXML(request);
			response.setContentType("text/xml");
			response.getWriter().write(result);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = createResultPost(request, response);
		response.setContentType("text/xml");
		response.getWriter().write(result);
	}

	private String createResultPost(HttpServletRequest request, HttpServletResponse response) {
		Long id = (Long) request.getSession().getAttribute("user");
		User user = shop.getUsers().getUser(id);
		Long productId = Long.parseLong(request.getParameter("product"));
		Product product = shop.getProducts().getProduct(productId);
		Order order;
		if(product.getNumberStock() != 0) {
			try {
				if(user.orderExists(product)) {
					order = user.getOrder(product);
					order.setQuantity(order.getQuantity() + 1);
					product.setNumberStock(product.getNumberStock() - 1);
				} else {
					shop.getOrders().addOrder(new Order(product, user, 1)); 
					product.setNumberStock(product.getNumberStock() - 1);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		String xmlResponse = toXML(request);
		return xmlResponse;
	}

	private String toXML(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Long id = (Long) session.getAttribute("user");
		User user = shop.getUsers().getUser(id);

		StringBuffer xmlDoc = new StringBuffer();

		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<shopinfo>\n");

		xmlDoc.append("<cart>\n");
		xmlDoc.append("<totalPrice>");
		xmlDoc.append(user.getTotalPrice());
		xmlDoc.append("</totalPrice>\n");
		xmlDoc.append("<totalQuantity>");
		xmlDoc.append(user.getTotalQuantity());
		xmlDoc.append("</totalQuantity>\n");
		xmlDoc.append("</cart>\n");

		xmlDoc.append("<stock>\n");
		for (int i = 0; i < shop.getProducts().getNumberOfProducts(); i++) {
			xmlDoc.append("<product>\n");
			xmlDoc.append("<id>");
			xmlDoc.append(shop.getProducts().getStock().get(i).getId());
			xmlDoc.append("</id>\n");
			xmlDoc.append("<name>");
			xmlDoc.append(shop.getProducts().getStock().get(i).getName());
			xmlDoc.append("</name>\n");
			xmlDoc.append("<numberinstock>");
			xmlDoc.append(shop.getProducts().getStock().get(i).getNumberStock());
			xmlDoc.append("</numberinstock>\n");
			xmlDoc.append("<description>");
			xmlDoc.append(shop.getProducts().getStock().get(i).getDescription());
			xmlDoc.append("</description>\n");
			xmlDoc.append("<price>");
			xmlDoc.append(shop.getProducts().getStock().get(i).getPrice());
			xmlDoc.append("</price>\n");
			xmlDoc.append("</product>\n");
		}
		xmlDoc.append("</stock>\n");

		xmlDoc.append("<user>");
		xmlDoc.append(id);
		xmlDoc.append("</user>\n");

		xmlDoc.append("</shopinfo>\n");

		return xmlDoc.toString();
	}

}


