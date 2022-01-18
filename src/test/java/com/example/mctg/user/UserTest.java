package com.example.mctg.user;

import com.example.mctg.cards.Deck;
import com.example.mctg.cards.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserTest {
    User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .username("madmax")
                .password("password")
                .token("madmax" + "-mtcgToken")
                .bio(":/")
                .image(":/")
                .coins(20)
                .elo(100)
                .stack(new Stack())
                .deck(new Deck())
                .isAdmin(false)
                .build();

        user.buyPackage();
    }

    @Test
    @DisplayName("ELO update")
    void calculateElo() {
        user.eloIncrease();
        user.eloDecrease();
        Assertions.assertAll("mockUser", () ->  Assertions.assertEquals(98, user.getElo()));
    }

    @Test
    @DisplayName("Buy Package")
    void addPackageToStack() {
        Assertions.assertAll("mockUser", () ->  Assertions.assertEquals(5, user.getStack().getStackList().size()),
                () ->  Assertions.assertEquals(15, user.getCoins()));
    }

    @Test
    @DisplayName("Create new Deck")
    void createNewDeck() {
        user.prepareDeck();
        Assertions.assertEquals(4, user.getDeck().getDeckList().size());
        Assertions.assertEquals(1, user.getStack().getStackList().size());
    }


}