package com.example.finalsample1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {
    private TextView contactus, faq;
    private Button logOut;
    private Button deleteAccount;
    FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.settings );
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        contactus = (TextView) findViewById( R.id.contact );
        faq = (TextView) findViewById( R.id.faq );
        logOut = (Button) findViewById( R.id.btn_logout );
        deleteAccount = (Button) findViewById( R.id.btn_delAccount );
        contactus.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent( settings.this, Contact.class );
                startActivity( inte );
                // Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

            }
        } );
        faq.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte1 = new Intent( settings.this, FAQ.class );
                startActivity( inte1 );
            }
        } );
        logOut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sendtoStart();
            }
        } );
        deleteAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( settings.this );
                dialog.setTitle( "Are you sure?" );
                dialog.setMessage( "Delete an account will completelty remove your account from the system and you wont be able to access" );
                dialog.setPositiveButton( "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser.delete().addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText( settings.this, "Account deleted", Toast.LENGTH_LONG ).show();
                                    Intent intent1 = new Intent( settings.this, UserLogin.class );
                                    intent1.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                    intent1.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity( intent1 );
                                } else {
                                    Toast.makeText( settings.this, task.getException().getMessage(), Toast.LENGTH_LONG ).show();
                                }
                            }
                        } );

                    }
                } );
                dialog.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();


                    }
                } );

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        } );

    }

    private void sendtoStart () {

        Intent intentStart = new Intent( settings.this, UserLogin.class );
        startActivity( intentStart );
        finish();
    }

}
   /* public void click(View view) {

        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
        Intent inte = new Intent(settings.this,Contact.class);
        startActivity(inte);
        Intent intent;
        switch(view.getId()) {
            case R.id.contact: // R.id.textView1
                intent = new Intent(settings.this, Contact.class);
                break;
            case R.id.faq: // R.id.textView2
                intent = new Intent(this, FAQ.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        startActivity(intent);
    } */




