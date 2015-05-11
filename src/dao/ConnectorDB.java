package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by jecka on 10.05.2015.
 */
public class ConnectorDB {
    public static Connection getConnection() throws SQLException {
        ResourceBundle resource= ResourceBundle.getBundle("database");
        String url= resource.getString("db.url");
        String user=resource.getString("db.user");
        String pass=resource.getString("db.password");
        return DriverManager.getConnection(url, user, pass);
    }

}
