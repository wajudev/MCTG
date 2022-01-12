package com.example.mctg.rest.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    NOTSUPPORTED;

    public static final List<HttpMethod> methodList = new ArrayList<>(Arrays.asList(HttpMethod.values()));
}
