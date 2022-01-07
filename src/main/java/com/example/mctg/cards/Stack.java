package com.example.mctg.cards;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Stack {
    List<Card> stackList;
    private static final Random RANDOM = new Random();

    public Stack() {
        this.stackList = new ArrayList<>();
    }

    public Card randomCard() {
        int rand = RANDOM.nextInt(this.stackList.size());
        Card temp = this.stackList.get(rand);
        this.stackList.remove(rand);
        return temp;
    }

    public void addListToStack(List<Card> list) {
        this.stackList.addAll(list);
    }
}
