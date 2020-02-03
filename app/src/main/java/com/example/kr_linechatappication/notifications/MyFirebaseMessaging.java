package com.example.kr_linechatappication.notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.activities.MainActivity;
import com.example.kr_linechatappication.datas.UserInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String user = remoteMessage.getData().get("user");
        Log.v("Singleton", UserInfo.getInstance().getName());
        Log.v("Singleton", user+"");
        Log.v("Singleton", UserInfo.getInstance().getName().equals(user)+"");
        if(UserInfo.getInstance().getName().equals(user)) {
            sendNotification(remoteMessage);
        }
    }

    public void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 7000, intent, PendingIntent.FLAG_ONE_SHOT);

        OreaNotification oreoNotification = new OreaNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                R.drawable.ic_launcher);



        oreoNotification.getManager().notify(7000, builder.build());
    }
}
