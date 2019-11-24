package com.example.finalsample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AllUsers extends AppCompatActivity {

    private Toolbar vToolbar;

    private RecyclerView vUserslist;

    private DatabaseReference vUsersDatabase, sample1;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        vToolbar = (Toolbar) findViewById(R.id.allUsers_bar);
        setSupportActionBar(vToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //userID = FirebaseDatabase.getInstance().getReference().getKey();


        vUserslist = (RecyclerView) findViewById(R.id.usersListVIew);
        vUserslist.setHasFixedSize(true);
        //vUserslist.setLayoutManager(new LinearLayoutManager(this));
        //vUserslist.setLayoutManager(new GridLayoutManager(this));

    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("Step1 - ujwal");
        Query query = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();


        FirebaseRecyclerAdapter<Users, UsersView> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Users, UsersView>(options) {

            @NonNull
            @Override
            public UsersView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_name_layout, parent, false);

                return new UsersView(view);
            }

            @Override
            protected void onBindViewHolder(final UsersView holder, int position, Users model) {

                System.out.println("Step2 - ujwal");

                final String namel = model.getName();
                Query query = vUsersDatabase.orderByChild("name").equalTo(namel);
                System.out.println("Step2.1 - ujwal");
                System.out.println(namel);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("Step2.3 - ujwal");
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                            System.out.println(userSnapshot.getKey());
                            holder.setDisplayName(namel, userSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Step2.4 - ujwal");
                        System.out.println(databaseError);

                    }
                });


            }
        };

        System.out.println("Step3 - ujwal");

        firebaseRecyclerAdapter1.startListening();

        vUserslist.setAdapter(firebaseRecyclerAdapter1);

        vUserslist.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        System.out.println("Step4 - ujwal");
    }

    public static class UsersView extends RecyclerView.ViewHolder{

        View mView;

        public UsersView(View itemView){
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(final String name, final String userID){


            TextView userNameView = (TextView) mView.findViewById(R.id.userNameSingle);
            userNameView.setText(name);

            userNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent = new Intent(mView.getContext(), ChatActivity.class);

                    System.out.println(userID);
                    chatIntent.putExtra("user_id", userID);
                    chatIntent.putExtra("user_name", name);
                    mView.getContext().startActivity(chatIntent);

                }
            });
        }

    }

}
