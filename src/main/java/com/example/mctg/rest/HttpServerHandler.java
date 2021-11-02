package com.example.mctg.rest;

public abstract class HttpServerHandler {

    public HttpResponse indexHandler(HttpRequest request) {
        return HttpResponse.notImplemented();
    }

    public HttpResponse getHandler(HttpRequest request) {
        return HttpResponse.notImplemented();
    }

    public HttpResponse postHandler(HttpRequest request) {
        return HttpResponse.notImplemented();
    }

    public HttpResponse putHandler(HttpRequest request) { return HttpResponse.notImplemented();}

    public HttpResponse patchHandler(HttpRequest request) {
        return HttpResponse.notImplemented();
    }

    public HttpResponse deleteHandler(HttpRequest request) {
        return HttpResponse.notImplemented();
    }
}
