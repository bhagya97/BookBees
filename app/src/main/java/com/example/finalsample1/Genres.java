package com.example.finalsample1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class Genres extends AppCompatActivity implements View.OnClickListener{

    List<String> selected_genre_list = new ArrayList<>();
    String profileGenre="";
    ChipGroup chipGroup;
    Button btn, btn_skip;
    String genresSelected ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genres_selection);
        final RegisterUserDetails registerUserDetails=(RegisterUserDetails) getIntent().getSerializableExtra("reg_user");
        // initiate views
        chipGroup = (ChipGroup)findViewById(R.id.chipgroup_genre);

        btn=findViewById(R.id.next_genres);
        btn_skip=(Button)findViewById(R.id.skip_genres);
        //btn.setOnClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* genresSelected = genresSelected.substring(0, genresSelected.length() -1 );
                registerUserDetails.setUserGenre(genresSelected);
                uploadData(registerUserDetails);
                Intent btomIntent = new Intent(Genres.this,SelectBooks.class);
                btomIntent.putExtra("reg_user",registerUserDetails);
                startActivity(btomIntent);*/

                Log.d( "My Logggg","inside on click" );

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
                            profileGenre += string +",";

                        }
                        i++;
                    }
                    selected_genre_list = Arrays.asList(selected_genres);
                    profileGenre=profileGenre.substring(0,profileGenre.length() - 1);


                    Log.d( "My Logggg","before genre list" );

                    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = curUser.getUid();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = firebaseDatabase.getReference("users").child( uid );

                    Map<String, Object> update = new HashMap<String,Object>();
                    update.put("genres", selected_genre_list);
                    update.put("genresSearch",profileGenre);
                    update.put("uid",uid);
                    //Log.d( "My Logggg",selected_genre_list.get( 2 ));
                    ref.updateChildren(update);

                    Log.d( "My Logggg","before upload data" );

                    Intent save_intent = new Intent(Genres.this, SelectBooks.class);
                    startActivity(save_intent);

                    //ref.setValue( registerUserDetails );
                    //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    //databaseReference.child(uid).setValue(registerUserDetails);


                    //uploadData(registerUserDetails);
//
//                   Intent save_intent = new Intent(Genres.this, SelectBooks.class);
//                    startActivity(save_intent);


//                    int i = 0;
//                    while (i < chipsCount) {
//                        Chip chip = (Chip) chipGroup.getChildAt(i);
//                        if (chip.isChecked() ) {
//                            String string = chip.getText().toString();
//                            selected_genres[i] = string;
//                            profileGenre += string +",";
//                        }
//                        i++;
//                    }
//                    selected_genre_list = Arrays.asList(selected_genres);
//                  //  save_genre();
//                    profileGenre=profileGenre.substring(0,profileGenre.length() - 1);
//                    registerUserDetails.setUserGenre(profileGenre);

//
//                    System.out.println("AA:"+registerUserDetails.getProfileImage());
//                    System.out.println("AA:"+registerUserDetails.getProfileName());
//                    System.out.println("AA:"+registerUserDetails.getProfileAboutMe());
//                    System.out.println("AA:"+registerUserDetails.getUserGenre());
//                    uploadData(registerUserDetails);
                }

            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  uploadData(registerUserDetails);
                Intent inte = new Intent(Genres.this,HomePage.class);
                startActivity(inte);
            }
        });

    }

   /* private void save_genre() {

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");
        DatabaseReference ref=databaseReference.child(uid);

        Map<String, Object> update = new HashMap<String,Object>();
        update.put("genres", selected_genre_list);
        ref.updateChildren(update);

       // Intent save_intent = new Intent(Genres.this, SelectBooks.class);
        //startActivity(save_intent);
    } */

    void uploadData(RegisterUserDetails registerUserDetails)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(uid).setValue(registerUserDetails);
        Log.d( "My Logggg","inside upload data" );

        Intent save_intent = new Intent(Genres.this, SelectBooks.class);
        startActivity(save_intent);

    }
    @Override
    public void onClick(View view) {

      /*  switch (view.getId()) {
            case R.id.checkBox1:
                if (check1.isChecked())
                    Toast.makeText(getApplicationContext(), "Art", Toast.LENGTH_LONG).show();
                genresSelected += "Art,";
                break;
            case R.id.checkBox2:
                if (check2.isChecked())
                    Toast.makeText(getApplicationContext(), "Adventure", Toast.LENGTH_LONG).show();
                genresSelected +="Adventure,";
                break;
            case R.id.checkBox3:
                if (check3.isChecked())
                    Toast.makeText(getApplicationContext(), "Romance", Toast.LENGTH_LONG).show();
                genresSelected +="Biography,";
                break;
            case R.id.checkBox4:
                if (check4.isChecked())
                    Toast.makeText(getApplicationContext(), "Thriller", Toast.LENGTH_LONG).show();
                genresSelected +="History,";
                break;
            case R.id.checkBox5:
                if (check5.isChecked())
                    Toast.makeText(getApplicationContext(), "Unity 3D", Toast.LENGTH_LONG).show();
                genresSelected +="Unity 3D,";
                break;
           case R.id.button3:
                if(btn.callOnClick())
                {
                    Intent btomIntent = new Intent(Genres.this,SelectBooks.class);
                    startActivity(btomIntent);
                    break;
                 } */
    }


}
