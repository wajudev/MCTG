package com.example.mctg.rest;

import com.example.mctg.controller.UserController;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.rest.enums.HttpMethod;
import com.example.mctg.rest.enums.StatusCode;
import com.example.mctg.rest.interfaces.IRequestHandler;
import com.example.mctg.user.Credentials;
import com.example.mctg.user.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class RequestHandler implements IRequestHandler {
    private StatusCode responseStatus;
    private HttpRequest requestContext;
    private String responseBody;
    private String[] path;
    private boolean formatJson;

    private UserController userController;
    private DatabaseService databaseService;

    public HttpResponse handleRequest() throws JsonProcessingException, SQLException {
        if (!HttpMethod.methodList.contains(requestContext.getMethod())){
            setResponseStatus("Method not supported", StatusCode.BADREQUEST);
        } else {
            if (this.requestContext.getVersion().equals("HTTP/1.1")){
                handlePath();
            } else {
                setResponseStatus("Version not supported",StatusCode.VERSIONNOTSUPPORTED);
            }
        }
        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .reasonPhrase(this.responseBody)
                .status(this.responseStatus)
                .requestHeaders(this.requestContext.getHeaders())
                .build();
    }

    @Override
    public void handlePath() throws JsonProcessingException, SQLException {
        splitURL(this.requestContext.getPath());

        if (this.responseStatus != StatusCode.BADREQUEST
                && this.requestContext.getPath().matches("(/" + this.path[1] + "/)("+ this.path[2] +")(/?)" )) {
            selectAction(this.path[1],this.path[2], getClientToken());

        } else if (this.responseStatus != StatusCode.BADREQUEST
                && ( this.requestContext.getPath().matches("(/" + this.path[1] + ")(/?)([?a-z=]*)")
                || this.requestContext.getPath().matches("(/deck\\?format=plain)" ) )
        ) {
            selectAction(this.path[1], getClientToken());

        } else {
            setResponseStatus("Wrong URL Path", StatusCode.BADREQUEST);
        }

    }

    @Override
    public void register(String requestBody, String token) throws JsonProcessingException, SQLException {
        Credentials credentials = getCredentials(requestBody);

        if (token == null) {
            if (UserService.getInstance().getUser(credentials.getUsername()) == null){
                this.responseBody = userController.register(credentials);
            } else {
                this.responseBody = "This user already exists";
            }
        } else {
            if (this.userController.setUser(token)){
                this.responseBody = "This user is already logged in";
            }
        }

    }

    @Override
    public void login(String requestBody) throws JsonProcessingException {
        Credentials credentials = getCredentials(requestBody);
        this.responseBody = this.userController.login(credentials);
        if (this.userController.getUser() == null){
            this.responseStatus = StatusCode.UNAUTHORIZED;
        }
    }

    public void selectAction(String first, String token) throws JsonProcessingException, SQLException {

        if(!this.userController.setUser(token)) { // ! user is not logged in
            anonymousUserAction(first);
        } else {
            loggedUserAction(first);
        }
    }

    public void manipulateUserAccount(String token, String userNameURL) throws JsonProcessingException {
        if( userNameURL.equals(token) ) { // * token = user

            if( requestContext.getMethod() == HttpMethod.GET ) { // ! show user info
                this.responseBody = this.userController.getUser().printUserDetails();

            } else if ( requestContext.getMethod() == HttpMethod.PUT ) { // ! modify user info
                this.responseBody = this.userController.editUserData(this.requestContext.getBody());
            } else {
                setResponseStatus("Method not allowed", StatusCode.BADREQUEST);
            }

        } else {
            setResponseStatus("You don't have access to other users' account", StatusCode.UNAUTHORIZED);
        }
    }

    public void selectAction(String first, String second, String token) throws JsonProcessingException {
        //! localhost:8008/first/second

        if(this.userController.setUser(token)) {
            switch (first) {
                case "users"        -> manipulateUserAccount(this.userController.getUser().getUsername(), second);
                //case "transactions" -> buyNewPackage(second, requestContext.getBody(), token);
                //case "tradings"     -> deleteOrTrade(second, this.requestContext.getBody());
                default             -> setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Need to login to access any functionality", StatusCode.BADREQUEST);
        }
    }

    public void loggedUserAction(String first) throws JsonProcessingException, SQLException {

        switch (first) {
            //case "score"    -> getScoreBoard();
            //case "stats"    -> this.responseBody = this.userController.getUser().userStats("");
            //case "battles"  -> startBattle(getClientToken());
            //case "packages" -> insertNewPackage();
            //case "cards"    -> showUserCards(getClientToken());
            //case "deck"     -> manipulateDeck(getClientToken(), requestContext.getBody());
            //case "tradings" -> handleTradings(getClientToken());
            default         -> setResponseStatus("Wrong URL", StatusCode.BADREQUEST);
        }
    }

    public void anonymousUserAction(String first) throws JsonProcessingException, SQLException {

        switch (first) {
            case "users"    -> handleUser(this.requestContext.getBody(), this.requestContext.getMethod());
            case "sessions" -> login(this.requestContext.getBody());
            default         -> setResponseStatus("Unauthorized. Log in to access all functionalities",
                    StatusCode.UNAUTHORIZED);
        }
    }


    public String getClientToken() {
        String tokenLine = requestContext.getHeaders().get("Authorization");
        if(tokenLine != null) {
            String[] auths = tokenLine.split(" ");
            if(!auths[1].isEmpty()) {
                return auths[1];
            }
        }
        return null;
    }

    public static Credentials getCredentials(String requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); // also private attributes
        return objectMapper.readValue(requestBody, Credentials.class);
    }

    public void handleUser(String body, HttpMethod method) throws JsonProcessingException, SQLException {
        if (method == HttpMethod.POST) {
            register(body, getClientToken());
        } else {
            setResponseStatus("Method is not allowed", StatusCode.BADREQUEST);
        }
    }

    public void splitURL(String fullPath) {
        this.path = new String[3];
        String[] temp = fullPath.split("/");
        int i = 0;

        for(String one: temp) {
            this.path[i] = one;
            i++;
        }

        if(this.requestContext.getPath().contains("?")) {
            temp = this.path[1].split("\\?");
            this.path[1] = temp[0];
            this.path[2] = temp[1];
            handleUrlParameters(temp[1]);
        } else {
            validateURLActions(this.path[1]);
        }
    }

    public void handleUrlParameters(String parameters) {
        if(parameters.equals("format=plain"))
            formatJson = false;
        Map<String, String> map = getQueryMap(parameters);
    }


    public void validateURLActions(String first) {
        String[] allowedTables = {"users", "sessions", "score", "stats", "tradings", "transactions", "battles", "deck", "cards", "", "packages", null};
        if( !Arrays.asList( allowedTables ).contains( first ) ) {
            this.path = null;
            setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
        }
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            if(param.length() < 2) return null; // ! control later
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

    private void setResponseStatus(String responseMessage, StatusCode code) {
        this.responseBody = responseMessage;
        this.responseStatus = code;
    }

}
