package com.example.gamesaverx.gamesaverx.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Utils.Utils;
import com.example.gamesaverx.gamesaverx.client.RestClient;

public class Login extends AppCompatActivity {
    private Button loginButton, registerButton;
    private EditText email,password;

    private Context context = this;

    private RestClient restClient = RestClient.getInstance(context);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginListener);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerListener);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (email.getText().length() == 0) {
                email.setError("Campo obligatorio");
            } else if(!Utils.validateEmail(email.getText().toString())){
                email.setError("Email no valido");
            } else if( password.getText().length() == 0) {
                password.setError("Campo obligatorio");
            } else {
                restClient.login(email, password, context);
            }


        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Register.class);
            startActivity(intent);
        }
    };
}