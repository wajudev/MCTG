package com.example.mctg.cards;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Getter
public class Packages {

    private final int price = 5;
    private List<Card> cards;

    public Packages(){
        Random random = new Random();
        Card temp;
        this.cards = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(random.nextInt(10) % 2 == 0) {
                temp = MonsterCard.builder()
                        .monsterType(MonsterType.randomMonsterType())
                        .elementType(ElementType.randomElement())
                        .build();
            } else {
                temp = SpellCard.builder()
                        .elementType(ElementType.randomElement())
                        .build();
            }
            this.cards.add(temp);
        }
    }

    public Packages(List<Card> pack){
        this.cards = new ArrayList<>();

        pack.forEach((temp) -> {
            if(temp.getMonsterType() != null){
                temp = MonsterCard.builder()
                        .monsterType(MonsterType.randomMonsterType())
                        .elementType(ElementType.randomElement())
                        .build();
            } else {
                temp  = SpellCard.builder()
                        .elementType(ElementType.randomElement())
                        .build();
            }
            this.cards.add(temp);
        });
    }


}
