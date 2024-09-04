package ASimulatorSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {
    Connection conn;
    Statement s;

    public Conn() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankmanagementsystem", "root", "Litesh24");
            // Create a statement
            s = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // Method to get a PreparedStatement
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    // Method to close resources
    public void close() {
        try {
            if (s != null) s.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing database resources: " + e.getMessage());
        }
    }
}
