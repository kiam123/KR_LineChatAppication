package com.example.kr_linechatappication.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3LnJN8M:APA91bFPe3Ds16yLHu2FQdddGYtExv13gQhQNhvrd8RKWsag3lalIEmmgP9UK2shSNdTOdSCXnTYfU6CC3COkkNHLslQaFwJPykRrKhVFIDExkpiLMHCJLQvHq6XONljZ9Nco7zah8cm"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
