package com.example.gamesaverx.gamesaverx.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Utils.Utils;
import com.example.gamesaverx.gamesaverx.client.RestClient;

public class Register extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword,editTextPassword2,editTextName,editTextSurnames;
    private Button registerButton;

    private Context context = this;

    private RestClient restClient = RestClient.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextSurnames = findViewById(R.id.editTextSurnames);
        editTextPassword2 = findViewById(R.id.editTextTextPassword2);
        registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.validateEmail(editTextEmail.getText().toString()))
                    editTextEmail.setError("Email no válido");

                if (!editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())) {
                    editTextPassword.setError("Contraseñas diferentes");
                    editTextPassword2.setError("Contraseñas diferentes");
                }

                if (editTextPassword.length() == 0)
                    editTextPassword.setError("Falta Contraseña");

                if (editTextPassword2.length() == 0)
                    editTextPassword2.setError("Falta Contraseña");

                if (editTextName.length() == 0)
                    editTextName.setError("Falta Nombre");

                if (editTextSurnames.length() == 0)
                    editTextSurnames.setError("Falta Apellidos");

                if (editTextEmail.length() == 0)
                    editTextEmail.setError("Falta Email");

                if (editTextName.getError() == null && editTextPassword.getError() == null && editTextSurnames.getError() == null && editTextPassword2.getError() == null && editTextEmail.getError() == null ){
                    restClient.register(editTextName,editTextSurnames,editTextEmail,editTextPassword,editTextPassword2);

                }
            }
        });
    }
}
