package com.example.mctg.battle;

import com.example.mctg.cards.Deck;
import com.example.mctg.cards.Stack;
import com.example.mctg.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    User playerOne = User.builder()
                    .username("playerOne")
                    .password("")
                    .token("")
                    .bio(":/")
                    .image(":/")
                    .coins(20)
                    .elo(100).stack(new Stack())
                    .deck(new Deck()).isAdmin(false).build();

    User playerTwo = User.builder()
                    .username("playerTwo")
                    .password("")
                    .token("")
                    .bio(":/")
                    .image(":/")
                    .coins(20)
                    .elo(100).stack(new Stack())
                     .deck(new Deck()).isAdmin(false).build();

    Battle battle = new Battle(playerOne, playerTwo);
    @BeforeEach
    void setUp() {
        playerOne.buyPackage();
        playerTwo.buyPackage();

        playerOne.buyPackage();
        playerTwo.buyPackage();

        playerOne.prepareDeck();
        playerTwo.prepareDeck();
    }

    @Test
    @DisplayName("Check for winner with deck size")
    void checkForWinnerAndLoser(){
        assertNull(battle.checkWinner(playerOne, playerTwo));
        playerOne.getDeck().cleardeckList();
        Assertions.assertEquals(0, playerOne.getDeck().getDeckList().size());
        Assertions.assertEquals(4, playerTwo.getDeck().getDeckList().size());
        Assertions.assertEquals(playerTwo, battle.checkWinner(playerOne, playerTwo));
        Assertions.assertEquals(playerTwo, battle.getWinner());
        Assertions.assertEquals(playerOne, battle.getLoser());
    }

    @Test
    @DisplayName("Check for winner with deck size")
    void testSwapperMethod(){
        assertNull(battle.fightRound(playerOne, playerTwo));
        User attacker = battle.getPlayerOne();
        User defender = battle.getPlayerTwo();
        battle.swapper(battle.getPlayerOne(), battle.getPlayerTwo());
        Assertions.assertEquals(attacker, battle.getPlayerTwo());
        Assertions.assertEquals(defender, battle.getPlayerOne());
    }
}