package com.exam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class providing static database connection factory.
 * Database URL, Username, and Password are configured here in one central location.
 */
public class DBConnection {

    // =========================================================================
    // DATABASE CONFIGURATION SETTINGS
    // Modify the URL, USERNAME, and PASSWORD below to match your MySQL setup.
    // =========================================================================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "akhil@010306"; // User MySQL root password

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Load MySQL JDBC Driver class
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Private constructor to prevent instantiation
    private DBConnection() {
    }

    /**
     * Obtains a new database connection.
     * Always close this connection after use (preferably using try-with-resources).
     *
     * @return Connection object to online_exam_db
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
