package com.example.ezaharova.test;


public class Answer {
    private String status;
    private String message;
    private String url;

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public Answer(String errMessage) {
        status = ERROR;
        message = errMessage;
    }

    public boolean isOk() {
        return status.equals(OK);
    }

    public String getMessage() {
        return message;
    }

    public String getUrlString() {
        return url;
    }

}