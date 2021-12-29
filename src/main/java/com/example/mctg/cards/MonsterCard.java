package com.example.mctg.cards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MonsterCard extends Card{

    CardType cardType;

    @Builder
    public MonsterCard(int id, String name, CardType cardType, MonsterType monsterType, ElementType elementType, float damage, boolean inDeck, int owner) {
        super(id, name, cardType, monsterType, elementType, damage, inDeck, owner);
        this.cardType = CardType.MONSTER;
    }

}
