package com.example.finalsample1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FAQ extends AppCompatActivity {
    private Button btn_faq;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_section);
        btn_faq = (Button) findViewById(R.id.button_back);
        btn_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(FAQ.this, settings.class);
                startActivity(inte);
            }
        });
    }
}
