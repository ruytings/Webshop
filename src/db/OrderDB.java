package db;

import java.util.ArrayList;

import domain.DomainException;
import domain.Order;
import domain.Product;
import domain.User;

public class OrderDB {

	private ArrayList<Order> orders = new ArrayList<Order>();
	private ArrayList<Order> ordersDone = new ArrayList<Order>();
	private volatile static OrderDB uniqueInstance;
	private static long nextId = 3001;

	private OrderDB() {
		 
	}
	
	public static OrderDB getInstance (){ 
        if (uniqueInstance == null) { 
            synchronized (OrderDB.class) { 
                if (uniqueInstance == null) { 
                    uniqueInstance = new OrderDB(); 
                } 
            } 
        } 
        return uniqueInstance; 
    } 

    private long getNextId() {
        return nextId;
    }

	public void addOrder(Order order) throws DbException, DomainException {
		order.setId(nextId);
		nextId++;
		if(orders.contains(order)) {
			throw new DbException("Order already exists.");
		}
		try {
			orders.add(order);
			order.getUser().addOrder(order);
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
	}

	public boolean orderExists(Product product, User user) {
		boolean exists = false;
		for(Order o : orders) {
			if(o.getProduct().equals(product) && o.getUser().equals(user)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public Order getOrder(Product product, User user) {
		Order order = null;
		for(Order o : orders) {
			if(o.getProduct().equals(product) && o.getUser().equals(user)) {
				order = o;
				break;
			}
		}
		return order;
	}

	public int getNumberOfOrders() {
		return orders.size();
	}
	
	public ArrayList<Order> getOrderList() {
		return orders;
	}

	public Order getOrder(Long id) {
		Order order = null;
		for(Order o : orders) {
			if(o.getId() == id) {
				order = o;
				break;
			}
		}
		return order;
	}

	public void deleteOrder(Order o) {
		orders.remove(o);
	}
	
}
