package com.example.mctg.trade;

import com.example.mctg.cards.CardService;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.user.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeService {
    private static TradeService instance;
    private final CardService cardInstance;
    private final UserService userInstance;

    public static TradeService getInstance() {
        if (TradeService.instance == null) {
            TradeService.instance = new TradeService();
        }
        return TradeService.instance;
    }

    private TradeService() {
        cardInstance = CardService.getInstance();
        userInstance = UserService.getInstance();
    }

    public Trade getTradeByCardId(String cardId){
        Trade trade = null;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trades WHERE card_id = ?;");
            preparedStatement.setString(1,cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null || !resultSet.next()){
                System.out.println("Card not found");
                return null;
            }
            trade = Trade.builder()
                    .id(resultSet.getString("id"))
                    .isMonster(resultSet.getBoolean("is_monster"))
                    .minDamage(resultSet.getFloat("min_damage"))
                    .userId(resultSet.getInt("user_id"))
                    .cardId(resultSet.getString("card_id"))
                    .build();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return trade;
    }

    public String getCardIdFromTradeId(String tradeId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT card_id FROM trades WHERE id = ?;");
            preparedStatement.setString(1,tradeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            try {
                if(resultSet.next()) {
                    return resultSet.getString("card_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    public int getUserIdFromTradeId(String tradeId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM trades WHERE id = ?;");
            preparedStatement.setString(1,tradeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            try {
                if(resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
            return 0;
        }
        return 0;
    }

    public Trade getTradeByTradeId(String tradeId){
        Trade trade = null;
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trades WHERE id = ?;");
            preparedStatement.setString(1,tradeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null || !resultSet.next()){
                System.out.println("Trade not found");
                return null;
            }
            trade = Trade.builder()
                    .id(resultSet.getString("id"))
                    .isMonster(resultSet.getBoolean("is_monster"))
                    .minDamage(resultSet.getFloat("min_damage"))
                    .userId(resultSet.getInt("user_id"))
                    .cardId(resultSet.getString("card_id"))
                    .build();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return trade;
    }

    public List<String> getAllTradesById(){
        List<String> trades = new ArrayList<>();
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT card_id FROM trades;");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                do {
                    trades.add(resultSet.getString("card_id"));
                } while (resultSet.next());
            }
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException exception){
            exception.printStackTrace();
            return trades;
        }
        return trades;
    }

    public boolean insertTrade(Trade trade) throws SQLException {
        int affectedRows = 0;
        Connection connection = DatabaseService.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trades (id, is_monster, min_damage, user_id, card_id) VALUES (?,?,?,?,?);");
        preparedStatement.setString(1, trade.getId());
        preparedStatement.setBoolean(2, trade.isMonster());
        preparedStatement.setFloat(3, trade.getMinDamage());
        preparedStatement.setInt(4, trade.getUserId());
        preparedStatement.setString(5, trade.getCardId());

        affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0){
            connection.commit();
        }
        preparedStatement.close();

        return affectedRows > 0;

    }

    public void deleteTradeByCardId(String cardId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trades WHERE card_id = ?;");
            preparedStatement.setString(1, cardId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0){
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void deleteTrade(String tradeId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trades WHERE id = ?;");
            preparedStatement.setString(1, tradeId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0){
                connection.commit();
            }
            preparedStatement.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }
}
