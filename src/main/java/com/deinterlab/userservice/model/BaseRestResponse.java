package com.deinterlab.userservice.model;

public class BaseRestResponse<T> {

    private int status;
    private String message;
    private T data;

    public BaseRestResponse() {}

    public BaseRestResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // To string
    public String toString() {
        return "BaseRestResponse(status=" + this.getStatus() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }
}
