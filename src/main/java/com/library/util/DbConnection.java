package com.library.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class DbConnection {


    private static final Logger logger = LoggerFactory.getLogger(DbConnection.class);

    public static Connection getConnection() throws SQLException {

        // Load .env file directly
        String envFilePath = System.getProperty("user.dir") + "/.env";

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(envFilePath)) {
            props.load(fis);
        } catch (IOException e) {
            logger.warn("Error loading .env file: " + e.getMessage());
        }


        String url = props.getProperty("URL");
        String user = props.getProperty("USERNAME");
        String password = props.getProperty("PASSWORD");


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Database connection error", e);
        }
    }
}
