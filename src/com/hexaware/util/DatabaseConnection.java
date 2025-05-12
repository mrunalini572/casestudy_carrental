package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL, USER, and PASSWORD based on your data
    private static final String URL = "jdbc:mysql://localhost:3306/VehicleRentalDB";  // Update database name if needed
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "Mrunalini";  // Your MySQL password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";  // MySQL driver

    static {
        try {
            // Register the MySQL JDBC driver
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Return a connection using the provided details
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

