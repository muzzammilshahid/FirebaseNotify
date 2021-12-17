package com.deskconn.firebasenotify;

import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class MainActivity extends AppCompatActivity {

    public String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
            sendFcmToken(android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID),s);
        });
    }

    public void sendFcmToken(String deviceId, String fcmToken){
        System.out.println("helloo  " + deviceId + "   " + fcmToken);
        HttpRequest request = new HttpRequest();
        request.setOnResponseListener(response -> {
            System.out.println("hellooo  in success" + response.text + response.code);
            if (response.code == HttpResponse.HTTP_CREATED) {

                System.out.println(response.toJSONObject());
            }
        });
        request.setOnErrorListener(error -> {
            // There was an error, deal with it
        });

        JSONObject json;
        try {
            json = new JSONObject();
            json.put("device", deviceId);
            json.put("fcm_token", fcmToken);
        } catch (JSONException ignore) {
            return;
        }
        request.post(url + "api/add_device/", json);
    }
}