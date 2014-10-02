package com.example.michael.lab2;

public class ChatModel implements Comparable {
    // class to contain chat message information
    public String name, message;
    public long timestamp;

    public ChatModel(){}

    public ChatModel(String sender, String message){
        // constructor to assign values
        this.name = sender;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ChatModel(String sender, String message, long timestamp){
        // constructor to assign values
        this.name = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    // compare times of objects
    @Override
    public int compareTo(Object object) {
        return (timestamp < ((ChatModel) object).timestamp)?1:0;
    }
}
