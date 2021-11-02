package com.example.mctg;

import com.example.mctg.rest.RestService;

public class Main {
    public static void main(String[] args){
        Thread rest = new Thread(new RestService(8000));
        rest.start();
    }
}

