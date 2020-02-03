package com.example.kr_linechatappication.notifications;

import com.example.kr_linechatappication.datas.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {

        String refreshToken = token;
        if(UserInfo.getInstance().getName() != null) {
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(UserInfo.getInstance().getName()).setValue(token);
    }
}
