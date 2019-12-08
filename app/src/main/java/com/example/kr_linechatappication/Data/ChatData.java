package com.example.kr_linechatappication.Data;

public class ChatData {
    String id, content, datetime;
    int type;

    public ChatData(String id, String content, String datetime, int type){
        this.id = id;
        this.content = content;
        this.datetime = datetime;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getType() {
        return type;
    }
}
