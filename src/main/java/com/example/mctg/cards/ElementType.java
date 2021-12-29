package com.example.mctg.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter
public enum ElementType {
    FIRE("Fire"),
    WATER("Water"),
    NORMAL("Normal");

    private final String elementName;

    private static final List<ElementType> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static ElementType randomElement()  {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public static ElementType find(String name) {
        for (ElementType type : listOfNames) {
            if (type.getElementName().contains(name)) {
                return type;
            }
        }
        return NORMAL;
    }

}
