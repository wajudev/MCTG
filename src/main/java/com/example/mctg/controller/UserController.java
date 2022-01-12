package com.example.mctg.controller;

import com.example.mctg.cards.Card;
import com.example.mctg.cards.CardService;
import com.example.mctg.cards.Deck;
import com.example.mctg.cards.Stack;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.user.Credentials;
import com.example.mctg.user.User;
import com.example.mctg.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserController {
    private DatabaseService databaseService;
    private User user;

    public String register(Credentials credentials) throws SQLException {
        try {
            this.user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .coins(20)
                    .elo(100)
                    .stack(new Stack())
                    .deck(new Deck())
                    .isAdmin(false)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return "Signup failed (UserController)";
        }

        if(UserService.getInstance().addUser(user)) {
            return "Signup was successful (UserController)";
        } else {
            return "Signup failed (UserController)";
        }
    }

    public boolean setUser(String token) {
        this.user = UserService.getInstance().getLoggedUser(token);

        if(this.user != null) {
            initializeStack();
            return true;
        } else {
            return false;
        }
    }

    public String editUserData(String jsonEditedUser) throws JsonProcessingException {


        User user = User.builder()
                .username(this.user.getUsername())
                .password(this.user.getPassword())
                .token(this.user.getUsername() + "-mtcgToken")
                .coins(this.user.getCoins())
                .elo(this.user.getElo())
                .stack(this.user.getStack())
                .deck(this.user.getDeck())
                .isAdmin(this.user.isAdmin())
                .build();

        if(UserService.getInstance().updateUser(this.user.getId(), user)) {
            return "User info update was successful (UserController)";
        } else {
            return "User info  update failed (UserController)";
        }
    }


    public String login(Credentials credentials) {
        try{
            this.user = UserService.getInstance().getUserByUsername(credentials.getUsername(), credentials.getPassword());
            if (this.user != null){
                if (UserService.getInstance().addSession(credentials.getUsername() + "-mtcgToken")){
                    System.out.println("Login was successful (UserController)");
                    return "Login was successful";
                } else {
                    return "User is already logged in";
                }
            } else {
                return "Wrong user or password";
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            System.out.println("Login failed (UserController)");
            return "Something failed";
        }
    }

    public boolean initializeStack() {
        List<Card> list = CardService.getInstance().getCardsByUser(this.user);
        if(list.isEmpty()) {
            return false;
        } else {
            this.user.getStack().setStackList(list);
            this.user.getDeck().setDeckList(CardService.getInstance().getCardsInDeck(this.user.getId()));
            return true;
        }
    }

    public String addCardsToDeck(List<String> ids) {
        if(ids.size() != 4) {
            return "Amount of cards in deck, can't be higher than 4 ";
        }

        CardService.getInstance().removeFromDeck(this.user.getId()); // Remove all cards from deck

        for(String id: ids) {  // ! mark card isDeck if user-id marches
            if (!CardService.getInstance().addToDeck(id, this.user.getId())){
                CardService.getInstance().removeFromDeck(this.user.getId());
                return "This card has not been added, because it doesn't belong to this user";
            }
        }

        // ! initialize/set new user's deck
        this.user.getDeck().setDeckList(CardService.getInstance().getCardsInDeck(this.user.getId()));

        /*for(String id: ids) { // ! delete from trading market
            if(db.getTradeByCardId(id) != null)
                db.deleteTrade(id);
        }*/

        return null;
    }

    public String buyPackage(List<Card> packageDB) {
        if(packageDB == null) {
            packageDB = new ArrayList<>(CardService.getInstance().getCardsForPackages(CardService.getInstance().getMaxPackageId()));
            if(packageDB.size() == 0) {
                return "The are no packages available to buy";
            }
        }

        if(this.user.buyPackage(packageDB)){
            CardService.getInstance().addPackageToUser(this.user.getId(), CardService.getInstance().getMaxPackageId());
            UserService.getInstance().updateUserStats(this.user);
            return "Package bought";
        } else {
            return "You don't have money to buy another package";
        }

    }
}
