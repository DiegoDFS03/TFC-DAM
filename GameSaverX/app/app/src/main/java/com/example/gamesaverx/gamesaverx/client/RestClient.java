package com.example.gamesaverx.gamesaverx.client;


import android.content.Context;

import com.android.volley.RequestQueue;

public class RestClient {

    private String BASE_REAL_URL = "http://10.0.2.2:8000";

    private Context context;

    private RequestQueue queue;

    private RestClient(Context context){
        this.context = context;
    }
    private static RestClient singleton = null;

    public static RestClient getInstance(Context context){
        if (singleton==null){
            singleton = new RestClient(context);
        }
        return singleton;
    }
}
