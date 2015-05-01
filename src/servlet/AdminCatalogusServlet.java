package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ProductDB;
import domain.DomainException;
import domain.Product;
import domain.Shop;

@WebServlet("/AdminCatalogusServlet")
public class AdminCatalogusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Shop shop;

	public AdminCatalogusServlet() {
		super();
		shop = Shop.getInstance();
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = toXML(shop.getProducts());;
		String action = request.getParameter("action");

		if (action.equals("add")) {
			try {
				String name = request.getParameter("name");
				String description = request.getParameter("description");
				double price = Double.parseDouble(request.getParameter("price"));
				int stock = Integer.parseInt(request.getParameter("stock"));
				if(shop.getProducts().getProduct(name) != null) {
					Product p = shop.getProducts().getProduct(name);
					p.setDescription(description);
					p.setPrice(price);
					p.setNumberStock(stock);
					result = toXML(shop.getProducts());
				} else {
					Product p = new Product(name, description, price, stock);
					shop.getProducts().addProductToStock(p);
					result = toXML(shop.getProducts());
				}
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
				result = toXmlError(request);
			}
		} else if(action.equals("delete")) {
			String name = request.getParameter("name");
			Product p = shop.getProducts().getProduct(name);
			shop.getProducts().removeProductToStock(p);
			result = toXML(shop.getProducts());
		} else if(action.equals("edit")) {
			try {
				long id = Long.parseLong(request.getParameter("id"));
				String name = request.getParameter("name");
				String description = request.getParameter("description");
				double price = Double.parseDouble(request.getParameter("price"));
				int stock = Integer.parseInt(request.getParameter("stock"));
				Product p = shop.getProducts().getProduct(id);
				p.setName(name);
				p.setDescription(description);
				p.setNumberStock(stock);
				p.setPrice(price);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
				result = toXmlError(request);
			}
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

	private String toXML(ProductDB productDB) {
		StringBuffer xmlDoc = new StringBuffer();

		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<shopinfo>\n");

		xmlDoc.append("<stock>\n");
		for (int i = 0; i < productDB.getNumberOfProducts(); i++) {
			xmlDoc.append("<product>\n");
			xmlDoc.append("<id>");
			xmlDoc.append(productDB.getStock().get(i).getId());
			xmlDoc.append("</id>\n");
			xmlDoc.append("<name>");
			xmlDoc.append(productDB.getStock().get(i).getName());
			xmlDoc.append("</name>\n");
			xmlDoc.append("<numberinstock>");
			xmlDoc.append(productDB.getStock().get(i).getNumberStock());
			xmlDoc.append("</numberinstock>\n");
			xmlDoc.append("<description>");
			xmlDoc.append(productDB.getStock().get(i).getDescription());
			xmlDoc.append("</description>\n");
			xmlDoc.append("<price>");
			xmlDoc.append(productDB.getStock().get(i).getPrice());
			xmlDoc.append("</price>\n");
			xmlDoc.append("</product>\n");
		}
		xmlDoc.append("</stock>\n");

		xmlDoc.append("</shopinfo>\n");

		return xmlDoc.toString();
	}

}
