package com.example.mctg.user;

import com.example.mctg.cards.Card;
import com.example.mctg.cards.CardService;
import com.example.mctg.cards.Deck;
import com.example.mctg.cards.Stack;
import com.example.mctg.database.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;

    private UserService(){

    }

    public static UserService getInstance(){
        if (UserService.instance == null){
            UserService.instance = new UserService();
        }
        return UserService.instance;
    }

    public boolean addUser(User user) throws SQLException {
        Connection connection = DatabaseService.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username, password, token, coins, elo, admin, total_battles, won_battles,lost_battles, bio, image, drawn_battles) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getToken());
        preparedStatement.setInt(4, user.getCoins());
        preparedStatement.setInt(5, user.getElo());
        preparedStatement.setBoolean(6, user.isAdmin());
        preparedStatement.setInt(7, 0);
        preparedStatement.setInt(8, 0);
        preparedStatement.setInt(9, 0);
        preparedStatement.setString(10, user.getBio());
        preparedStatement.setString(11, user.getImage());
        preparedStatement.setInt(12, 0);


        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0){
            connection.commit();
        }
        preparedStatement.close();
        connection.close();

        return affectedRows > 0;
    }

    public User getUser(String username) {
        User user = null;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if (resultSet.next()) {
                    user =buildUser(resultSet, true);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    public User getUser(int id) {
        User user = null;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id=?;");
            preparedStatement.setInt(id, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if (resultSet.next()) {
                    user =buildUser(resultSet, false);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    public User buildUser(ResultSet resultSet, boolean withPassword) throws SQLException {
        String password;
        if (withPassword) {
            password = resultSet.getString("password");
        } else {
            password = "";
        }
        User user = User.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .password(password)
                .token(resultSet.getString("token"))
                .coins(resultSet.getInt("coins"))
                .elo(resultSet.getInt("elo"))
                .isAdmin(resultSet.getBoolean("admin"))
                .stack(new Stack())
                .deck(new Deck())
                .battlesFought( resultSet.getInt("total_battles"))
                .battlesWon(resultSet.getInt("won_battles"))
                .battlesLost(resultSet.getInt("lost_battles"))
                .battlesDrawn(resultSet.getInt("drawn_battles"))
                .bio(resultSet.getString("bio"))
                .image(resultSet.getString("image"))
                .build();


        List<Card> list = CardService.getInstance().getCardsByUser(user);
        if(!list.isEmpty()) {
            user.getStack().addListToStack(list);
        }
        return user;
    }

    public User getUserByUsername(String username, String password){
        User user = null;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?;");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            try {
                if (resultSet.next()) {
                    user =buildUser(resultSet, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                resultSet.close();
                preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return user;

    }

    public boolean updateUser(int id, User user) {
        int affectedRows = 0;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET username = ?, password = ?, token = ?, bio = ?, image = ? WHERE id = ?;");

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getToken());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, id);

            affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                connection.commit();
            }
            preparedStatement.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    public boolean updateUserStats(User user){
        int affectedRows = 0;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET coins = ?, elo = ?, total_battles = ?, won_battles = ?, lost_battles = ?, drawn_battles = ? WHERE username LIKE ?;");
            preparedStatement.setInt(1, user.getCoins());
            preparedStatement.setInt(2, user.getElo());
            preparedStatement.setInt(3, user.getBattlesFought());
            preparedStatement.setInt(4, user.getBattlesWon());
            preparedStatement.setInt(5, user.getBattlesLost());
            preparedStatement.setInt(6, user.getBattlesDrawn());
            preparedStatement.setString(7, user.getUsername());

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return affectedRows > 0;
    }

    public List<User> getUsersRankedByElo() {
            List<User> users = new ArrayList<>();
            try {
                Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users ORDER BY elo, username;");
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    if (resultSet.next()) {
                        do {
                            users.add(buildUser(resultSet, true));
                        } while (resultSet.next());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

            return users;
    }

    public User getLoggedUser(String token) {
        User user = null;
        try {
            if(isLoggedIn(token)) {
                String[] strs = token.split("-");
                user = getUser(strs[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isLoggedIn(String token) throws SQLException {
        Connection connection = DatabaseService.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sessions WHERE token=?;");
        preparedStatement.setString(1, token);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            return true;
        } else {
            return false;
        }

    }

    public boolean addSession(String token){
        try {
            if(!isLoggedIn(token)) {
                Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sessions (token, \"last_loggedin\") VALUES (?, current_timestamp);");
                preparedStatement.setString(1, token);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    connection.commit();
                }
                preparedStatement.close();
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteSession(String token) {
        int affectedRows = 0;
        try {
            if(isLoggedIn(token)) {
                Connection connection = DatabaseService.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sessions WHERE token LIKE ?;");
                preparedStatement.setString(1, token);
                affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    connection.commit();
                }
                preparedStatement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return affectedRows > 0;
    }

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
