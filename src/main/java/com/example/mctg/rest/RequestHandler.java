package com.example.mctg.rest;

import com.example.mctg.cards.Card;
import com.example.mctg.cards.CardService;
import com.example.mctg.controller.CardController;
import com.example.mctg.controller.TradeController;
import com.example.mctg.controller.UserController;
import com.example.mctg.database.DatabaseService;
import com.example.mctg.rest.enums.HttpMethod;
import com.example.mctg.rest.enums.StatusCode;
import com.example.mctg.rest.interfaces.IRequestHandler;
import com.example.mctg.user.Credentials;
import com.example.mctg.user.User;
import com.example.mctg.user.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;


import java.sql.SQLException;
import java.util.*;

@Builder
@Data
public class RequestHandler implements IRequestHandler {
    private StatusCode responseStatus;
    private HttpRequest requestContext;
    private String responseBody;
    private String[] path;
    private boolean formatJson;
    private boolean startBattle;

    private UserController userController;
    private DatabaseService databaseService;
    private CardController cardController;
    private TradeController tradeController;

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
                .user(this.userController.getUser())
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

        if(this.userController.setUser(token)) { // ! user is not logged in
            loggedUserAction(first);
        } else {
            anonymousUserAction(first);
        }
    }

    public void loggedUserAction(String first) throws JsonProcessingException, SQLException {

        switch (first) {
            case "score"    -> ScoreBoard();
            case "stats"    -> this.responseBody = this.userController.getUser().userStats("");
            case "battles"  -> startBattle(getClientToken());
            case "packages" -> insertNewPackage();
            case "cards"    -> showUserCards(getClientToken());
            case "deck"     -> manipulateDeck(getClientToken(), requestContext.getBody());
            case "tradings" -> tradeHandler(getClientToken());
            default         -> setResponseStatus("Wrong URL", StatusCode.BADREQUEST);
        }
    }

    public void selectAction(String first, String second, String token) throws JsonProcessingException {
        //! localhost:8008/first/second

        if(this.userController.setUser(token)) {
            switch (first) {
                case "users"        -> manipulateUserAccount(this.userController.getUser().getUsername(), second);
                case "transactions" -> buyPackage(second, requestContext.getBody(), token);
                case "tradings"     -> tradeOrDelete(second, this.requestContext.getBody());
                default             -> setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Need to login to access any functionality", StatusCode.BADREQUEST);
        }
    }

    public void tradeHandler(String token) throws JsonProcessingException, SQLException {
        if (this.userController.setUser(token) && !this.userController.getUser().isAdmin()){
            switch (this.requestContext.getMethod()){
                case GET -> showTrades();
                case POST -> addTrade(this.requestContext.getBody(), this.userController.getUser().getId());
                default -> setResponseStatus("Wrong method", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Only players own cards", StatusCode.BADREQUEST);
        }
    }

    public void addTrade(String requestBody, int userId) throws JsonProcessingException, SQLException {
        String errorMsg = tradeController.addNewTrade(requestBody, userId);
        if(errorMsg == null) {
            this.responseBody = "New trade added";
        } else {
            setResponseStatus(errorMsg, StatusCode.BADREQUEST);
        }
    }

    public void showTrades() {
        String allStr = "--------------------\n" +
                "-- Cards to Trade --\n" +
                "--------------------\n\n";
        String tradesInfo = String.valueOf(tradeController.getTradesSummary());
        if(tradesInfo == null) {
            setResponseStatus("The are no trades available", StatusCode.NOCONTENT);
        } else {
            System.out.println(allStr + tradesInfo);
            this.responseBody = allStr + tradesInfo;
        }
    }

    public void tradeOrDelete(String desiredCardId, String requestBody){
        if (HttpMethod.DELETE == requestContext.getMethod()){
            deleteTrade(desiredCardId);
        } else if (HttpMethod.POST == requestContext.getMethod()){
            tradeCards(desiredCardId, requestBody);
        }
    }

    public void tradeCards(String desiredCard, String requestBody){
        if (requestBody.isEmpty()){
            String errorMsg = tradeController.payForTrade(desiredCard, userController.getUser().getId(), userController.getUser().getCoins());

            if (errorMsg == null){
                this.userController.getUser().setCoins(this.userController.getUser().getCoins());
                this.userController.getUserService().updateUserStats(this.getUserController().getUser());
                this.responseBody = "Trade purchase was successful";
            } else {
                setResponseStatus(errorMsg, StatusCode.BADREQUEST);
            }
        } else {
            String offeredCardId = requestBody.replaceAll("[\"]", "");
            String errorMsg = tradeController.tradeCards(desiredCard, offeredCardId, userController.getUser().getId());
            if (errorMsg == null){
                this.responseBody = "Trade was successful";
            } else {
                setResponseStatus(errorMsg, StatusCode.BADREQUEST);
            }
        }
    }

    public void deleteTrade(String tradeId) {
        String errorMsg = tradeController.deleteTradedCard(tradeId);
        if(errorMsg == null) {
            this.responseBody = "Trade was deleted";
        } else {
            setResponseStatus(errorMsg, StatusCode.BADREQUEST);
        }
    }

    private void startBattle(String clientToken) {
        if (this.userController.setUser(clientToken) && !this.userController.getUser().isAdmin() && this.requestContext.getMethod() == HttpMethod.POST){
            if (this.userController.initializeStack()){
                if (this.userController.getUser().getStack().getStackList().size() >= 4){
                    this.startBattle = true;
                } else {
                    setResponseStatus("You don't have enough cards to play (min: 4)", StatusCode.BADREQUEST);
                }
            } else {
                setResponseStatus("You can't start a battle. Your Stack is Empty", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    private void ScoreBoard() {
        List<User> playersList = new ArrayList<>(UserService.getInstance().getUsersRankedByElo());

        this.responseBody = "-- Scoreboard --\n";
        playersList.forEach((temp)->{
            if (!temp.isAdmin()){
                this.responseBody = this.responseBody + temp.userStats(temp.toString());
            }
        });
    }

    public void manipulateUserAccount(String token, String userNameURL) throws JsonProcessingException {
        if( userNameURL.equals(token) ) {

            if( requestContext.getMethod() == HttpMethod.GET ) {
                this.responseBody = this.userController.getUser().printUserDetails();

            } else if ( requestContext.getMethod() == HttpMethod.PUT ) {
                this.responseBody = this.userController.editUserData(this.requestContext.getBody());
            } else {
                setResponseStatus("Method not allowed", StatusCode.BADREQUEST);
            }

        } else {
            setResponseStatus("You don't have access to other users' account", StatusCode.UNAUTHORIZED);
        }
    }

    private void buyPackage(String url, String requestBody, String token) {
        if(this.requestContext.getMethod() == HttpMethod.POST && this.userController.setUser(token)){
            if (url.equals("packages") && requestBody.isEmpty()) {
                this.responseBody = this.userController.acquirePackage(null);
            } else {
                setResponseStatus("WRONG URL", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Invalid HTTP method, only POST allowed", StatusCode.BADREQUEST);
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

    public void showUserCards(String token) throws JsonProcessingException {
        // Admins can't own cards
        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin()) {
            if(this.userController.initializeStack()) {
                this.responseBody = String.valueOf(cardController
                        .getCardsListJson(this.userController.getUser().getStack().getStackList()));
            } else {
                setResponseStatus("Stack is empty", StatusCode.NOCONTENT);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void manipulateDeck(String token, String requestBody) throws JsonProcessingException {

        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin()) {
            if(this.userController.initializeStack()) {
                switch (this.requestContext.getMethod()) {
                    case PUT -> initializeDeck(requestBody);
                    case GET -> showDeckCards(this.formatJson);
                    default  -> setResponseStatus("Deck - This method is not allowed", StatusCode.BADREQUEST);
                }
            } else {
                setResponseStatus("Stack is empty", StatusCode.NOCONTENT);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void showDeckCards(boolean formatJson) throws JsonProcessingException {
        if(!this.userController.getUser().isAdmin()) {
            if(this.userController.getUser().getDeck().getDeckList() != null) {
                List<Card> deck = this.userController.getUser().getDeck().getDeckList();
                if(!deck.isEmpty()) {
                    if(!formatJson) {
                        this.responseBody = String.valueOf(cardController
                                .getCardListStats(deck, "Deck"));
                    } else {
                        this.responseBody = String.valueOf(cardController
                                .getCardsListJson(deck));
                    }
                } else {
                    setResponseStatus("Deck is empty", StatusCode.NOCONTENT);
                }
            }
        } else {
            setResponseStatus("Only players can own cards, admins aren't allowed", StatusCode.BADREQUEST);
        }
    }

    public void initializeDeck(String requestBody) {
        List<String> ids = parseJsonArray(requestBody);
        String errorMsg = this.userController.addCardsToDeck(ids);
        if(errorMsg == null && ids.size() == 4) {
            this.responseBody = "New deck prepared";
        } else {
            setResponseStatus(  errorMsg, StatusCode.BADREQUEST);
        }
    }

    public void insertNewPackage() throws JsonProcessingException {
        if(this.userController.getUser().isAdmin()) {
            if( ! this.cardController.insertJSONCards( this.requestContext.getBody(), this.userController.getUser() ) ) {
                setResponseStatus( "This card already exists in DB", StatusCode.BADREQUEST);
            } else {
                this.responseBody = "Cards added to DB";
            }
        } else {
            setResponseStatus("Only admin can add new packages", StatusCode.UNAUTHORIZED);
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

    public List<String> parseJsonArray(String json) {
        String replaceStr = json.replaceAll("[\\[\\] \\n\\r\"]", "");
        return Arrays.asList(replaceStr.split(","));
    }

}
