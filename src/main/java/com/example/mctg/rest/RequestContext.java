package com.example.mctg.rest;

import lombok.Getter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestContext {
    @Getter
    Socket socket;

    @Getter
    HttpRequest request;

    @Getter
    HttpResponse response;

    @Getter
    Map<String, Method> routes;
    public RequestContext(Socket socket) {
        this.socket = socket;
        request = new HttpRequest();
        routes = new HashMap<>(){{
           try {

           } catch(NoSuchMethodError e){
               e.printStackTrace();
           }
        }};
    }

    public void socketHandler(){
        try {
            // Read InputStream
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            request.read(reader);

            Method method = routeResolver(request);
            if (method != null) {
                // Try to invoke the resolved method
                try {
                    response = (HttpResponse) method.invoke(method.getDeclaringClass().getConstructor().newInstance(), request);
                } catch (InstantiationException | NoSuchMethodException e) {
                    // Error 500 - Internal Server Error
                    response = HttpResponse.internalServerError();
                    e.printStackTrace();
                }
            } else {
                // Error 404 - Not Found
                response = HttpResponse.notFound();
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            response.write(writer);

        } catch (IOException | IllegalAccessException | InvocationTargetException ignored){

        }


    }

    public Method routeResolver(HttpRequest request){
        if (request.getMethod() == null || request.getPath() == null){
            return null;
        }
        String requestRoute = request.getMethod().toUpperCase() + " " + request.getPath();
        for (Map.Entry<String, Method> entry : this.routes.entrySet()){
            if (Pattern.matches(entry.getKey(), requestRoute)){
                return entry.getValue();
            }
        }

        return null;
    }

}
