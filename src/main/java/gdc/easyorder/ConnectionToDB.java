package gdc.easyorder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDB {
    static String user = "root";
//    static String password = "";
    static String password = "135226697";
    static String url = "jdbc:mysql://localhost:3306/gdc";
    static String driver = "com.mysql.cj.jdbc.Driver";
    public static Connection getConnexion() {
        Connection connection = null;
        try {
            Class.forName(driver);
            try {
                connection = DriverManager.getConnection(url,user,password);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}

