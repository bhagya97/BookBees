package com.example.finalsample1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.Toolbar;


import androidx.appcompat.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //private Toolbar maiBar;

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                // Check if user is signed in (non-null) and update UI accordingly.
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                //updateUI(currentUser);
                if(currentUser == null){
                    sendtoStart();
                    finish();
                }
                else{
                    Intent intentStart = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intentStart);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void sendtoStart() {
        Intent intentStart = new Intent(MainActivity.this, UserLogin.class);
        startActivity(intentStart);
        finish();
    }
}
