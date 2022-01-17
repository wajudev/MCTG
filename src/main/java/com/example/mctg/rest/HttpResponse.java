package com.example.mctg.rest;

import com.example.mctg.rest.enums.StatusCode;
import com.example.mctg.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class HttpResponse {

    private String version;
    private final StatusCode status;
    private String reasonPhrase;
    private final Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;
    private String body;
    private User user;
    private boolean startBattle;

    public String getResponse(){
        this.responseHeaders = new HashMap<>();
        fillUpHeader();
        return
                this.version + " " + this.status.getCode() + " " + this.status.getStatus() + "\r\n"
                        + getHeaderPairs()
                        + "\r\n"
                        + reasonPhrase + "\r\n"
                ;

    }


    public String getHeaderPairs() {
        Set<Map.Entry<String, String>> entries = this.responseHeaders.entrySet();
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return result.toString();
    }

    public void fillUpHeader() {
        if(this.status == StatusCode.UNAUTHORIZED) {
            addHeaderPair("WWW-Authenticate", "Basic realm=\"User Visible Realm\", charset=\"UTF-8\"");
        }
        if(!reasonPhrase.isEmpty()) {
            addHeaderPair("Content-Length", Integer.toString(reasonPhrase.length()));
            addHeaderPair("Content-Type", body);
        } else {
            addHeaderPair("Content-Length", "0");
        }
    }

    public void addHeaderPair(String key, String value) {
        this.responseHeaders.put(key, value);
    }

}
