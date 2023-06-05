package com.example.gamesaverx.gamesaverx.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.client.RestClient;

public class EditProfile extends AppCompatActivity {

    private EditText nombre, apellidos;
    private Button confirmar;

    private RestClient restClient = RestClient.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nombre = findViewById(R.id.editname);
        apellidos = findViewById(R.id.editsurnames);


        confirmar = findViewById(R.id.confirmar);
        confirmar.setOnClickListener(editListener);

        restClient.fillProfile(nombre, apellidos);
    }
    //Listener que comprueba que los campos de los edittext no están vacios
    private View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (nombre.getText().length() == 0) {
                nombre.setError("Campo obligatorio");
            }if (apellidos.getText().length() == 0) {
                apellidos.setError("Campo obligatorio");
            }
            if(nombre.getError() == null && apellidos.getError() == null){
                restClient.editProfile(nombre, apellidos);
            }
        }
    };
}