package com.example.mctg.user;

public interface UserInterface {
    int getId();

    String getStatus();

    String getUsername();

    String getPassword();

    String getToken();

    int getElo();

    int getCoins();

    void setCoins(int coins);

    String auth(String password);

}