package com.example.mctg.user;

import com.example.mctg.database.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements UserServiceInterface {
    private static UserService instance;

    private UserService(){

    }

    public static UserService getInstance(){
        if (UserService.instance == null){
            UserService.instance = new UserService();
        }
        return UserService.instance;
    }

    @Override
    public UserInterface addUser(UserInterface user) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username, status, password, token, coins, elo) VALUES (?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getStatus());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getToken());
            preparedStatement.setInt(5, user.getCoins());
            preparedStatement.setInt(6, user.getElo());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                return null;
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()){
                    return this.getUser(generatedKeys.getInt(1));
                }
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException ignored){

        }
        return null;
    }

    @Override
    public UserInterface getUser(int id) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, username, status, password, token, coins, elo FROM users WHERE id=?;");
            preparedStatement.setInt(id, 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                UserInterface user = User.builder()
                        .id(resultSet.getInt(1))
                        .username(resultSet.getString(2))
                        .status(resultSet.getString(3))
                        .password(resultSet.getString(4))
                        .token(resultSet.getString(5))
                        .coins(resultSet.getInt(6))
                        .elo(resultSet.getInt(7))
                        .build();
                resultSet.close();
                preparedStatement.close();
                connection.close();

                return user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public UserInterface updateUser(int id, UserInterface user) {
        User toBeUpdatedUser = (User) this.getUser(id);
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET username = ?, password = ?, token = ?, status = ?, coins = ?, elo = ? WHERE id = ?;");

            preparedStatement.setString(1, user.getUsername() != null ? user.getUsername() : toBeUpdatedUser.getUsername());
            preparedStatement.setString(2, user.getPassword() != null ? user.getPassword() : toBeUpdatedUser.getPassword());
            preparedStatement.setString(3, user.getToken() != null ? user.getToken() : toBeUpdatedUser.getToken());
            preparedStatement.setString(4, user.getStatus() != null ? user.getStatus() : toBeUpdatedUser.getStatus());
            preparedStatement.setInt(5, user.getCoins());
            preparedStatement.setInt(6, user.getCoins());
            preparedStatement.setInt(5, id);

            int affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            if (affectedRows == 0){
                return null;
            }

            return this.getUser(id);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserInterface getUserWithoutSensibleData(int id) {
        if (id == 0) {
            return null;
        }
        return ((User) this.getUser(id)).toBuilder().password(null).token(null).build();
    }

    @Override
    public UserInterface getUserByUsername(String username){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, username, status, password, token, coins, elo FROM users WHERE id=?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                UserInterface user = User.builder()
                        .id(resultSet.getInt(1))
                        .username(resultSet.getString(2))
                        .status(resultSet.getString(3))
                        .password(resultSet.getString(4))
                        .token(resultSet.getString(5))
                        .coins(resultSet.getInt(6))
                        .elo(resultSet.getInt(7))
                        .build();
                resultSet.close();
                preparedStatement.close();
                connection.close();

                return user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public UserInterface getUserByUsernameWithoutSensibleData(String username) {
        if (username == null) {
            return null;
        }
        return ((User) this.getUserByUsername(username)).toBuilder().password(null).token(null).build();
    }

    @Override
    public List<UserInterface> getUsers() {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, username, status, password, token, coins, elo FROM users");

            List<UserInterface> users = new ArrayList<>();
            while (resultSet.next()){
                users.add(User.builder()
                        .id(resultSet.getInt(1))
                        .username(resultSet.getString(2))
                        .status(resultSet.getString(3))
                        .password(resultSet.getString(4))
                        .token(resultSet.getString(5))
                        .coins(resultSet.getInt(6))
                        .elo(resultSet.getInt(7))
                        .build());
                resultSet.close();
                statement.close();
                connection.close();

                return users;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?;");
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            if (affectedRows == 0) {
                return false;
            }

            preparedStatement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
