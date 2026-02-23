package com.ejercicio.conector.exception;

public class ErrorMessage {

    private String message;
    private String url;
    private String method;

    public ErrorMessage(String message, String url, String method) {
        this.message = message;
        this.url = url;
        this.method = method;
    }

    public String getMessage() { return message; }
    public String getUrl() { return url; }
    public String getMethod() { return method; }
}