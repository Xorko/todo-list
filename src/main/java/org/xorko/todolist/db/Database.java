package org.xorko.todolist.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connection;

    /**
     * Open a connection to the database
     */
    public static void connect() {
        try {
            String url = "jdbc:sqlite:src/main/resources/database.db";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database
     */
    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }
}
