package com.example.mctg.database;

import com.example.mctg.user.User;

import java.sql.*;

public class DatabaseService {
    private static DatabaseService instance;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/monstercardgame";
    private static final String USER = "waju";
    private static final String PASSWORD = "";

    private DatabaseService() {
    }

    public static DatabaseService getInstance() {
        if (DatabaseService.instance == null) {
            DatabaseService.instance = new DatabaseService();
        }
        return DatabaseService.instance;
    }

    public Connection getConnection() {
       try {
           Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           connection.setAutoCommit(false);
           return connection;
       } catch (SQLException e){
           System.err.println( e.getClass().getName()+": "+ e.getMessage() );
           System.exit(0);
           return null;
       }
    }
}
