package com.example.mctg.rest;

import com.example.mctg.rest.enums.HttpMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    @Getter
    String version;

    @Getter
    HttpMethod method;

    @Getter
    String path;

    @Getter
    Map<String, String> headers;

    @Setter
    @Getter
    String body;

    @Getter
    ArrayList<String> request;


    public HttpRequest(ArrayList<String> request){
        this.request = request;
        String[] line = request.get(0).split(" ", 3);
        try {
            this.method = HttpMethod.valueOf(line[0]);
        } catch (Exception e) {
            this.method = null;
        }

        this.path = line[1];
        this.version = line[2];
        this.headers = new HashMap<>();
        this.body = "";

        for (int i = 1; i < request.size()-1; i++) {
            line = request.get(i).split(": ", 2);
            headers.put(line[0], line[1]);
        }
    }

    public int getBodyLength() {
        if(this.headers.get("Content-Length") != null) {
            return Integer.parseInt(this.headers.get("Content-Length"),10);
        }
        return 0;
    }
}
