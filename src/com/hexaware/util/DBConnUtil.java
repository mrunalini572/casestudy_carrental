package com.hexaware.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnUtil {

    // This method gets a connection to the database using DBUtil
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Correctly calling DBUtil.getConnection() to get the database connection
            connection = DBUtil.getConnection();
            if (connection != null) {
                System.out.println("Connection established successfully!");
            } else {
                System.out.println("Failed to establish a connection.");
            }
        } catch (SQLException e) {
            // Handle SQLException
            System.out.println("Error while getting database connection: " + e.getMessage());
        }
        return connection;
    }

    // This method will close the database connection safely
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            // Handle SQLException when closing the connection
            System.out.println("Error while closing database connection: " + e.getMessage());
        }
    }
}

