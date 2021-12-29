package com.example.mctg.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardType {
    MONSTER("Monster"),
    SPELL("Spell");

    private final String name;
}
