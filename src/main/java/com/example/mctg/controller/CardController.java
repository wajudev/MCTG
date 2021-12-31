package com.example.mctg.controller;

import com.example.mctg.cards.*;
import com.example.mctg.database.DatabaseService;

public class CardController {
    private DatabaseService databaseService;

    public static Card buildCard(int id, String name, String cardTypeString, String monsterTypeString, String elementTypeString, float damage, boolean locked){
        CardType cardType;
        MonsterType monsterType;
        ElementType elementType;
        Card card;

        try {
            cardType = CardType.valueOf(cardTypeString);
        } catch (IllegalArgumentException e){
            cardType = CardType.MONSTER;
        }

        try {
            monsterType = MonsterType.valueOf(elementTypeString);
        } catch (IllegalArgumentException e) {
            monsterType = MonsterType.randomMonsterType();
        }

        try {
            elementType = ElementType.valueOf(elementTypeString);
        } catch (IllegalArgumentException e) {
            elementType = ElementType.randomElement();
        }

        if (CardType.MONSTER.equals(cardType)){
            // Create Monster Card
            card = MonsterCard.builder()
                    .id(id)
                    .name(name)
                    .monsterType(monsterType)
                    .elementType(elementType)
                    .damage(damage)
                    .inDeck(locked)
                    .build();

        } else {
            // Otherwise create Spell Card
            card = SpellCard.builder()
                    .id(id)
                    .name(name)
                    .elementType(elementType)
                    .damage(damage)
                    .inDeck(locked)
                    .build();
        }

        return card;
    }
}
