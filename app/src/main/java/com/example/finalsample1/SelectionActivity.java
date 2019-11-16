package com.example.finalsample1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

public class SelectionActivity extends AppCompatActivity {

    private ViewPager slidePager;
    private LinearLayout linearLayout;

    private SliderAdapter sliderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        slidePager = (ViewPager) findViewById(R.id.slidePager);
        linearLayout=(LinearLayout) findViewById(R.id.linearlayout);

        sliderAdapter=new SliderAdapter(this);
        slidePager.setAdapter(sliderAdapter);
    }


}
