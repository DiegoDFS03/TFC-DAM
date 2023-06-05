package com.example.gamesaverx.gamesaverx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamesaverx.gamesaverx.Screens.Login;
import com.example.gamesaverx.gamesaverx.client.RestClient;

public class Launcher extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String sessionToken = preferences.getString("tokenSession", null);
    //Aquí se comprueba si hay algun token guardado en las SharedPreferences, si es así inicia sesión autoáticamente
        if (sessionToken == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } else {
            RestClient restClient = RestClient.getInstance(this);
            restClient.isLogged(sessionToken);
        }
        //Aquí se lanza el método para cada vez que se lanza la app borre las oferta expiradas
        RestClient restClient = RestClient.getInstance(this);
        restClient.deleteExpiredOffers();

    }
}
