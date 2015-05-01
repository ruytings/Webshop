package domain;

import java.util.ArrayList;

public class User extends Identifiable {

	private String userName;
	private String email;
	private String password;
	private ArrayList<Order> orders;
	private ArrayList<Order> history;
	private boolean isAdmin = false;

	public User(String userName, String email, String password)
			throws DomainException {
		super();
		setUserName(userName);
		setEmail(email);
		setPassword(password);
		orders = new ArrayList<Order>();
		history = new ArrayList<Order>();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) throws DomainException {
		if (userName == null || userName.isEmpty()) {
			throw new DomainException("Username cannot be null.");
		}
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws DomainException {
		if (email == null || email.isEmpty() || !email.contains("@")
				|| !email.contains(".")) {
			throw new DomainException("Invalid e-mail.");
		}
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws DomainException {
		if (password.length() < 6 || password == null) {
			throw new DomainException("Invalid password.");
		}
		this.password = password;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public ArrayList<Order> getHistory() {
		return history;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean equals(Object object) {
		boolean isEqual = false;
		if (object instanceof User) {
			User user = (User) object;
			if (user.getId() == this.getId()) {
				isEqual = true;
			}
		}
		return isEqual;
	}

	public void addOrder(Order order) throws DomainException {
		if (orders.contains(order)) {
			throw new DomainException("Order already exists.");
		}
		orders.add(order);
	}

	public void cancelOrder(Order order) {
		orders.remove(order);
	}

	public void recieveOrder(Order order) throws DomainException {
		if (!orders.contains(order)) {
			throw new DomainException("Order doesn't exists.");
		}
		orders.remove(order);
		history.add(order);
	}

	public Order getOrder(Product product) {
		Order order = null;
		for (Order o : orders) {
			if (o.getProduct().equals(product)) {
				order = o;
			}
		}
		return order;
	}

	public double getTotalPrice() {
		double total = 0;
		for (Order o : orders) {
			total = o.getProduct().getPrice() * o.getQuantity() + total;
		}
		return total;
	}

	public int getTotalQuantity() {
		int total = 0;
		for (Order o : orders) {
			total = o.getQuantity() + total;
		}
		return total;
	}

	@Override
	public String toString() {
		return userName + " - " + email;
	}
	
	public boolean orderExists(Product product) {
		boolean exists = false;
		for(Order o : orders) {
			if(o.getProduct().equals(product)) {
				exists = true;
				break;
			}
		}
		return exists;
	}
}
