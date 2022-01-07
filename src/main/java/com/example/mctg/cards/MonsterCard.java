package com.example.mctg.cards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MonsterCard extends Card{
    @Getter
    CardType cardType = CardType.MONSTER;

    @Builder
    public MonsterCard(String id, String name, MonsterType monsterType, ElementType elementType, float damage, boolean inDeck, int userId) {
        super(id, name, monsterType, elementType, damage, inDeck, userId);
        this.cardType = CardType.MONSTER;
    }
}
