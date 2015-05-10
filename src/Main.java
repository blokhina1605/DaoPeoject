import dao.OrderDao;
import dao.ProductDao;

/**
 * Created by jecka on 10.05.2015.
 */
public class Main {
    public static void main(String[] args) {
        ProductDao product= new ProductDao();
        OrderDao orderDao = new OrderDao();
        System.out.println(orderDao.createOrderConsistProductOfCurrentDate());

    }

}
