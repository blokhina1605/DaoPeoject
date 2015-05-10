package dao;

import entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecka on 10.05.2015.
 */
public class ProductDao extends AbstractDao<Integer, Product> {
    public static final String SQL_SELECT_ALL_PRODUCTS = "SELECT * FROM product";
    public static final String SQL_SELECT_PRODUCT_BY_ID = "SELECT * FROM product WHERE product_id = ?";
    private Connection cn = null;
    private Statement st = null;

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            cn = ConnectorDB.getConnection();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_PRODUCTS);
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                products.add(new Product(id, name, desc, price));
            }

        } catch (SQLException e) {
            System.err.println("SQL Exeption (request or table failed):" + e);
        }
        return products;
    }

    @Override
    public Product findEntityById(Integer id) {
        PreparedStatement pst;
        Product product = null;
        try {
            cn = ConnectorDB.getConnection();
            pst = cn.prepareStatement(SQL_SELECT_PRODUCT_BY_ID);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                product = new Product(id, name, desc, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(Product entity) {
        return false;
    }

    @Override
    public boolean create(Product entity) {
        return false;
    }

    @Override
    public Product update(Product entity) {
        return null;
    }
}
