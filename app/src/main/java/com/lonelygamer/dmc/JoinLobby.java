package com.lonelygamer.dmc;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

public class JoinLobby extends AppCompatActivity {

    private Spinner consoleSpinner, micSpinner;
    private TextView gameTitle;
    private EditText inputPlayerCount;
    private Button submitLobbyReqeust;

    private DatabaseReference mRef;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DrawerLayout mDrawerLayout;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_lobby);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        consoleSpinner = (Spinner) findViewById(R.id.spinnerConsoleJoin);
        micSpinner = (Spinner) findViewById(R.id.spinnerMicJoin);
        inputPlayerCount = (EditText) findViewById(R.id.inputPlayerCountJoin);
        submitLobbyReqeust = (Button) findViewById(R.id.submitLobbyRequest);
        gameTitle = (TextView) findViewById(R.id.clGameName);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lobby_Requests");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    gameTitle.setText( dataSnapshot.child(userID).child(userID).child("game").getValue(String.class) );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitLobbyReqeust.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( !inputPlayerCount.getText().toString().equals("") && !consoleSpinner.getSelectedItem().equals("") && !micSpinner.getSelectedItem().equals("")) {

                    if(inputPlayerCount.getText().toString().equals("0") || inputPlayerCount.getText().toString().equals("1")){
                        Toast.makeText(JoinLobby.this, "Requires at least 2 players", Toast.LENGTH_SHORT).show();
                    } else {
                        mRef.child("Lobby_Requests").child(userID).child(userID).child("console").setValue(String.valueOf(consoleSpinner.getSelectedItem()));
                        mRef.child("Lobby_Requests").child(userID).child(userID).child("players").setValue(inputPlayerCount.getText().toString());
                        mRef.child("Lobby_Requests").child(userID).child(userID).child("mic").setValue(String.valueOf(micSpinner.getSelectedItem()));

                        Intent i = new Intent(JoinLobby.this, LobbyList.class);
                        startActivity(i);
                    }


                } else {
                    Toast.makeText(JoinLobby.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }

        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent i = new Intent(JoinLobby.this, LandingLogin.class);
                    startActivity(i);
                }
                // ...
            }
        };

    }

    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CURRENT_LOBBY))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HELP))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SIGN_OUT));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
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

}
