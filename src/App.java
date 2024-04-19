import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import interfaces.MainInterface;

public class App {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to Oracle Database
            String dbURL = "jdbc:oracle:thin:@//db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk";
            String username = "h073";
            String password = "Gharbupp";
            conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Connected to the Oracle database successfully!");
                MainInterface.main(null);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    // Close Connection
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}