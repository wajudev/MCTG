package com.example.mctg.cards;

import com.example.mctg.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Deck {
    private final static int DECKSIZE = 4;
    private List<Card> deckList;
    Random RANDOM = new Random();

    public Deck() {
        this.deckList = new ArrayList<>();
    }

    public Deck(User player) {
        this.deckList = new ArrayList<>();
        for (int i = 0; i < DECKSIZE; i++) {
            this.deckList.add( player.getStack().randomCard() );
        }
    }

    public Card randomCard() {
        int position = RANDOM.nextInt(this.deckList.size());
        Card droppedCard = this.deckList.get(position);
        this.deckList.remove(position);
        return droppedCard;
    }

    public void cleardeckList() {
        this.deckList.clear();
    }
}
