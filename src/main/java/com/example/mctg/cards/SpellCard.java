package com.example.mctg.cards;

import lombok.*;

@NoArgsConstructor
public class SpellCard extends Card{
    @Getter
    CardType cardType = CardType.SPELL;

    @Builder
    public SpellCard(String id, String name, ElementType elementType, float damage, boolean inDeck, int userId){
        super(id, name, null, elementType, damage, inDeck, userId);
        this.cardType = CardType.SPELL;
    }
}
