package com.lonelygamer.dmc;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingSignUp extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private SpinKitView sp;

    private EditText emailUp, passwordUp, passwordCheck;
    private Button btnSignUp, btnSignIn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing_sign_up);

        sp = findViewById(R.id.spin_kit);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        emailUp = (EditText) findViewById(R.id.emailUp);
        passwordUp = (EditText) findViewById(R.id.passwordUp);
        passwordCheck = (EditText) findViewById(R.id.passwordCheck);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Hi", "onAuthStateChanged:signed_in:" + user.getUid());
                    sp.setVisibility(View.VISIBLE);
                    userID = user.getUid();
                    toastMessage("User has signed in: " + user.getEmail());
                    mRef = mFirebaseDatabase.getReference("users").child(user.getUid());
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Hi", "This: "+dataSnapshot.getValue());
                            if (dataSnapshot.exists()){
                                Intent i = new Intent(LandingSignUp.this, Home.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(LandingSignUp.this, UserInfoClass.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Log.d("Hi", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = emailUp.getText().toString();
                final String password = passwordUp.getText().toString();
                String pCheck = passwordCheck.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    if(password.equals(pCheck)) {
                        mAuth.createUserWithEmailAndPassword(email, password);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (password.length()<6) {
                                    toastMessage("Password must include at least 6 characters");
                                } else if (user == null) {
                                    toastMessage("Invalid Email");
                                } else {
                                    toastMessage("User Created");

                                }
                            }
                        }, 3000);

                    } else {
                        toastMessage("Passwords do not match");
                    }
                } else {
                    toastMessage("You did not fill in all the fields");
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent i = new Intent(LandingSignUp.this, LandingLogin.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
