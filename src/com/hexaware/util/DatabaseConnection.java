package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/VehicleRentalDB"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "Mrunalini"; 
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Load the JDBC driver
            return DriverManager.getConnection(URL, USER, PASSWORD);  // Establish the connection
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found.", e);
        }
    }
}
