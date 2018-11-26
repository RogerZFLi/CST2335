package com.example.amusu.androidlabs;



public class Message {
    private long id;
    private String message;

    protected Message(){}

    protected Message(long id, String message){
        this.id=id;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
