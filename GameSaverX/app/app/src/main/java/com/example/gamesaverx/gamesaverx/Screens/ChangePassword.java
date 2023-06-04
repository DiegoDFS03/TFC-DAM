package com.example.gamesaverx.gamesaverx.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.client.RestClient;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPassword, newPassword, newPasswordConfirm;
    private Button Button;
    private TextView showPassword;
    private boolean hide = true;
    private RestClient restClient = RestClient.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassword = findViewById(R.id.oldPassword);

        newPassword = findViewById(R.id.newPassword);

        newPasswordConfirm = findViewById(R.id.newPasswordConfirm);

        Button = findViewById(R.id.Button);
        Button.setOnClickListener(buttonListener);

        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(showPasswordListener);
    }
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (oldPassword.getText().length() == 0 )
                oldPassword.setError("Campo obligatorio");
            else if (newPassword.getText().length() == 0)
                newPassword.setError("Campo obligatorio");
            else if (newPasswordConfirm.getText().length() == 0)
                newPasswordConfirm.setError("Campo obligatorio");
            else if (!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString()))
                newPassword.setError("Las contraseñas no coinciden");
            else {
                restClient.changePassword(oldPassword, newPassword);
            }
        }
    };

    View.OnClickListener showPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (oldPassword.getText().toString().isEmpty()) {
                oldPassword.setError("Campo vacío");
            }if (newPassword.getText().toString().isEmpty()) {
                newPassword.setError("Campo vacío");
            }if (newPasswordConfirm.getText().toString().isEmpty()) {
                newPasswordConfirm.setError("Campo vacío");
            }
            if (hide) {
                hide = false;
                oldPassword.setTransformationMethod(null);
                newPassword.setTransformationMethod(null);
                newPasswordConfirm.setTransformationMethod(null);
            } else {
                hide = true;
                oldPassword.setTransformationMethod(new PasswordTransformationMethod());
                newPassword.setTransformationMethod(new PasswordTransformationMethod());
                newPasswordConfirm.setTransformationMethod(new PasswordTransformationMethod());
            }

        }
    };
}