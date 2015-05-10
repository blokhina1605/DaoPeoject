package entity;

import java.util.Date;
import java.util.Map;

/**
 * Created by jecka on 10.05.2015.
 */
public class Order extends ADaoEntity {

    private Date date;
    private Map<Product, Integer> orderedProducts;

    public Order() {
    }

    public Order(int id, Date date, Map<Product, Integer> orderedProducts) {
        super(id);
        this.date = date;
        this.orderedProducts = orderedProducts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "\n"+"Order" +getId()+"{" +
                date +
                ", products=" + orderedProducts +
                '}';
    }
}
