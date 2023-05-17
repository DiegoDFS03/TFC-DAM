package com.example.gamesaverx.gamesaverx.Utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Utils {

    public static boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
