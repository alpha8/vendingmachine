package com.yihuyixi.vendingmachine.message;

import java.io.Serializable;

public class EventMessage {
    private int type;
    private String message;
    private Serializable data;

    public EventMessage(){}

    public EventMessage(int type) {
        this.type = type;
    }

    public EventMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public EventMessage(int type, Serializable data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
