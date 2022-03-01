package com.example.mctg.battle;

import com.example.mctg.cards.Card;
import com.example.mctg.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Battle {
    private User playerOne;
    private User playerTwo;
    private User winner = null;
    private int rounds = 0;
    private List<Card> arena;
    private final int MAX_ROUNDS = 100;
    private User loser = null;

    public Battle(User playerOne, User playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.arena = new ArrayList<>();
    }



    public void startBattle(){
        while (this.winner == null && rounds < MAX_ROUNDS){
            rounds++;
            this.winner = fightRound(this.playerOne, this.playerTwo);
        }
        System.out.println("Game Over");

        if(this.winner != null){
            this.winner.eloIncrease();
            this.loser.eloDecrease();
            this.winner.updateBattlesFoughtAndWon();
            this.loser.updateBattlesFoughtAndLost();
        } else {
            playerOne.updateBattlesFoughtAndDrawn();
            playerTwo.updateBattlesFoughtAndDrawn();
        }
    }

    public User fightRound(User attacker, User defender){
        Card cardA = attacker.getDeck().randomCard();
        Card cardB = defender.getDeck().randomCard();
        winner = checkWinner(attacker, defender);
        if (cardA.defeats(cardB) || cardA.calculateEffectiveness(cardB) > cardB.calculateEffectiveness(cardA)) {
            attacker.getStack().getStackList().add(cardB);
            System.out.println("Winner: " + attacker.getUsername() + "-" + cardA.getName() + "(" + cardB.getDamage() + ")");
        } else if (cardB.getDamage() > cardA.getDamage()){
            defender.getStack().getStackList().add(cardA);
            System.out.println("Winner: " + defender.getUsername() + "-" + cardB.getName() + "(" + cardA.getDamage() + ")");
        }
        if (winner == null){
            swapper(attacker, defender);
        }

        return winner;
    }

   public void swapper(User attacker, User defender) {
        this.playerOne = defender;
        this.playerTwo = attacker;
    }

    public User checkWinner(User playerOne, User playerTwo){
      if (playerOne.getDeck().getDeckList().size() == 0){
          this.winner = playerTwo;
          this.loser = playerOne;
      } else if (playerTwo.getDeck().getDeckList().size() == 0){
          this.winner = playerOne;
          this.loser = playerTwo;
      } else {
          this.winner = null;
          this.loser = null;
      }
      return this.winner;
    }


}
