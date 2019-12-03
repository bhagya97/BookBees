package com.example.finalsample1.ProfileUpdateActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalsample1.Profile;
import com.example.finalsample1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateGenre extends AppCompatActivity {
    List<String> selected_genre_list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_genres);

        Button save = findViewById(R.id.btn_save);
        Button cancel = findViewById(R.id.btn_cancel);

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //to retrieve favorite genres from firebase
                List<String> genre_list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.child("genres").getChildren()) {
                    String genres = ds.getKey();
                    genre_list.add(genres);
                }
//                Log.d("genreslist", String.valueOf(genre_list));

//                printing favorite tags on profile page
                ChipGroup chipGroup = findViewById(R.id.chipgroup_genre);
                int i = 0;
//                Log.d("genre_count", String.valueOf(i));
                while (i < genre_list.size()) {
                    Chip chip = (Chip) chipGroup.getChildAt(Integer.parseInt(genre_list.get(i)));
                    chip.setChecked(true);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selected_genres = new String[35];

                ChipGroup chipGroup = findViewById(R.id.chipgroup_genre);
                int chipsCount = chipGroup.getChildCount();
                if (chipsCount == 0) {
                    Toast.makeText(getApplicationContext(),"Please select at least one genre.", Toast.LENGTH_LONG).show();
                } else {
                    int i = 0;
                    while (i < chipsCount) {
                        Chip chip = (Chip) chipGroup.getChildAt(i);
                        if (chip.isChecked() ) {
                            String string = chip.getText().toString();
                            selected_genres[i] = string;
                        }
                        i++;
                    }
                    selected_genre_list = Arrays.asList(selected_genres);
                    save_genre();
                }
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
//            Intent cancel_intent = new Intent(UpdateGenre.this, Profile.class);
//            startActivity(cancel_intent);
        }

        private void save_genre() {

            FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = curUser.getUid();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
            DatabaseReference ref=databaseReference.child(uid);

            Map<String, Object> update = new HashMap<String,Object>();
            update.put("genres", selected_genre_list);
            ref.updateChildren(update);

            finish();
//            Intent save_intent = new Intent(UpdateGenre.this, Profile.class);
//            startActivity(save_intent);
        }
}
