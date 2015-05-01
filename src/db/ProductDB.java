package db;

import java.util.ArrayList;

import domain.DomainException;
import domain.Product;

public class ProductDB {
	
	private ArrayList<Product> stock = new ArrayList<Product>();
	private volatile static ProductDB uniqueInstance;
	private static long nextId = 5001;

	private ProductDB() {
		 try {
			addProductToStock(new Product("Monopoly", "Het enige echte klassieke Monopoly Standaard Euro Editie spel staat al jaren garant voor veel speelplezier. De benodigdheden om de overwinning te behalen blijven in Monopoly altijd hetzelfde: een beetje geluk en een heleboel tactiek! Monopoly Standaard Euro Editie is geschikt voor 2 tot 8 spelers.", 29.99, 5));
			addProductToStock(new Product("Dokter Bibber", "Jij kan de patient verlossen van zijn pijn. Maar wees voorzichtig, want de patient is erg gevoelig. Haal met een pincet heel voorzichtig zijn hart, darm, maag en bot eruit. Maar pas op dat je hem niet raakt!", 27.99, 80));
		 } catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ProductDB getInstance (){ 
        if (uniqueInstance == null) { 
            synchronized (ProductDB.class) { 
                if (uniqueInstance == null) { 
                    uniqueInstance = new ProductDB(); 
                } 
            } 
        } 
        return uniqueInstance; 
    } 

    private long getNextId() {
        return nextId;
    }

	public void addProductToStock(Product product) throws DbException {
		if(stock.contains(product)) {
			throw new DbException("Product already exists.");
		}
		try {
			product.setId(getNextId());
		} catch (DomainException e) {
			throw new DbException(e.getMessage());
		}
		stock.add(product);
		nextId++;
	}
	
	public void removeProductToStock(Product product) {
		stock.remove(product);
	}
	
	public void lowerStock(Product product, int number) throws DbException {
		try {
			product.setNumberStock(product.getNumberStock() - number);
		} catch (DomainException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public void raiseStock(Product product, int number) throws DbException {
		try {
			product.setNumberStock(product.getNumberStock() + number);
		} catch (DomainException e) {
			throw new DbException(e.getMessage());
		}
	}

	public Product getProduct(String productName) {
		Product product = null;
		for(Product p : stock) {
			if(p.getName().equals(productName)) {
				product = p;
				break;
			}
		}
		return product;
	}

	public int getNumberOfProducts() {
		return stock.size();
	}

	public ArrayList<Product> getStock() {
		return stock;
	}

	public Product getProduct(long id) {
		Product product = null;
		for(Product p : stock) {
			if(p.getId() == id) {
				product = p;
				break;
			}
		}
		return product;
	}
	
	

}
