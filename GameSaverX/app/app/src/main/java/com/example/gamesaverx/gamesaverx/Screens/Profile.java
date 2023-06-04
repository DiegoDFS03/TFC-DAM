package com.example.gamesaverx.gamesaverx.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.client.RestClient;


public class Profile extends Fragment {
    private Context context;

    private Button editar, contraseña, csesion;

    private RestClient restClient;

    private TextView name, email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getActivity().getApplicationContext();

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);

        csesion = view.findViewById(R.id.csesion);
        csesion.setOnClickListener(csesionlistener);

        contraseña = view.findViewById(R.id.change);
        contraseña.setOnClickListener(contraseñalistener);

        editar = view.findViewById(R.id.edit);
        editar.setOnClickListener(editarlistener);

        peticion();

        return view;
    }

    View.OnClickListener csesionlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(context, Login.class);
            startActivity(intent);


        }
    };


    View.OnClickListener contraseñalistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,ChangePassword.class);
            startActivity(intent);
        }
    };

    View.OnClickListener editarlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


        }
    };
    private void peticion() {
        restClient = RestClient.getInstance(context);
        restClient.profile(name,email);
    }
}