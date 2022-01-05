package com.example.mctg.cards;

public interface ICard {
    CardType getCardType();

    int getId();

    String getName();

    float getDamage();

    MonsterType getMonsterType();

    ElementType getElementType();

    boolean isLocked();

    boolean defeats(Card card);

    float calculateEffectiveness(Card card);
}
