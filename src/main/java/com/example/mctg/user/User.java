package com.example.mctg.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Builder(toBuilder = true)
public class User implements UserInterface {
    @Getter
    int id;

    @Getter
    String status;

    @Getter
    String username;

    @Getter
    String password;

    @Getter
    String token;

    @Getter
    @Setter
    @Builder.Default
    int elo = 100;


    @Getter
    @Setter
    @Builder.Default
    int coins = 20;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = this.auth(password);
    }


    @Override
    @JsonIgnore
    public String auth(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1,encodedhash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32){
                hexString.insert(0,'0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @JsonIgnore
    public boolean isLoggedIn(){
        if (this.token == null){
            return false;
        }
        return !(token.isEmpty());
    }

}
