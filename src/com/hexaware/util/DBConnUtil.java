package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/VehicleRentalDB";  // Update database name if needed
    private static final String USER = "root";  // MySQL username
    private static final String PASSWORD = "Mrunalini";  // MySQL password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";  // MySQL JDBC driver

    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    /**
     * Establishes and returns a connection to the database.
     * 
     * @return Connection object
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Closes the provided database connection.
     * 
     * @param conn the Connection to be closed
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to close a connection in a finally block, to ensure proper resource management.
     * 
     * @param conn the connection to close
     */
    public static void closeQuietly(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // Log or ignore
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
