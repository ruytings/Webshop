package domain;

public class Order extends Identifiable {

	private Product product;
	private User user;
	private int quantity;
	private String status = "In karretje";
	
	public Order(Product product, User user, int quantity) throws DomainException {
		super();
		setProduct(product);
		setUser(user);
		setQuantity(quantity);
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) throws DomainException {
		if(product== null) {
			throw new DomainException("Invalid product.");
		}
		this.product = product;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) throws DomainException {
		if(user == null) {
			throw new DomainException("Invalid user.");
		}
		this.user = user;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) throws DomainException {
		if(quantity < 0) {
			throw new DomainException("Invalid quantity.");
		}
		this.quantity = quantity;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
