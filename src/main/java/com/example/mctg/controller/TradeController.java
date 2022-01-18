package com.example.mctg.controller;

import com.example.mctg.cards.Card;
import com.example.mctg.cards.CardService;
import com.example.mctg.serializer.TradeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.example.mctg.trade.Trade;
import com.example.mctg.trade.TradeService;

import java.sql.SQLException;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TradeController {
    private TradeService tradeService;

    public StringBuilder getTradesSummary(){
        List<String> trades = TradeService.getInstance().getAllTradesById();
        StringBuilder stringBuilder = new StringBuilder();
        Card card;
        int i = 1;
        if (trades != null){
            for (String cardId : trades){
                card = CardService.getInstance().getCard(cardId);
                stringBuilder.append(i++ + "-> ");
                stringBuilder.append(card.getCardStats());
                stringBuilder.append(getRequirementsForTrading(card.getId()));
            }
        } else {
            return null;
        }
        System.out.println(stringBuilder);
        return stringBuilder;
    }

    public String getRequirementsForTrading(String cardId){
        Trade trade = TradeService.getInstance().getTradeByCardId(cardId);
        return trade.tradeSummary();
    }

    public String tradeCards(String tradeId, String offeredCardId, int userId){
        Trade desiredCard = TradeService.getInstance().getTradeByTradeId(tradeId);
        Card offeredCard = CardService.getInstance().getCard(offeredCardId);

        if (desiredCard == null){
            return "This card doesn't exist in the market";
        }
        if (offeredCard == null){
            return "There is no card with the specified id";
        }

        String response = desiredCard.tradeValidator(desiredCard,offeredCard, userId);

        if(response != null) {
            return response;
        }

        CardService.getInstance().addCardToNewUser(TradeService.getInstance().getCardIdFromTradeId(tradeId), userId);
        CardService.getInstance().addCardToNewUser(offeredCardId, TradeService.getInstance().getUserIdFromTradeId(tradeId));
        TradeService.getInstance().deleteTrade(tradeId);

        return null;
    }

    public String addNewTrade(String json, int userId) throws JsonProcessingException, SQLException {
        TradeData tradeSerialized = getParsedTrade(json);
        boolean isMonster = (!tradeSerialized.getType().equals("spell"));

        Trade trade = Trade.builder()
                .id(tradeSerialized.getId())
                .isMonster(isMonster)
                .minDamage(tradeSerialized.getMinimumDamage())
                .cardId(tradeSerialized.getCardToTrade())
                .userId(userId)
                .build();

        if (CardService.getInstance().getCard(tradeSerialized.getCardToTrade()).getUserId() != userId){
            return "You don't own the card you want to trade";
        }

        if (TradeService.getInstance().getTradeByCardId(trade.getCardId()) != null){
            return "This card is already available for trading";
        }
        if (TradeService.getInstance().insertTrade(trade)){
            return null;
        } else {
            return "Card couldn't be added";
        }
    }

    public String payForTrade(String tradeId, int userId, int coins){
        if (coins > 5){
            return "Not enough money to make this trade";
        }
        Trade trade = TradeService.getInstance().getTradeByTradeId(tradeId);

        if (trade == null){
            return "Wanted card doesn't exist on the trading market";
        }

        CardService.getInstance().addCardToNewUser(tradeId, userId);
        TradeService.getInstance().deleteTrade(tradeId);

        return null;
    }

    public String deleteTradedCard(String tradeId){
        Trade tradedCard = TradeService.getInstance().getTradeByTradeId(tradeId);
        if (tradedCard == null){
            return "Trade with specified id doesn't exist ";
        }

        TradeService.getInstance().deleteTrade(tradeId);

        return null;
    }

    public TradeData getParsedTrade(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, TradeData.class);
    }
}
