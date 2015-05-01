package domain;

import db.*;

public class Shop {
	
	private OrderDB orders = OrderDB.getInstance();
	private ProductDB products = ProductDB.getInstance();
	private UserDB users = UserDB.getInstance();
	private volatile static Shop uniqueInstance;

	private Shop() {
		 
	}
	
	public static Shop getInstance (){ 
        if (uniqueInstance == null) { 
            synchronized (ProductDB.class) { 
                if (uniqueInstance == null) { 
                    uniqueInstance = new Shop(); 
                } 
            } 
        } 
        return uniqueInstance; 
    } 
	
	public void registerUser(String userName, String email, String password, String confirmation) throws DomainException {
		if(!password.equals(confirmation)) {
			throw new DomainException("Passwords do not match.");
		}
		try {
			User user = new User(userName, email, password);
			users.addUser(user);
		} catch (Exception e) {
			throw new DomainException(e.getMessage());
		}
	}

	public OrderDB getOrders() {
		return orders;
	}

	public ProductDB getProducts() {
		return products;
	}

	public UserDB getUsers() {
		return users;
	}
	

}
