package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    static {
        try {
            // Load the database driver from properties
            Class.forName(DBPropertyUtil.getDbDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                DBPropertyUtil.getDbUrl(), 
                DBPropertyUtil.getDbUser(), 
                DBPropertyUtil.getDbPassword());
        } catch (SQLException e) {
            throw new SQLException("Unable to connect to the database", e);
        }
    }
}
