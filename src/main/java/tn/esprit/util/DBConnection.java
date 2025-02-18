package tn.esprit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/commande_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    private DBConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database Connection Failed");
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            new DBConnection();
        }
        return connection;
    }
}