package com.example.gamesaverx.gamesaverx.Screens;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamesaverx.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Drawer extends AppCompatActivity {
    //Declaración de los fragments
    Home homeFragment = new Home();
    Saved savedFragment = new Saved();

    Profile profileFragment = new Profile();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Para que se abra el "HomeFragment" desde el principio
        loadFragment(homeFragment);



    }

    //Método para mostrar cada fragment
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId()==R.id.action_home) {
                    loadFragment(homeFragment);
                    return true;
            } else if (item.getItemId()==R.id.action_saved) {
                loadFragment(savedFragment);
                return true;
            } else if (item.getItemId()==R.id.action_profile) {
                loadFragment(profileFragment);

            }
            return false;
        }
    };

    //Método para cambiar de fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed(){

    }

}
