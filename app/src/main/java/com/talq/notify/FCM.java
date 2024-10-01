package com.talq.notify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by CodeSlu on 9/4/2018.
 */

public interface FCM {
    @Headers({

            "Content-Type: application/json",
            "Authorization:key=AAAA-wuTVLk:APA91bF9yoKcJHi0DppvUSlGTsnmm36a29BHLs-vO1S3OriJDb7SgIYnt3Q4hbRGyswMVQJyP2Rs38qu8tkeZHpcILxcOKAptWgMBI4kPWsK9nb0cRIBQPIs0IfoJwwzdhzNFPhd5Kc4"
    })
    @POST("fcm/send")
    Call<FCMresp> send(@Body Sender body);
}
