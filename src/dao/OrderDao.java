package dao;

import entity.Order;
import entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jecka on 10.05.2015.
 */
public class OrderDao extends AbstractDao<Integer, Order> {
    private Connection cn = null;
    // private Statement st = null;
    private PreparedStatement pst = null;
    Map<Product, Integer> orderedProducts;
    public static final String SQL_SELECT_ALL_ORDERS = "SELECT * FROM `order`";
    public static final String SQL_SELECT_ORDER_BY_ID = "SELECT * FROM order WHERE order_id = ?";
    public static final String SQL_SELECT_RPODUCTS_BY_ORDER = "SELECT order.order_id,order.date, " +
            "product.name, product.description, product.price, product.product_id, ordered_products.quantity " +
            "FROM `order` " +
            "INNER JOIN ordered_products USING(order_id) " +
            "INNER JOIN product USING(product_id) " +
            "WHERE order_id = ?";
    public static final String SQL_SELECT_ORDERS_WITH_CURRENT_SUM_AND_PRODUCTS = "SELECT COUNT(ordered_products.product_id) AS q, order.order_id, sum(price*quantity) " +
            "FROM `order` " +
            "INNER JOIN ordered_products USING(order_id) " +
            "INNER JOIN product USING(product_id) " +
            "GROUP BY ordered_products.order_id " +
            "HAVING (q = ?) and (sum(price*quantity) < ?)";
    public static final String SQL_SELECT_ORDERS_WITH_CURRENT_PRODUCT = "SELECT order.order_id FROM `order` \n" +
            "INNER JOIN ordered_products USING(order_id) \n" +
            "INNER JOIN product USING(product_id) \n" +
            "WHERE (product_id = ?)\n" +
            "GROUP BY ordered_products.order_id";
    public static final String SQL_SELECT_ORDERS_WITHOUT_CURRENT_PRODUCT_AND_CUR_DATE = "SELECT order.order_id FROM `order` INNER JOIN ordered_products USING(order_id)\n" +
            "INNER JOIN product USING(product_id)\n" +
            "WHERE (product_id != ?)\n" +
            "AND DATE(date) = DATE(NOW())\n" +
            "GROUP BY ordered_products.order_id";
    public static final String SQL_SELECT_ORDERS_WITH_CURRENT_DATE = "SELECT product.name, product.description, product.price, product.product_id, ordered_products.quantity " +
            "FROM `order` INNER JOIN ordered_products USING(order_id)\n" +
            "INNER JOIN product USING(product_id)\n" +
            "WHERE  DATE(date) = DATE(NOW())\n" +
            "GROUP BY ordered_products.order_id";
    public static final String SQL_INSERT_ORDER = "INSERT INTO `order`(`order_id`, `date`) VALUES (null,curdate())";
    public static final String SQL_SELECT_LAST_INSERT_ORDER = "SELECT MAX( order_id ) AS last FROM  `order`";
    public static final String SQL_INSERT_ORDERED_PRODUCTS = "INSERT INTO `ordered_products`(`id`, `order_id`, `product_id`, `quantity`) " +
            "VALUES (null, ?, ?, ?)";

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_ALL_ORDERS);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("order_id");
                Date date = rs.getDate("date");
                orders.add(new Order(id, date, findProductsByOrderID(id)));
            }

        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return orders;
    }

    public Map findProductsByOrderID(int id) {
        orderedProducts = new HashMap<Product, Integer>();
        Product product = null;
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_RPODUCTS_BY_ORDER);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                product = new Product(product_id, name, desc, price);
                int quantity = rs.getInt("quantity");
                orderedProducts.put(product, quantity);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return orderedProducts;
    }

    @Override
    public Order findEntityById(Integer id) {
        Order order = null;
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_ALL_ORDERS);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                order = new Order(id, date, findProductsByOrderID(id));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return order;
    }


    public List findOrdersWithCurrentSumAndProducts(double sum, int quantityOfProducts) {
        List<Integer> orders = new ArrayList<>();

        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_ORDERS_WITH_CURRENT_SUM_AND_PRODUCTS);
            pst.setInt(1, quantityOfProducts);
            pst.setDouble(2, sum);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(rs.getInt("order_id"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return orders;
    }

    public List findOrdersWithCurrentProduct(int productId) {
        List<Integer> orders = new ArrayList<>();
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_ORDERS_WITH_CURRENT_PRODUCT);
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(rs.getInt("order_id"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return orders;
    }

    public List findOrdersWithoutCurrentProductAndCurrentDate(int productId) {
        List<Integer> orders = new ArrayList<>();
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_ORDERS_WITHOUT_CURRENT_PRODUCT_AND_CUR_DATE);
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(rs.getInt("order_id"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return orders;
    }

    public boolean createOrderConsistProductOfCurrentDate() {
        try {
            cn = ConnectorDB.getConnection();
            Map<Product, Integer> productList = getProductsWithCurrentDate(cn);

            pst = cn.prepareStatement(SQL_INSERT_ORDER);
            pst.executeQuery();

            pst = cn.prepareStatement(SQL_SELECT_LAST_INSERT_ORDER);
            int lastOrderId = pst.executeQuery().getInt("last");

            pst = cn.prepareStatement(SQL_INSERT_ORDERED_PRODUCTS);
            for (Product product : productList.keySet()) {
                int quantity = productList.get(product);
                pst.setInt(1, lastOrderId);
                pst.setInt(2, product.getId());
                pst.setInt(3, quantity);
                pst.executeQuery();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return false;
    }

    private Map getProductsWithCurrentDate(Connection con) throws SQLException {
        Map<Product, Integer> productList = new HashMap<>();
        pst = cn.prepareStatement(SQL_SELECT_ORDERS_WITH_CURRENT_DATE);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            int product_id = rs.getInt("product_id");
            String desc = rs.getString("description");
            double price = rs.getDouble("price");
            productList.put(new Product(product_id, name, desc, price), rs.getInt("quantity"));
        }
        return productList;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(Order entity) {
        return false;
    }

    @Override
    public boolean create(Order entity) {
        return false;
    }

    @Override
    public Order update(Order entity) {
        return null;
    }
}
