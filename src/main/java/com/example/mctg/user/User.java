package com.example.mctg.user;


import com.example.mctg.cards.Deck;
import com.example.mctg.cards.Stack;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder(toBuilder = true)
public class User {
    private int id;
    private String status;
    private String username;
    private String password;
    private String token;
    private int coins;
    private int battlesFought;
    private int battlesWon;
    private int battlesLost;
    private int elo;
    private boolean isAdmin;

    private Stack stack;
    private Deck deck;



    public String printUserDetails() {
        return  "-- User Account Summary -- \n" +
                "\tUser: " + this.username +
                " - ELO: " + this.elo + " - coins: " + this.coins +
                "\nToken: "+ this.token + " \n";
    }

}
