package domain;

public class Product extends Identifiable {

	private String name;
	private String description;
	private double price;
	private int numberStock;
	
	public Product(String name, String description, double price, int numberStock) throws DomainException {
		super();
		setName(name);
		setDescription(description);
		setPrice(price);
		setNumberStock(numberStock);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws DomainException {
		if(name == null || name.isEmpty()) {
			throw new DomainException("Name cannot be null.");
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws DomainException {
		if(description == null || description.isEmpty()) {
			throw new DomainException("Description cannot be null.");
		}
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) throws DomainException {
		if(price < 0) {
			throw new DomainException("Price must be > 0.");
		}
		this.price = price;
	}

	public int getNumberStock() {
		return numberStock;
	}

	public void setNumberStock(int numberStock) throws DomainException {
		if(numberStock < 0) {
			throw new DomainException("Number of products in stock must be > 0");
		}
		this.numberStock = numberStock;
	}
	
	public void lowerStock(int number) {
		numberStock -= number;
	}
	
	public void raiseStock(int number) {
		numberStock += number;
	}
	
	public boolean equals(Object object) {
		boolean isEqual = false;
		if(object instanceof Product) {
			Product product = (Product) object;
			if(product.getId() == this.getId()) {
				isEqual = true;
			}
		}
		return isEqual;
	}
	
	public String toString() {
		return this.getName();
	}
	
	
	
	
	
}
