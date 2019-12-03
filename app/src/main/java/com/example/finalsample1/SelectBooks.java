package com.example.finalsample1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SelectBooks extends AppCompatActivity {

    private static int check=0;
    public static List<Integer> imagelist= new ArrayList<Integer>();
    ImageView i1;
    //Button button;
    ImageButton button1;
    Button button2;
    ImageButton[] button= new ImageButton[50];
    static int[] imageview= {R.id.imageView1,R.id.imageView2,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6,
                        R.id.imageView7,R.id.imageView8,R.id.imageView9,R.id.imageView10,R.id.imageView11,R.id.imageView12};
    static int[] images= {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,
                        R.drawable.img7,R.drawable.img8,R.drawable.img9,R.drawable.img10,R.drawable.img11,R.drawable.img12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_layout1);
        i1= (ImageView) findViewById(R.id.imageView);
        //imageToBase64(R.drawable.img1);
        //button = (Button) findViewById(R.id.button);
        button1=(ImageButton) findViewById(R.id.imageView3);
        button2=(Button) findViewById(R.id.skip);
        for(int i=0;i<12;i++){
            button[i]= (ImageButton) findViewById(imageview[i]);
            buttonEffect(button[i],i);
        }
//        for(int j=1;j<imagelist.size()+1;j++){
//            System.out.println("Image:"+imagelist.get(j));
//        }


        //System.out.println("Imagelistttt"+imagelist);


    }


    public static void buttonEffect(View buttonView,int id){
        final View button=buttonView;
        final int no=id;
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        check+=1;
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        System.out.println("images[no]"+images[no]);
                        imagelist.add(images[no]);
                        //imagelist.add(button.getId())
                        System.out.println("book" + button+ "added");
                        v.invalidate();
                        break;
                    }
//                    case MotionEvent.ACTION_UP: {
//                        //v.getBackground().clearColorFilter();
//                        v.invalidate();
//                        break;
//                    }
                }
                if(check>1 && check%2==1){
                    v.getBackground().clearColorFilter();
                    //imagelist.remove(imagelist.indexOf(images[no]));
                    System.out.println("book" + button+ "deleted");
                }
                return false;
            }
        });


    }

    public void nextPage(View view) {
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = firebaseDatabase.getReference("users").child( uid );
        Map<String, Object> update = new HashMap<String,Object>();
        update.put("books", imagelist );
        //Log.d( "My Logggg",selected_genre_list.get( 2 ));
        ref.updateChildren(update);

        Intent gotoChat= new Intent(this,HomePage.class);
        startActivity(gotoChat);
        finish();

    }

    public String imageToBase64(int image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public void skipPage(View view) {

        Intent gotoChat= new Intent(this,HomePage.class);
        startActivity(gotoChat);
        finish();

    }
}
