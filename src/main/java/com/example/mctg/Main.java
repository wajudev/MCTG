package com.example.mctg;


import com.example.mctg.controller.BattleController;
import lombok.Builder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Builder
public class Main implements Runnable{

    private static ServerSocket listener = null;
    private final static BattleController battleController = new BattleController(new ArrayBlockingQueue<>(2, true), new AtomicBoolean(false), null);
    public static void main(String[] args)  {
        System.out.println("start server");

        try {
            listener = new ServerSocket(10001, 5);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Main()));

        try {
            while (true) {
                Socket s = listener.accept();
                new Thread(new RequestThread(s, battleController)).start();
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

