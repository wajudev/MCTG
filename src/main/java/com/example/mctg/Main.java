package com.example.mctg;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main implements Runnable{

    private static ServerSocket listener = null;
    public static void main(String[] args)  {
        System.out.println("start server");

        try {
            listener = new ServerSocket(10001, 5); //8080
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Main()));

        try {
            while (true) {
                Socket s = listener.accept();
                new Thread(new RequestThread(s)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = null;
        System.out.println("close server");
    }
}

