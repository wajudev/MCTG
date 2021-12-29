package com.example.mctg.cards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MonsterCard extends Card{
    @Getter
    CardType cardType = CardType.MONSTER;

    @Builder
    public MonsterCard(int id, String name, MonsterType monsterType, ElementType elementType, float damage, boolean inDeck, int owner) {
        super(id, name, monsterType, elementType, damage, inDeck, owner);
        this.cardType = CardType.MONSTER;
    }

}
