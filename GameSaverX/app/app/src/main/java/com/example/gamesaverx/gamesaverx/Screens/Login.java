package com.example.gamesaverx.gamesaverx.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
    private TextView showpassword;
    private boolean hide = true;

    private Context context = this;

    private RestClient restClient = RestClient.getInstance(context);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showpassword = findViewById(R.id.mostrar);
        showpassword.setOnClickListener(showListener);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginListener);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerListener);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

    }
//Listener que comprueba los edittext y sino hay problema envía la petición
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
//Listener que lanza la clase Register cuando se pulsa
    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Register.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener showListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (password.getText().toString().isEmpty()) {
                password.setError("Falta Contraseña");
            } else {
                if (hide) {
                    hide = false;
                    password.setTransformationMethod(null);
                } else {
                    hide = true;
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        }
    };

    //Método que sobrecarga el boton Back y solo permite salir de la aplicacion con el boton Home
    @Override
    public void onBackPressed(){

    }
}