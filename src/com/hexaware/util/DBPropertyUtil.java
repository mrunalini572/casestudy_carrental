package com.hexaware.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {

    public static String getPropertyString(String propertyFileName) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertyFileName)) {
            properties.load(input);
            String hostname = properties.getProperty("hostname");
            String dbname = properties.getProperty("dbname");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");

            // Construct the connection string
            return "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading properties file: " + e.getMessage());
        }
    }
}
