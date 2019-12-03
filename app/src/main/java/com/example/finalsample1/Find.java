//back up copy of 24-11-2019

package com.example.finalsample1;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Find extends Fragment {


    //code changed 2019-11-15

    private GridView gridView;
    private ImageView imageView;
    private  SearchView searchView;
    private int count;
    private String[] columns = {"name", "books" , "InterestedGenre"};

    //list of userdetails object

    private List<UserDetails> userDetailsList=new ArrayList<>();
    CustomGridViewAdapter customGridViewAdapter;
    private CustomGridViewAdapter saveCustomGridViewInstance;


    //End of today change



    public Find() {
        // Required empty public constructor
    }

    String lastQuery;

    // TODO: Rename and change types and number of parameters
    @Override
    public void onSaveInstanceState(Bundle outState) {
        System.out.println("Saving state fragment");
        //outState.putSerializable("adapter",saveCustomGridViewInstance);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(,params);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        HomePage activity = (HomePage) getActivity();
        String myDataFromActivity = activity.getLastQuery();
        System.out.println("myDataFromActivity" + myDataFromActivity);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");

        if(savedInstanceState != null)
        {
            System.out.println("Here if");
            View view = inflater.inflate(R.layout.fragment_find, container, false);
            gridView = (GridView) view.findViewById(R.id.gridViewCustom);
            gridView.setAdapter(customGridViewAdapter);
            return view;

        }
        else {
            System.out.println("Here else");
            View view = inflater.inflate(R.layout.fragment_find, container, false);
            gridView = (GridView) view.findViewById(R.id.gridViewCustom);
            return view;
        }

    }


    void searchLogic(final String query)
    {
        userDetailsList.clear();
        System.out.println("Query:" + query);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("userdetails");
        for(int i =0; i < columns.length ;i++)
        {
            databaseReference.orderByChild(columns[i]).equalTo(query).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot d : dataSnapshot.getChildren()
                        ) {
                            if(userDetailsList.isEmpty()) {
                                UserDetails userDetails = new UserDetails();
                                Bitmap decodedByte = userDetails.convertToImage(d.child("profile").getValue().toString());
                                userDetails.setProfileName(d.child("name").getValue().toString());
                                userDetails.setProfileLocation(d.child("location").getValue().toString());
                                userDetails.setUserUid(d.child("uid").getValue().toString());
                                userDetailsList.add(userDetails);
                            } else {
                                UserDetails userDetails = new UserDetails();
                                Bitmap decodedByte = userDetails.convertToImage(d.child("profile").getValue().toString());
                                userDetails.setProfileName(d.child("name").getValue().toString());
                                userDetails.setProfileLocation(d.child("location").getValue().toString());
                                userDetails.setUserUid(d.child("uid").getValue().toString());
                                if(userDetailsList.contains(userDetails))
                                {
                                    System.out.println("Already exist");
                                }
                                else {
                                    userDetailsList.add(userDetails);
                                }

                            }
                        }
                        customGridViewAdapter = new CustomGridViewAdapter(userDetailsList, getContext());
                        gridView.setAdapter(customGridViewAdapter);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d:dataSnapshot.getChildren()
                    ) {
                        String countValue=String.valueOf(d.getValue());
                        System.out.println("count:"+countValue);
                        String regex=query.substring(0, query.length() / 2)+".*";
                        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                        for( int i =0 ;i<columns.length;i++) {

                            Matcher m = p.matcher(d.child(columns[i]).getValue().toString());
                            System.out.println("Regex" + regex);
                            boolean result = m.matches();
                            if(i == 2)
                            {
                                String s=d.child(columns[i]).getValue().toString();
                                if(s.contains(query)) {
                                    result = true;
                                }
                            }
                            if (result) {
                                if(userDetailsList.isEmpty()) {
                                    UserDetails userDetails = new UserDetails();
                                    Bitmap decodedByte = userDetails.convertToImage(d.child("profile").getValue().toString());
                                    userDetails.setProfileName(d.child("name").getValue().toString());
                                    userDetails.setProfileLocation(d.child("location").getValue().toString());
                                    userDetails.setUserUid(d.child("uid").getValue().toString());
                                    userDetailsList.add(userDetails);
                                } else {
                                    UserDetails userDetails = new UserDetails();
                                    Bitmap decodedByte = userDetails.convertToImage(d.child("profile").getValue().toString());
                                    userDetails.setProfileName(d.child("name").getValue().toString());
                                    userDetails.setProfileLocation(d.child("location").getValue().toString());
                                    userDetails.setUserUid(d.child("uid").getValue().toString());
                                    if(userDetailsList.contains(userDetails))
                                    {
                                        System.out.println("Already exist");
                                    }
                                    else
                                    {
                                        userDetailsList.add(userDetails);
                                    }

                                }
                            }
                        }
                    }
                    customGridViewAdapter=new CustomGridViewAdapter(userDetailsList,getContext());
                    saveCustomGridViewInstance = customGridViewAdapter;
                    gridView.setAdapter(customGridViewAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        HomePage activity = (HomePage) getActivity();
        //activity.setLastQuery(query);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu_items, menu);
        searchView=(SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        /*ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.RIGHT);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(searchView,params);*/
        final int[] count;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchLogic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                if (searchView.getQuery().length() == 0) {
                    userDetailsList.clear();
                    customGridViewAdapter=new CustomGridViewAdapter(userDetailsList,getContext());
                    saveCustomGridViewInstance = customGridViewAdapter;
                    gridView.setAdapter(customGridViewAdapter);

                }
                else
                {
                    searchLogic(query);
                }
                System.out.println("newText:" +query);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
