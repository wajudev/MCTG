package com.example.mctg.cards;

import lombok.*;

@NoArgsConstructor
public class SpellCard extends Card{

    CardType cardType;

    @Builder
    public SpellCard(int id, String name, CardType cardType, ElementType elementType, float damage, boolean inDeck, int owner){
        super(id, name, cardType, null, elementType, damage, inDeck, owner);
        this.cardType = CardType.SPELL;
    }
}
