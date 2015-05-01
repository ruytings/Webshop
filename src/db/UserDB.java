package db;

import java.util.ArrayList;

import domain.DomainException;
import domain.User;

public class UserDB {

	private ArrayList<User> users = new ArrayList<User>();
	private volatile static UserDB uniqueInstance;
	private static long nextId = 12001;

	private UserDB() {
		try {
			User admin = new User("elste", "a@a.a", "Abc123");
			admin.setAdmin(true);
			addUser(admin);
			addUser(new User("mikem", "b@b.b", "123Abc"));
			addUser(new User("maaus", "c@c.c", "Aaa999"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static UserDB getInstance (){ 
        if (uniqueInstance == null) { 
            synchronized (UserDB.class) { 
                if (uniqueInstance == null) { 
                    uniqueInstance = new UserDB(); 
                } 
            } 
        } 
        return uniqueInstance; 
    } 

    private long getNextId() {
        return nextId;
    }
    
    public void addUser(User user) throws DbException {
    	if(users.contains(user)) {
    		throw new DbException("User already exists.");
    	}
		try {
			user.setId(getNextId());
		} catch (DomainException e) {
			throw new DbException(e.getMessage());
		}
		users.add(user);
		nextId++;
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	public User getUser(String name) {
		User user = null;
		for(User u : users) {
			if(u.getUserName().equals(name)) {
				user = u;
			}
		}
		return user;
	}

	public User getUser(Long id) {
		User user = null;
		for(User u : users) {
			if(u.getId() == id) {
				user = u;
			}
		}
		return user;
	}
}
