package com.example.kr_linechatappication.datas;

public class ChatData {
    String id, content, datetime;
    int type, mChatView;

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

    public int getmChatView() {
        return mChatView;
    }
}
