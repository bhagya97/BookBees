package com.example.finalsample1;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;




    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        System.out.println( "Step1 - MessageFragment" );
        mMainView = inflater.inflate(R.layout.fragment_message, container, false);

        System.out.println( "Step1.1 - MessageFragment" );
        mConvList = (RecyclerView) mMainView.findViewById(R.id.convo_list);
        mAuth = FirebaseAuth.getInstance();


        System.out.println( "Step1.2 - MessageFragment" );

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        System.out.println( "Step1.3 - MessageFragment" );

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(mCurrent_user_id);

        System.out.println( "Step1.4 - MessageFragment" );

        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);

        System.out.println( "Step1.5 - MessageFragment" );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        System.out.println( "Step1.6 - MessageFragment" );

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

        System.out.println( "Step1.7 - MessageFragment" );

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Chats");

        // Inflate the layout for this fragment
        return mMainView;

    }


    @Override
    public void onStart() {
        super.onStart();


        Log.d( "Fragments-OnStart","step1" );

        Query conversationQuery = mConvDatabase.orderByChild("timestamp");

        FirebaseRecyclerOptions<Convo> options = new FirebaseRecyclerOptions.Builder<Convo>()
                .setQuery(conversationQuery, Convo.class)
                .build();

        Log.d( "Fragments-OnStart","step1.2" );

        FirebaseRecyclerAdapter<Convo, ConvViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Convo, ConvViewHolder>(options) {

                /*
        FirebaseRecyclerAdapter<Convo, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<Convo, ConvViewHolder>(
                Convo.class,
                R.layout.chat_single_layout,
                ConvViewHolder.class,
                conversationQuery
        ) {
                 */


            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                Log.d( "Fragments-OnStart","step1.3" );
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_single_layout, parent, false);

                return new Message.ConvViewHolder( view );
            }

            @Override
            protected void onBindViewHolder(@NonNull final ConvViewHolder holder, int position, @NonNull final Convo model) {

                Log.d( "Fragments-OnStart","step2" );

                final String list_user_id = getRef(position).getKey();

                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Log.d( "Fragments-OnStart","step3" );
                        String data = dataSnapshot.child("message").getValue().toString();
                        holder.setMessage(data, model.isSeen());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

            });

                System.out.println( "Step2 - MessageFragment" );

                System.out.println( list_user_id);

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {

                            System.out.println(dataSnapshot);
                            System.out.println( "Step2.1 - MessageFragment" );

                            try {


                                final String userName = dataSnapshot.child( "name" ).getValue().toString();
                                String userThumb = dataSnapshot.child( "thumb_image" ).getValue().toString();

                                System.out.println( userName );

                                System.out.println( "Step2.2 - MessageFragment" );

                                if (dataSnapshot.hasChild( "online" )) {

                                    System.out.println( "Step2.3 - MessageFragment" );

                                    String userOnline = dataSnapshot.child( "online" ).getValue().toString();
                                    holder.setUserOnline( userOnline );

                                }

                                holder.setName( userName );
                                holder.setUserImage( userThumb, getContext() );

                                holder.mView.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        System.out.println( "Step3 - MessageFragment" );
                                        Intent chatIntent = new Intent( getContext(), ChatActivity.class );
                                        chatIntent.putExtra( "user_id", list_user_id );
                                        chatIntent.putExtra( "user_name", userName );
                                        startActivity( chatIntent );

                                    }
                                } );

                            }catch (NullPointerException e){
                                Log.d( "Fragments-OnStart","Error in datasnapshot username" );
                            }

                        }
                        else {
                            Log.d( "Fragments-OnStart","Error in datasnapshot" );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        Log.d( "Fragments-OnStart","step5" );

        firebaseRecyclerAdapter1.startListening();

        mConvList.setAdapter(firebaseRecyclerAdapter1);

        mConvList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        Log.d( "Fragments-OnStart","step5.1" );

    }

    public static class ConvViewHolder extends RecyclerView.ViewHolder {



        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);

            Log.d( "Fragments-OnStart","step6" );

            mView = itemView;

        }

        public void setMessage(String message, boolean isSeen){

            Log.d( "Fragments-OnStart","step6.1" );

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(message);

            Log.d( "Fragments-OnStart","step6.2" );

            if(!isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }

        }

        public void setName(String name){

            Log.d( "Fragments-OnStart","step6.3" );

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            Log.d( "Fragments-OnStart","step6.4" );

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.get().load(thumb_image).placeholder(R.drawable.baseline_face_black_18dp).into(userImageView);
            //Picasso.get().load(thumb_image).placeholder()

        }

        public void setUserOnline(String online_status) {

            Log.d( "Fragments-OnStart","step6.5" );

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }

}
