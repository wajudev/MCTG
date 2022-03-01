package com.example.mctg.user;


import com.example.mctg.cards.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class User  {
    private int id;
    private String username;
    private String password;
    private String token;
    private int coins;
    private int battlesFought;
    private int battlesWon;
    private int battlesLost;
    private int battlesDrawn;
    private int elo;
    private float winLossRatio;
    private boolean isAdmin;
    private String bio;
    private String image;

    private Stack stack;
    private Deck deck;

    private final static int ELOINCREASEBY = 3;
    private final static int ELODECREASEBY = 5;


    public boolean buyPackage(List<Card> list){ // ! DB cards
       Packages pack = new Packages(list);
        if(this.coins >= pack.getPrice()) {
            this.coins = this.coins - pack.getPrice();
            this.stack.addListToStack(pack.getCards());

            return true;
        } else {
            return false;
        }
    }

    public String userStats(String rank) {
        if(!this.isAdmin) {
            if(rank.isEmpty()) {
                return  rank +
                        " User: " + this.username +
                        " - Coins: " + this.coins +
                        " - Games Played: " + this.battlesFought +
                        " - Games Won " + this.battlesWon +
                        " - Games Lost " + this.battlesLost +
                        " - Games Drawn " + this.battlesDrawn +
                        " - Win/Loss Ratio " + this.winLossRatio +
                        " - ELO: " + this.elo + "\n";
            }
            return  rank +
                    " User: " + this.username +
                    " - Games Played: " + this.battlesFought +
                    " - Games Won " + this.battlesWon +
                    " - Games Lost " + this.battlesLost +
                    " - Games Drawn " + this.battlesDrawn +
                    " - Win/Loss Ratio " + this.winLossRatio +
                    " - ELO: " + this.elo + "\n";
        } else {
            return "";
        }
    }

    public String printUserDetails() {
        return  "-- User Account Summary -- \n" +
                "\tUser: " + this.username + " - ELO: " + this.elo + " - coins: " + this.coins + "\n" +
                "\tBio: "+ this.bio +
                "\tImage: "+ this.image +
                "\tToken: "+ this.token + " \n";
    }

    public String getUserStats() {
        return  "-- User Account Summary -- \n" +
                "-  User: " + this.username +
                " - ELO: " + this.elo +
                " - Games Played: " + this.battlesFought +
                " - Games Won: " + this.battlesWon +
                " - Games Lost " + this.battlesLost +
                " - Games Drawn " + this.battlesDrawn +
                " - Win/Loss Ratio " + this.winLossRatio +
                " - Total Cards: " + this.stack.getStackList().size() + " \n";
    }

    public void eloDecrease() {
        int newElo;
        newElo = this.elo - ELODECREASEBY;
        this.elo = Math.max(newElo, 0);
    }

    public void eloIncrease(){
        this.elo += ELOINCREASEBY;
    }


    // For unit test
    public void buyPackage() {
        Packages newPackage = new Packages();
        if(this.coins - newPackage.getPrice() > 0) {
            this.coins = this.coins - newPackage.getPrice();
            this.stack.addListToStack(newPackage.getCards());
        }
    }

    public void prepareDeck() {
        this.deck = new Deck(this);
    }

    public void updateBattlesFoughtAndWon(){
        this.battlesFought++;
        this.battlesWon++;
        this.winLossRatio = (float) battlesWon / battlesFought;

    }
    public void updateBattlesFoughtAndDrawn(){
        this.battlesFought++;
        this.battlesDrawn++;
        this.winLossRatio = (float) battlesWon / battlesFought;
    }
    public void updateBattlesFoughtAndLost(){
        this.battlesFought++;
        this.battlesLost++;
        this.winLossRatio = (float) battlesWon / battlesFought;
    }
}
