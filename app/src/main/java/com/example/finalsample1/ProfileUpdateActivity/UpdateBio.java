package com.example.finalsample1.ProfileUpdateActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalsample1.Profile;
import com.example.finalsample1.R;
import com.example.finalsample1.RegisterPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateBio extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bio_edit);
        mAuth = FirebaseAuth.getInstance();
        final EditText bio_input = findViewById(R.id.et_bio);
        Button save = findViewById(R.id.btn_save);
        Button cancel = findViewById(R.id.btn_cancel);

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //to retrieve bio from firebase
                bio_input.setText(dataSnapshot.child("bio").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bio = bio_input.getText().toString();
//                Toast.makeText(UpdateBio.this,bio, Toast.LENGTH_LONG).show();
                save_bio(bio);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void cancel() {
        finish();
//        Intent cancel_intent = new Intent(UpdateBio.this, Profile.class);
//        startActivity(cancel_intent);
    }

    private void save_bio(final String bio) {

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        DatabaseReference ref1=databaseReference.child(uid);

        Map<String, Object> update = new HashMap<String,Object>();
        update.put("bio", bio);
        ref1.updateChildren(update);

        finish();

//        Intent save_intent = new Intent(UpdateBio.this, Profile.class);
//        startActivity(save_intent);
    }
}
