package com.example.mctg;

import com.example.mctg.controller.UserController;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.rest.HttpRequest;
import com.example.mctg.rest.HttpResponse;
import com.example.mctg.rest.RequestHandler;
import com.example.mctg.rest.enums.StatusCode;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestThread implements Runnable {

    private final Socket socket;

    public RequestThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    @SneakyThrows
    public void run() {
        ArrayList<String> header = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        readHeader(header, reader);

        try {
            if(header.size() < 2) return; // postman solution - empty request
            HttpRequest requestContext = new HttpRequest(header);
            OutputStream outputStream = socket.getOutputStream();
            readBody(requestContext, reader);

            try {
                RequestHandler requestHandler = RequestHandler.builder()
                        .requestContext(requestContext)
                        .responseStatus(StatusCode.OK)
                        .databaseService(DatabaseService.getInstance())
                        .userController(new UserController(DatabaseService.getInstance(), null))
                        //.cardController( new CardController( new DbConnection() ) )
                        //.tradeController( new TradeController( new DbConnection() ) )
                        .formatJson(true)
                        //.startBattle(false)
                        .build();

                HttpResponse response = requestHandler.handleRequest();
                /*if(response.isStartBattle() && response.getPlayer() != null) { //! Battle
                    prepareBattle(gameController, response);
                }*/

                outputStream.write(response.getResponse().getBytes());
            }catch (RuntimeException e) {
                e.printStackTrace();
                HttpResponse res = HttpResponse.builder()
                        .version(requestContext.getVersion())
                        .reasonPhrase("Internal Server Error")
                        .status(StatusCode.INTERNALERROR)
                        .requestHeaders(null)
                        .build();

                outputStream.write(res.getResponse().getBytes()); // e.getMessage();
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e){
            System.out.println("Error reading header request or body");
        }
        reader.close();
    }

    private void readHeader(ArrayList<String> header, BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            header.add(line);
            System.out.println(line);
        } while (line != null && !line.isEmpty());
    }

    public void readBody(HttpRequest requestContext, BufferedReader reader) throws IOException {
        if ( requestContext.getBodyLength() > 0 ) {
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = reader.read()) != -1) {
                sb.append((char) read);
                if (sb.length() == requestContext.getBodyLength()) {
                    break;
                }
            }
            requestContext.setBody(sb.toString());
        } else {
            requestContext.setBody("");
        }
    }
}
