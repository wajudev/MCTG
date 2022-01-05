package com.example.mctg.cards;

import lombok.*;

@NoArgsConstructor
public class SpellCard extends Card{
    @Getter
    CardType cardType = CardType.SPELL;

    @Builder
    public SpellCard(int id, String name, ElementType elementType, float damage, boolean inDeck){
        super(id, name, null, elementType, damage, inDeck);
        this.cardType = CardType.SPELL;
    }
}
