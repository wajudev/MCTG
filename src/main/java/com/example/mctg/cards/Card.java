package com.example.mctg.cards;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Card {
    private int id;
    private String name;
    private MonsterType monsterType;
    private ElementType elementType;
    private float damage;
    private boolean locked;
    private int owner;


}
