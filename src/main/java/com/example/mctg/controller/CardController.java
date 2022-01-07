package com.example.mctg.controller;

import com.example.mctg.cards.*;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.serializer.CardData;
import com.example.mctg.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class CardController {
    private DatabaseService databaseService;

    // Json to Card
    public CardData[] deserializer(String cardsArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper.readValue(cardsArray, CardData[].class);
    }

    public boolean insertJSONCards(String cardsArray, User user) throws JsonProcessingException {
        CardData[] listCustomCard = deserializer(cardsArray);

        for(CardData temp: listCustomCard) {
            Card card = buildCard(temp, user.getId(), false);
            if (CardService.getInstance().insertCard(card)) {
                return false;
            }
        }
        return true;
    }

    public String getCardsListJson(List<Card> cardList) throws JsonProcessingException {
        StringBuilder st = new StringBuilder();
        st.append("[");
        for(Card card: cardList) {
            st.append(serializer(card));
            st.append(",");
        }
        st.deleteCharAt(st.length()-1);
        st.append("]");

        return String.valueOf(st);
    }

    // Card to Json
    public String serializer(Card card) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("id", card.getId());
        map.put("name", card.getName());

        String monsterType = (card instanceof MonsterCard) ? card.getMonsterType().getName() : "";
        map.put("monsterType", monsterType);

        map.put("element", String.valueOf(card.getElementType().getElementName()));

        map.put("damage", String.valueOf(card.getDamage()));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }


    public Card buildCard(CardData cardData, int userId, boolean inDeck) {
        Card card;
        if (cardData.getMonsterType() == null || cardData.getMonsterType().isEmpty()) {
            card = new SpellCard(
                    cardData.getId(),
                    cardData.getName(),
                    ElementType.find(cardData.getElementType()),
                    cardData.getDamage(),
                    inDeck,
                    userId);
        } else {
            card = new MonsterCard(
                    cardData.getId(),
                    cardData.getName(),
                    MonsterType.find(cardData.getMonsterType()),
                    ElementType.find(cardData.getElementType()),
                    cardData.getDamage(),
                    inDeck,
                    userId);
        }
        return card;
    }

    public StringBuilder getCardListStats(List<Card> cardList, String deckName) {
        StringBuilder all = new StringBuilder();

        System.out.println("Your "+ deckName + " Cards: " );
        for (Card temp: cardList) {
            all.append(temp.getCardStats());
        }
        System.out.println(all);
        return all;
    }






}
