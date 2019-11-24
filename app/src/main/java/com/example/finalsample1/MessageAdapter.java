package com.example.finalsample1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Displaymsgs> vMessageList;

    private FirebaseAuth mAuth;

    public MessageAdapter(List<Displaymsgs> vMessageList) {
        this.vMessageList = vMessageList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent, false);

        return new MessageViewHolder(v);
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public ImageView profileImage;
        public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.chatusermessage);
            profileImage = (ImageView) view.findViewById(R.id.chatuserimage);
            messageImage = (ImageView) view.findViewById(R.id.chatusersentimage);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String current_User = mAuth.getInstance().getCurrentUser().getUid();
        Displaymsgs c = vMessageList.get(position);
        String user_from = c.getFrom();

        String message_type = c.getType();

        if (user_from.equals(current_User)){

            holder.messageText.setBackgroundColor(Color.WHITE);
            holder.messageText.setTextColor(Color.BLACK);
            holder.profileImage.setImageResource(R.drawable.baseline_face_black_18dp);


        }else{

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);


        }

        //holder.messageText.setText(c.getMessage());
        if(message_type.equals("text")) {

            holder.messageText.setText(c.getMessage());
            /*
            if (holder.messageImage != null) {
                holder.messageImage.setVisibility(View.INVISIBLE);
            }

             */


        } else {



            if (holder.messageText != null) {
                holder.messageText.setVisibility(View.INVISIBLE);
            }



            //Picasso.with(holder.profileImage.getContext()).load(c.getMessage())
              //      .placeholder(R.drawable.baseline_face_black_18dp).into(holder.messageImage);

            //Picasso.get().load(c.getMessage()).placeholder(R.drawable.baseline_image_black_24dp).into(holder.messageImage);

            Picasso.get().load(c.getMessage())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.baseline_image_black_24dp)
                    .into(holder.messageImage);
        }



    }

    @Override
    public int getItemCount() {
        return vMessageList.size();
    }
}
