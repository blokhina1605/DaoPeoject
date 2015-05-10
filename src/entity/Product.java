package entity;

/**
 * Created by jecka on 10.05.2015.
 */
public class Product extends ADaoEntity {
    private String name;
    private String description;
    private double price;
    private int id;


    public Product() {

    }

    public Product(int id, String name, String description, double price) {
        setId(id);
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product"+getId()+"{" +
                 name +
                ", desc:" + description +
                ", price:" + price +
                '}';
    }
}
