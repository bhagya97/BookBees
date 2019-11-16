package com.example.finalsample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mainBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
         /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment,new Message());
        fragmentTransaction.commit();*/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.navigation_messages :
                        fragmentTransaction.replace(R.id.fragment,new Message());
                        menuItem.setChecked(true);
                        fragmentTransaction.commit();
                        Toast.makeText(HomePage.this, " messages", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.navigation_find :
                        fragmentTransaction.replace(R.id.fragment,new Find());
                        Toast.makeText(HomePage.this, " find", Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        fragmentTransaction.commit();

                        break;
                    case  R.id.navigation_profile :
                        fragmentTransaction.replace(R.id.fragment,new Profile());
                        Toast.makeText(HomePage.this, "Profile", Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        fragmentTransaction.commit();

                        break;
                }

                return false;
            }
        });


        mainBar = (Toolbar) findViewById(R.id.userMenu);
        setSupportActionBar(mainBar);
        getSupportActionBar().setTitle("Messages");
    }


    private void sendtoStart() {

        Intent intentStart = new Intent(HomePage.this, StartPage.class);
        startActivity(intentStart);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            sendtoStart();

        }

        return true;
    }

}
