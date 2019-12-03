package com.example.finalsample1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Displaymsgs> vMessageList;
    Displaymsgs c;
    private FirebaseAuth mAuth;
    public static final int MSG_RIGHT=0;
    public static final int MSG_LEFT=1;
    FirebaseUser fuser;


    private DatabaseReference vRootref = FirebaseDatabase.getInstance().getReference();


    public MessageAdapter(List<Displaymsgs> vMessageList) {
        this.vMessageList = vMessageList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_LEFT){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent, false);
            return new MessageViewHolder(v);
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_user_layout,parent, false);
            return new MessageViewHolder(v);
        }

    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText, userText;
        public ImageView profileImage, userImage;
        public ImageView messageImage;




        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message);
            profileImage = (ImageView) view.findViewById(R.id.profileimage);
            messageImage = (ImageView) view.findViewById(R.id.chatusersentimage);

//            userText = (TextView) view.findViewById(R.id.chatownmessage);
//          userImage = (ImageView) view.findViewById(R.id.chatownimage);




            }


    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        //String current_User = mAuth.getInstance().getCurrentUser().getUid();
        c = vMessageList.get(position);
        //String user_from = c.getFrom();


        String message_type = c.getType();
//        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT
//        );
//        imageParams=0;
//        imageParams.leftMargin=20;
        //params.setMargins(, 0, 0, 0);
        //yourbutton.setLayoutParams(params);

        try {

            if (message_type.equals( "text" )) {
                holder.messageText.setText(c.getMessage());
                if (holder.messageImage != null) {
                    holder.messageImage.setVisibility( View.VISIBLE );
                }

            } else {
                if (holder.messageText != null) {
                    holder.messageText.setVisibility( View.VISIBLE );
                }
                Picasso.get().load( c.getMessage() )
                        .fit()
                        .centerCrop()
                        .placeholder( R.drawable.baseline_image_black_24dp )
                        .into( holder.messageImage );
            }

            if (vMessageList.get(position).getFrom().equals(fuser.getUid())){
                vRootref.child( "users" ).child( fuser.getUid() ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String encodedImage = dataSnapshot.child("dp").getValue(String.class);
                        if (encodedImage != null) {
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holder.profileImage.setImageBitmap(decodedByte);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
            else{

            }
//                if (user_from.equals( current_User )) {
//
////                    holder.userText.setVisibility(View.VISIBLE);
//                    //holder.userImage.setVisibility(View.VISIBLE);
//                    holder.profileImage.setLayoutParams(imageParams);
//                    holder.messageText.setVisibility(View.VISIBLE);
//                    holder.messageText.setText( c.getMessage() );
//                    holder.profileImage.setVisibility(View.VISIBLE);
//
//                    //holder.userText.setGravity(Gravity.RIGHT);
//
//                } else {
//
//
//                    holder.profileImage.setVisibility(View.VISIBLE);
//                    holder.messageText.setText( c.getMessage() );
//
//
//                }

            //holder.messageText.setText(c.getMessage());

                /*
                if (holder.messageImage != null) {
                    holder.messageImage.setVisibility(View.INVISIBLE);
                }

                 */





            //Picasso.with(holder.profileImage.getContext()).load(c.getMessage())
            //      .placeholder(R.drawable.baseline_face_black_18dp).into(holder.messageImage);

            //Picasso.get().load(c.getMessage()).placeholder(R.drawable.baseline_image_black_24dp).into(holder.messageImage);



        } catch (NullPointerException e){
            Log.d( "Fragments-OnStart","Error in Message Adapter, onBindViewHolder" );

        }



    }

    @Override
    public int getItemCount() {
        return vMessageList.size();
    }

    @Override
    public int getItemViewType(int position){
        fuser = mAuth.getInstance().getCurrentUser();
        //String user_from = c.getFrom();
        if (vMessageList.get(position).getFrom().equals(fuser.getUid())){

            Log.d( "getFrom right", vMessageList.get(position).getFrom() );
            Log.d( "Fragments-OnStart","sending right" );
            return MSG_RIGHT;
        }
        else{
            Log.d( "getFrom left", vMessageList.get(position).getFrom() );
            Log.d( "Fragments-OnStart","sending left" );
            return MSG_LEFT;
        }
    }
}
