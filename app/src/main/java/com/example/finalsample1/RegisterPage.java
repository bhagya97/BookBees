package com.example.finalsample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    private EditText disName, regEmail, regPass;
    private Button regButton;
    private FirebaseAuth mAuth;
    private Toolbar regBar;
    private ProgressDialog regProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        disName = (EditText) findViewById(R.id.disName);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regPass = (EditText) findViewById(R.id.regPass);
        regButton = (Button) findViewById(R.id.registerUser);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = disName.getText().toString();
                String userEmail = regEmail.getText().toString();
                String userPass = regPass.getText().toString();
                if (!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(userEmail) || !TextUtils.isEmpty(userPass)) {

                    regProgress.setTitle("Registering User");
                    regProgress.setMessage("Please wait while we create your account!");
                    regProgress.setCanceledOnTouchOutside(false);
                    regProgress.show();
                    new_user(displayName, userEmail, userPass);
                }

            }
        });

        Toolbar mainBar = (Toolbar) findViewById(R.id.registerMenu);
        setSupportActionBar(mainBar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regProgress = new ProgressDialog(this);

    }


    private void new_user(String displayName, String userEmail, String userPass) {

        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    regProgress.dismiss();
                    Toast.makeText(RegisterPage.this,"In Progress", Toast.LENGTH_LONG).show();
                    Intent btomIntent = new Intent(RegisterPage.this,HomePage.class);
                    startActivity(btomIntent);
                    finish();
                    Toast.makeText(RegisterPage.this,"Success!!", Toast.LENGTH_LONG).show();


                } else {
                    regProgress.hide();
                    Toast.makeText(RegisterPage.this,"ERROR!!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}
