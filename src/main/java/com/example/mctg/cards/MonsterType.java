package com.example.mctg.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
@AllArgsConstructor
public enum MonsterType {
    GOBLIN      ("Goblin"),
    DRAGON      ("Dragon"),
    WIZARD      ("Wizard"),
    ORK         ("Ork"),
    KNIGHT      ("Knight"),
    KRAKEN      ("Kraken"),
    FIRE_ELF    ("Fire Elf");

    private final String name;


    private static final List<MonsterType> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static MonsterType randomMonsterType(){
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public static MonsterType find(String name) {
        for (MonsterType type : listOfNames) {
            String typeName = type.getName();
            if (Objects.equals(typeName, name)) {
                return type;
            }
        }
        return null;
    }

}
