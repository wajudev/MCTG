package com.example.mctg.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.Hashing;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
@Builder(toBuilder = true)
public class User {
    private int id;
    private String status;
    private String username;
    private String password;
    private String token;
    private int elo = 100;
    private int coins = 20;
    private boolean isAdmin;


    @JsonIgnore
    public boolean auth(String password) {
        String passwordHash = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        return passwordHash.equals(getPassword());
    }

    @JsonIgnore
    public boolean isLoggedIn(){
        if (this.token == null){
            return false;
        }
        return !(token.isEmpty());
    }

    public String printUserDetails() {
        return  "-- User Account Summary -- \n" +
                "\tUser: " + this.username +
                " - ELO: " + this.elo + " - coins: " + this.coins +
                "\nToken: "+ this.token + " \n";
    }

}
