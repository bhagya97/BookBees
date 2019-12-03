package com.example.finalsample1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Contact extends AppCompatActivity {
    private Button btn_contact;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        btn_contact = (Button) findViewById(R.id.contactout);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(Contact.this, settings.class);
                startActivity(inte);
            }
        });
    }
       /* btn_contact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //back
                        Intent inte = new Intent(Contact.this,settings.class);
                        startActivity(inte);
                return false;
            }
        });
    } */
}
