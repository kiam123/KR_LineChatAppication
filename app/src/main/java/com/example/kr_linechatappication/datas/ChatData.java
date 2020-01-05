package com.example.kr_linechatappication.datas;

public class ChatData {
    String id, content, mTimeStamp;
    int type, mChatView;



    public ChatData(String id, String content, String timeStamp, int type) {
        this.id = id;
        this.content = content;
        this.mTimeStamp = timeStamp;
        this.type = type;
    }

    public ChatData(String timeStamp, String sender, String receiver, String content, String type, String imageURL) {
        this.id = sender;
        this.content = content;
        this.mTimeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getType() {
        return type;
    }

    public int getmChatView() {
        return mChatView;
    }
}
