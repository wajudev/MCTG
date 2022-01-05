package com.example.mctg.cards;

import com.example.mctg.database.DatabaseService;

import java.sql.*;


public class CardService {
    private static CardService instance;

    private CardService(){}

    public static CardService getInstance() {
        if (CardService.instance == null){
            CardService.instance = new CardService();
        }
        return CardService.instance;

    }

    public Card insertCard(Card card){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cards(name, card_type, monster_type, element_type, damage, user_id, package_id) VALUES (?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, card.getName());
            preparedStatement.setString(2, String.valueOf(card.getCardType()));
            preparedStatement.setString(3, String.valueOf(card.getMonsterType()));
            preparedStatement.setString(4, String.valueOf(card.getElementType()));
            preparedStatement.setFloat(5, card.getDamage());
            preparedStatement.setNull(6, Types.NULL);
            preparedStatement.setNull(7, Types.NULL);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0){
                return null;
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return this.getCard(generatedKeys.getInt(1));
                }
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }

    public Card getCard(int id){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cards WHERE id=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                  Card card =  Card.buildCard(
                        resultSet.getInt(1),
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
}
