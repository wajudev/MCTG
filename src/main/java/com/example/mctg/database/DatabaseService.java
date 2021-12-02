package com.example.mctg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService implements DatabaseServiceInterface {
    private static DatabaseService instance;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/monstercardgame";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private DatabaseService() {
    }

    public static DatabaseService getInstance() {
        if (DatabaseService.instance == null) {
            DatabaseService.instance = new DatabaseService();
        }
        return DatabaseService.instance;
    }

    @Override
    public Connection getConnection() {
       try {
           return DriverManager.getConnection(DB_URL, USER, PASSWORD);
       } catch (SQLException e){
           e.printStackTrace();
       }
       return null;
    }
}
