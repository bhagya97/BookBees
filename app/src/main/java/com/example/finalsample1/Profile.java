package com.example.finalsample1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalsample1.ProfileUpdateActivity.UpdateBio;
import com.example.finalsample1.ProfileUpdateActivity.UpdateGenre;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.graphics.ImageDecoder.createSource;

public class Profile extends Fragment {

    private static final String TAG = "ProfileFragment";
    public ImageView profile_picture, edit_bio, edit_genre;
    private TextView bio;
    private TextView name;
//    private static Bitmap[] sendBitmap;
    private static ArrayList<Bitmap> sendBitmap = new ArrayList<>();


    private static ArrayList<String> bookimglist_tosend = new ArrayList<>();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private Object ContentResolver;
    ChipGroup chipGroup;
    List<String> genre_list_2 = new ArrayList<>();

    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_picture = view.findViewById(R.id.profile_picture);
        edit_bio = view.findViewById(R.id.iv_bio_edit);
        edit_genre = view.findViewById(R.id.iv_genres_edit);
        bio = view.findViewById(R.id.tv_bio);
        name = view.findViewById(R.id.display_name);
        chipGroup = view.findViewById(R.id.genres_chipgroup);
        gridView = view.findViewById(R.id.grid_library);
        gridView.setAdapter(new LibraryAdapter(this.getContext()));

        edit_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_bio_click();
            }
        });

        edit_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_genre_click();
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage(getContext());
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        //Firebase data retrieval
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //to retrieve bio from firebase
               bio.setText(dataSnapshot.child("bio").getValue(String.class));
               //to retrieve name from firebase
               name.setText(dataSnapshot.child("name").getValue(String.class));

               //to retrieve profile picture from firebase
               String encodedImage = dataSnapshot.child("dp").getValue(String.class);
               if (encodedImage != null) {
                   byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                   Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                   profile_picture.setImageBitmap(decodedByte);
               }

               //to retrieve favorite genres from firebase
               List<String> genre_list = new ArrayList<>();
               for (DataSnapshot ds : dataSnapshot.child("genres").getChildren()) {
                   String genres = ds.getValue(String.class);
                   genre_list.add(genres);
//                    Log.d("Genre", genres);
               }

               //printing favorite tags on profile page
               chipGroup.removeAllViews();
               LayoutInflater layoutInflater = LayoutInflater.from(getContext());
               int i = 0;
               Log.d("genre_count", String.valueOf(i));
               while (i < genre_list.size()) {
                   Chip chip = (Chip) layoutInflater.inflate(R.layout.layout_selected_genres, null, false);
                   chip.setText(genre_list.get(i));
                   chipGroup.removeView(getView());
                   chipGroup.addView(chip);
                   i++;
               }
           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //to retrieve book Images from firebase
        DatabaseReference bookReference = firebaseDatabase.getReference("books");
        bookReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> book_img_list = new ArrayList<>();
                String book_img_string, key;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    book_img_string = ds.child("Image").getValue(String.class);
                    book_img_list.add(book_img_string);
                    Log.d("book_string", String.valueOf(book_img_string));
                 }
//                bookimglist_tosend = book_img_list;
                Bitmap decodedByte = null;
                ArrayList<String> encodedImage = new ArrayList<>();
                encodedImage = book_img_list;
//                String encodedImage = book_img_list.get(0);
                int i=0;
                while (i < encodedImage.size()) {
                    byte[] decodedString = Base64.decode(encodedImage.get(i), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                    test.setImageBitmap(decodedByte);
//                    testImage = test;
                    sendBitmap.add(decodedByte);
                    i++;
                }
//                sendBitmap = decodedByte;


//                Log.d("bookimgsize", String.valueOf(book_img_list.size()));
                Log.d("bookimgarray", String.valueOf(book_img_list));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return view;
    }

    public static ArrayList<Bitmap> getBookBitmap()
    {
        return sendBitmap;
    }

    public View onCreate(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_library);
        return view;

    }

    public static ArrayList getImageArrayList()
    {
        return bookimglist_tosend;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void edit_genre_click() {
        Intent intentMain = new Intent(getActivity(), UpdateGenre.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intentMain);
    }

    private void edit_bio_click() {
        Intent intentMain = new Intent(getActivity(), UpdateBio.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intentMain);
    }

    //To Update profile picture
    private void updateImage(Context context) {
        final CharSequence[] options = { "Open Camera", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Your Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera")) {
                    Intent clickPicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(clickPicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent choosePicture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(choosePicture , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap clickedImage = (Bitmap) data.getExtras().get("data");
//                        profile_picture.setImageBitmap(clickedImage);

                        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = curUser.getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
                        DatabaseReference ref=databaseReference.child(uid);
                        
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        clickedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteFormat = stream.toByteArray();
                        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                        Map<String, Object> update = new HashMap<String,Object>();
                        update.put("dp", encodedImage);
                        ref.updateChildren(update);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
//                        profile_picture.setImageURI(selectedImage);

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = curUser.getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
                        DatabaseReference ref=databaseReference.child(uid);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteFormat = stream.toByteArray();
                        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                        Map<String, Object> update = new HashMap<String,Object>();
                        update.put("dp", encodedImage);
                        ref.updateChildren(update);
                    }
                    break;
            }
        }
    }
    //profile picture updation code ends here
}
