package domain;

public abstract class Identifiable {

    private long id;
    
    public Identifiable() {
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) throws DomainException {
        if(id < 0) {
            throw new DomainException("ID must be > 0.");
        }
        this.id = id;
    }
}