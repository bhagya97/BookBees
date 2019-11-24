package com.example.finalsample1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private String vchatUser,vCurrentUser;

    private Toolbar vchatBar;

    private DatabaseReference vRootref;

    private TextView displayName;

    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;

    private ImageButton vchatAddfiles;
    private ImageButton vchatSendmsg;
    private EditText vChatMessage;

    private RecyclerView vMessageList;

    private final List<Displaymsgs> vMessages = new ArrayList<>();
    private LinearLayoutManager vLinearLaayout;

    private MessageAdapter vMessageAdapter;

    private static final int Total_Items_To_Load = 10;

    private int mCurrrentPage = 1;

    private SwipeRefreshLayout vRefreshLayout;

    //private DataSnapshot dataSnapshot;

    private int itemPosition = 0;

    private String mLastKey;

    private String mPrevKey;

    private static final int GALLERY_PICK = 1;

    //private StorageReference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        vchatBar = (Toolbar) findViewById(R.id.chatActivityBar);
        setSupportActionBar(vchatBar);

        mAuth = FirebaseAuth.getInstance();
        vCurrentUser = mAuth.getCurrentUser().getUid();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        vRootref = FirebaseDatabase.getInstance().getReference();

        vchatUser = getIntent().getStringExtra("user_id");
        String chat_user_name = getIntent().getStringExtra("user_name");

        getSupportActionBar().setTitle(chat_user_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(action_bar_view);

        vchatAddfiles = (ImageButton) findViewById(R.id.addfiles);
        vchatSendmsg = (ImageButton) findViewById(R.id.sendmsg);
        vChatMessage = (EditText) findViewById(R.id.entermsg);

        vMessageAdapter = new MessageAdapter(vMessages);

        vMessageList = (RecyclerView) findViewById(R.id.messageView);

        vRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);

        vLinearLaayout = new LinearLayoutManager(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //vMessageList.setHasFixedSize(true);
        vMessageList.setLayoutManager(vLinearLaayout);

        vMessageList.setAdapter(vMessageAdapter);

        loadMessages();

        displayName = (TextView) findViewById(R.id.displayName);
        displayName.setText(chat_user_name);

        vRootref.child("chat").child(vCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(vchatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chat/"+ vCurrentUser + "/"+vchatUser,chatAddMap);
                    chatUserMap.put("chat/" + vchatUser + "/" + vCurrentUser, chatAddMap);

                    vRootref.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


/*
        vRootref.child("users").child(vchatUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chat_user_name = dataSnapshot.child("name").getValue().toString();

                getSupportActionBar().setTitle(chat_user_name);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 */


        vchatSendmsg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                sendMessage();
            }


        });

        vchatAddfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"), GALLERY_PICK);
            }
        });

        vRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrrentPage++;
                itemPosition = 0;
                //vMessages.clear();

                loadMoreMessages();

            }
        });


    }

    private void loadMoreMessages() {

        DatabaseReference msgRefmore = vRootref.child("messages").child(vCurrentUser).child(vchatUser);

        Query moremessageQuery = msgRefmore.orderByKey().endAt(mLastKey).limitToLast(10);

        moremessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Displaymsgs msg = dataSnapshot.getValue(Displaymsgs.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)){
                    vMessages.add(itemPosition++,msg);
                } else {

                    mPrevKey = mLastKey;
                }


                if (itemPosition == 1){

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS","Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + "| Message Key : " + messageKey);

                vMessageAdapter.notifyDataSetChanged();

                //vMessageList.scrollToPosition(vMessages.size()-1);

                vRefreshLayout.setRefreshing(false);

                vLinearLaayout.scrollToPositionWithOffset(10,0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {

        String message = vChatMessage.getText().toString();

        if (!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/" + vCurrentUser + "/" + vchatUser;
            String chat_user_ref = "messages/" + vchatUser + "/" + vCurrentUser;

            DatabaseReference user_message_push = vRootref.child("messages").child(vCurrentUser).child(vchatUser).push();

            String pushID = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from",vCurrentUser);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+pushID,messageMap);
            messageUserMap.put(chat_user_ref+"/"+pushID,messageMap);

            vRootref.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if(databaseError!=null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    }
                }
            });

            vChatMessage.setText("");

        }

        //loadMessages();

    }

    private void loadMessages(){

        DatabaseReference msgRef = vRootref.child("messages").child(vCurrentUser).child(vchatUser);

        Query messageQuery = msgRef.limitToLast(mCurrrentPage * Total_Items_To_Load);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Displaymsgs msg = dataSnapshot.getValue(Displaymsgs.class);

                itemPosition++;

                if (itemPosition == 1){

                    mLastKey = dataSnapshot.getKey();
                    mPrevKey = mLastKey;

                }

                vMessages.add(msg);
                vMessageAdapter.notifyDataSetChanged();

                vMessageList.scrollToPosition(vMessages.size()-1);

                vRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Image - ujwal - step1");

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){

            System.out.println("Image - ujwal - step1.1");

             Uri imageUri = data.getData();

            System.out.println("Image - ujwal - step1.2");
            final String current_user_ref = "messages/" + vCurrentUser + "/" + vchatUser;
            final String chat_user_ref = "messages/" + vchatUser + "/" + vCurrentUser;

            System.out.println("Image - ujwal - step1.3");

            DatabaseReference user_message_push = vRootref.child("messages").child(vCurrentUser).child(vchatUser).push();

            System.out.println("Image - ujwal - step1.4");

            final String push_id = user_message_push.getKey();

            System.out.println("Image - ujwal - step1.5");
            //System.out.println(push_id);

            //final StorageReference filePath = mStorageRef.child("message_images").child( push_id + ".jpg");



            final StorageReference filePath = mStorageRef.child("images").child(push_id+".jpg");

            System.out.println("Image - ujwal - step1.6");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    System.out.println("Image - ujwal - step2");

                    if(task.isSuccessful()){

                        //String download_url = task.getResult().getDownloadUrl().toString();

                        String download_url = filePath.getDownloadUrl().toString();

                        //String download_url = task.getResult().getMetadata().getReference().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message", download_url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", vCurrentUser);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        vChatMessage.setText("");

                        System.out.println("Image - ujwal - step3");

                        vRootref.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                System.out.println("Image - ujwal - step4");

                                if(databaseError != null){

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }

                            }
                        });


                    }

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            System.out.println("Image - ujwal - step3");

                            //System.out.println(exception.getCause().getLocalizedMessage());

                            Toast.makeText(ChatActivity.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            ;


        }
    }
}
