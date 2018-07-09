package com.android.renly.edu_yunzhi.Bean;

public class MessageEvent {
    private String Message;
    public MessageEvent(String message){
        this.Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void getMessage(String Message){
        this.Message = Message;
    }
}
