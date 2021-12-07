package com.example.mctg.rest.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public interface IRequestHandler {
    void handlePath() throws JsonProcessingException, SQLException;
    void register(String requestBody, String token) throws JsonProcessingException, SQLException;
    void login(String requestBody) throws JsonProcessingException;
}
