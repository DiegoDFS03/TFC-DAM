package com.example.gamesaverx.gamesaverx.client;


import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {

    private String BASE_URL = "http://10.0.2.2:8000";

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

    public void register(TextView editTextName, TextView editTextSurnames, TextView editTextEmail, TextView editTextPassword, TextView editTextPassword2) {
        queue = Volley.newRequestQueue(context);
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put("name", editTextName.getText().toString());
            requestBody.put("surnames", editTextSurnames.getText().toString());
            requestBody.put("email", editTextEmail.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "/v1/users",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(context,Login.class);
                        //context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "Sin conexión", Toast.LENGTH_LONG).show();

                        } else if(error.networkResponse.statusCode == 409) {
                            Toast.makeText(context, "Cuenta ya registrada", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context, "Error: " + serverCode, Toast.LENGTH_LONG).show();

                        }
                    }
                });
        this.queue.add(request);
    }
}
