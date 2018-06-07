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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingLogin extends AppCompatActivity {

    private static final String TAG = "LandingLogin";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    private SpinKitView sp;
    private EditText emailIn, passwordIn;
    private Button btnSignIn, btnSignUp;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing_login);

        sp = findViewById(R.id.spin_kit);
        emailIn = (EditText) findViewById(R.id.emailIn);
        passwordIn = (EditText) findViewById(R.id.passwordIn);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    sp.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    userID = user.getUid();
                    toastMessage("User has signed in: " + user.getEmail());
                    mRef = mFirebaseDatabase.getReference("users").child(user.getUid());
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "This: "+dataSnapshot.getValue());
                            if (dataSnapshot.exists()){
                                Intent i = new Intent(LandingLogin.this, Home.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(LandingLogin.this, UserInfoClass.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = emailIn.getText().toString();
                String password = passwordIn.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(email, password);
                    sp.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user == null) {
                                toastMessage("Email or password is incorrect");
                                sp.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, 2000);


                } else {
                    toastMessage("You did not fill in all the fields");
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent i = new Intent(LandingLogin.this, LandingSignUp.class);
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
