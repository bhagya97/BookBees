package com.example.finalsample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    private EditText loginEmail, loginPass;
    private Button loginButton;
    private Button signUp;
    private FirebaseAuth mAuth;
    private Toolbar loginBar;
    private ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);
        loginButton = (Button) findViewById(R.id.userLogin);
        signUp = (Button) findViewById(R.id.registerBtn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        /*
        final Toolbar loginBar = (Toolbar) findViewById(R.id.loginBar);
        setSupportActionBar(loginBar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         */

        loginProgress = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginEmail.getText().toString();
                String userPass = loginPass.getText().toString();
                if (!TextUtils.isEmpty(userEmail) || !TextUtils.isEmpty(userPass)) {

                    loginProgress.setTitle("Logging In");
                    loginProgress.setMessage("Please Hold On!");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    loggeduser(userEmail, userPass);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(UserLogin.this, RegisterPage.class);
                startActivity(regIntent);
            }
        });
    }

    private void loggeduser(String userEmail, String userPass) {

    mAuth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){

                loginProgress.dismiss();
                Toast.makeText(UserLogin.this,"In Progress", Toast.LENGTH_LONG).show();
                Intent btomIntent = new Intent(UserLogin.this,HomePage.class);
                startActivity(btomIntent);
                finish();
                Toast.makeText(UserLogin.this,"Success!!", Toast.LENGTH_LONG).show();
            }
            else {
                loginProgress.hide();
                Toast.makeText(UserLogin.this,"Please enter valid Email.", Toast.LENGTH_LONG).show();
            }
        }
    });

    }
}
