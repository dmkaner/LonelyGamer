package com.lonelygamer.dmc;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

public class CreateLobby extends AppCompatActivity {

    private Spinner consoleSpinner, micSpinner;
    private EditText inputPlayerCount, inputNote;
    private TextView gameTitle;
    private Button submitCreateLobby;

    private Spinner consoleSel;
    private DatabaseReference mRef;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

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
        setContentView(R.layout.activity_create_lobby);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        consoleSpinner = (Spinner) findViewById(R.id.spinnerConsole);
        micSpinner = (Spinner) findViewById(R.id.spinnerMic);
        inputPlayerCount = (EditText) findViewById(R.id.inputPlayerCount);
        inputNote = (EditText) findViewById(R.id.inputNote);
        submitCreateLobby = (Button) findViewById(R.id.submitCreateLobby);
        gameTitle = (TextView) findViewById(R.id.clGameName);
        consoleSel = (Spinner) findViewById(R.id.spinnerConsole);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();


        ArrayAdapter<CharSequence> consoleAdapter = ArrayAdapter.createFromResource(
                this, R.array.gamertag_array, R.layout.spinner_console);
        consoleAdapter.setDropDownViewResource(R.layout.spinner_console);
        consoleSel.setAdapter(consoleAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lobbies");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    gameTitle.setText( dataSnapshot.child(userID).child("game").getValue(String.class) );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitCreateLobby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( !inputPlayerCount.getText().toString().equals("") && !consoleSpinner.getSelectedItem().equals("") && !micSpinner.getSelectedItem().equals("") && !inputNote.getText().toString().equals("")) {

                    if(inputPlayerCount.getText().toString().equals("0") || inputPlayerCount.getText().toString().equals("1")){
                        Toast.makeText(CreateLobby.this, "Requires at least 2 players", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lobbies");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    dataSnapshot.getRef().child(userID).child("Messages").removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mRef.child("Lobbies").child(userID).child("console").setValue(String.valueOf(consoleSpinner.getSelectedItem()));
                        mRef.child("Lobbies").child(userID).child("players").setValue(String.valueOf(inputPlayerCount.getText()));
                        mRef.child("Lobbies").child(userID).child("mic").setValue(String.valueOf(micSpinner.getSelectedItem()));
                        mRef.child("Lobbies").child(userID).child("note").setValue(String.valueOf(inputNote.getText()));
                        mRef.child("Lobbies").child(userID).child("leader").setValue(userID);
                        mRef.child("Lobbies").child(userID).child("allPlayers").child(userID).setValue(userID);


                        Intent i = new Intent(CreateLobby.this, Lobby.class);
                        startActivity(i);
                    }

                } else {
                    Toast.makeText(CreateLobby.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }

        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent i = new Intent(CreateLobby.this, LandingLogin.class);
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
