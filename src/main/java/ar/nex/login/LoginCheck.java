package ar.nex.login;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Renzo
 */
public class LoginCheck {

    private final static String URL = "jdbc:mysql://localhost:3306/sae_app";
    private final static String USER = "root";
    private final static String PASSWORD = "";

    public void LoginCheck() throws SQLException {
        Connection c = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver found");
            System.out.println("Connecting...");
            c = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Cannot connect the database");   
        } finally {    
            if (c != null) {
                c.close();
            }
        }
    }

}
