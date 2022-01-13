package com.example.mctg.cards;

import com.example.mctg.database.DatabaseService;
import com.example.mctg.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CardService {
    private static CardService instance;

    private CardService(){}

    public static CardService getInstance() {
        if (CardService.instance == null){
            CardService.instance = new CardService();
        }
        return CardService.instance;

    }

    public Card getCard(String id){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cards WHERE id=?;");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Card card =  Card.buildCard(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getFloat(6),
                        resultSet.getBoolean(7));

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return card;
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public boolean insertCardForPackages(Card card, int packageId){
        int affectedRows = 0;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cards(id, name, card_type, monster_type, element_type, damage, user_id, package_id) VALUES (?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setString(3, String.valueOf(card.getCardType()));
            preparedStatement.setString(4, String.valueOf(card.getMonsterType()));
            preparedStatement.setString(5, String.valueOf(card.getElementType()));
            preparedStatement.setFloat(6, card.getDamage());
            preparedStatement.setNull(7, Types.NULL);
            preparedStatement.setInt(8, packageId);


            affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                connection.commit();
            }

            preparedStatement.close();
            //connection.close();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return affectedRows > 0;

    }


    public boolean insertCard(Card card){
        int affectedRows = 0;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cards(id, name, card_type, monster_type, element_type, damage, user_id, package_id) VALUES (?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setString(3, String.valueOf(card.getCardType()));
            preparedStatement.setString(4, String.valueOf(card.getMonsterType()));
            preparedStatement.setString(5, String.valueOf(card.getElementType()));
            preparedStatement.setFloat(6, card.getDamage());
            preparedStatement.setNull(7, Types.NULL);
            preparedStatement.setNull(8, Types.NULL);


            affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                connection.commit();
            }

            preparedStatement.close();
            //connection.close();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return affectedRows > 0;
    }

    public List<Card> getAllCards(){
        try{
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, card_type, monster_type, element_type, damage, is_locked FROM cards;");
            return getCardList(preparedStatement);
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    private List<Card> getCardList(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Card> cards = new ArrayList<>();
        while (resultSet.next()){
            cards.add(Card.buildCard(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getFloat(6),
                    resultSet.getBoolean(7)
            ));
        }
        resultSet.close();
        preparedStatement.close();

        return cards;
    }


    public List<Card> getCardsByUser(User user){
        try{
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, card_type, monster_type, element_type, damage, is_locked FROM cards WHERE user_id=?;");
            preparedStatement.setInt(1, user.getId());
            return getCardList(preparedStatement);
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public List<Card> getCardsForPackages(int packageId){
        try{
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, card_type, monster_type, element_type, damage, is_locked FROM cards WHERE package_id=?;");
            preparedStatement.setInt(1, packageId);
            return getCardList(preparedStatement);
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public void addCardToUser(Card card, User user){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET package_id = NULL, user_id = ? WHERE \"id\" = ?;");
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, card.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxPackageId() {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT max(package_id) as max_id FROM cards");
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if(resultSet.next()) {
                    return resultSet.getInt("max_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (RuntimeException | SQLException e) {
            System.out.println("Get max packageId exception");
            return 0;
        }
        return 0;
    }

    public int getRandomPackage(){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT package_id FROM cards OFFSET floor(random() * (SELECT COUNT(package_id) FROM cards));");
            ResultSet resultSet = preparedStatement.executeQuery();

            try {
                if(resultSet.next()) {
                    return resultSet.getInt("package_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            resultSet.close();
            preparedStatement.close();


        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return 0;
    }

    public void addPackageToUser(int userId, int packageId) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET user_id = ?, package_id = null WHERE package_id = ?;");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, packageId);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                connection.commit();
            }

           preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addCardToPackage(String id, int packageId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET package_id = ? WHERE \"id\" = ?;");
            preparedStatement.setInt(1, packageId);
            preparedStatement.setString(2, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void lockCard(Card card, boolean isLocked) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET is_locked = ? WHERE id = ?;");
            preparedStatement.setBoolean(1, isLocked);
            preparedStatement.setString(2, card.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromDeck(int userId) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET deck = false WHERE \"id\" = ?;");
            preparedStatement.setInt(1, userId);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                connection.commit();
            }

            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean addToDeck(String id, int userId) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cards SET deck = true WHERE \"id\" = ? AND user_id = ?;");

            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, userId);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                connection.commit();
            }

            connection.commit();
            preparedStatement.close();
            return (rows > 0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public List<Card> getCardsInDeck(int userId) {
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cards WHERE user_id = ? AND deck = true;");
            preparedStatement.setInt(1, userId);
            return getCardList(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public boolean deleteCard(String id){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM cards WHERE id=?;");
            preparedStatement.setString(1,id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
            }

            preparedStatement.close();


            return true;
        } catch (SQLException exception){
            exception.printStackTrace();
            return false;
        }
    }
}
