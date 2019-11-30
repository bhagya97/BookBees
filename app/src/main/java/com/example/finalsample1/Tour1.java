package com.example.finalsample1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Tour1 extends AppCompatActivity {
    private Button btnnext, btnskip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour1);
        btnnext = findViewById(R.id.button23);
        btnskip = findViewById(R.id.skiptour1);

        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(Tour1.this, InitialProfile.class);
                startActivity(regIntent);
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(Tour1.this, Tour2.class);
                startActivity(regIntent);
            }
        });
    }
}
