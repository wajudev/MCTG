package com.example.mctg.controller;

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
                    //.stack(new CardStack())
                    //.deck(new CardDeck())
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
            //initializeStack(); //changed check for errors
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
                //.stack(this.user.getStack())
                //.deck(this.user.getDeck())
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
                if (UserService.getInstance().addSession(credentials.getUsername() + "mctg-token")){
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
}
