package com.lonelygamer.dmc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Profile extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseDatabase mFirebaseDatabase;
    private TextView profUName;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button enterGamertag;
    private String xboxOneStr = "Enter Xbox One Tag",
            xbox360Str = "Enter Xbox 360 Tag",
            ps4Str = "Enter PS4 Tag",
            ps3Str = "Enter PS3 Tag",
            steamStr = "Enter Steam Tag",
            originStr = "Enter Origin Tag",
            ubisoftStr = "Enter Ubisoft Tag",
            epicgamesStr = "Enter Epic Games Tag",
            bnetStr = "Enter BNET Tag";
    private Button xboxOne, xbox360, ps4, ps3, steam, origin, ubisoft, epicgames, bnet;

    ArrayList<ProfileInformation> ProfTags = new ArrayList<>();

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
        setContentView(R.layout.activity_profile);

        xboxOne = findViewById(R.id.btnXboxOneGTag);
        xbox360 = findViewById(R.id.btnXbox360GTag);
        ps4 = findViewById(R.id.btnPS4GTag);
        ps3 = findViewById(R.id.btnPS3GTag);
        steam = findViewById(R.id.btnSteamGTag);
        origin = findViewById(R.id.btnOriginGTag);
        ubisoft = findViewById(R.id.btnUbisoftGTag);
        epicgames = findViewById(R.id.btnEpicGamesGTag);
        bnet = findViewById(R.id.btnBNetGTag);

        profUName = (TextView) findViewById(R.id.txtProfUName);
        //enterGamertag = (Button) findViewById(R.id.btnAddTag);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("users");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent i = new Intent(Profile.this, LandingLogin.class);
                    startActivity(i);
                }
            }
        };

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        if (dataSnapshot.child(userID).child("userID").getValue(String.class).equals(userID)) {
                            profUName.setText(dataSnapshot.child(userID).child("uname").getValue(String.class));
                        }
                    }

                    if(dataSnapshot.child(userID).child("gamertag").child("Xbox One").getValue() != null){
                        //xboxOne.setText(dataSnapshot.child(userID).child("gamertag").child("Xbox One").getValue(String.class));
                        xboxOne.setBackgroundResource(R.drawable.rounded_frame_green);
                        xboxOne.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        xboxOneStr = dataSnapshot.child(userID).child("gamertag").child("Xbox One").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("Xbox 360").getValue() != null){
                        //xbox360.setText(dataSnapshot.child(userID).child("gamertag").child("Xbox 360").getValue(String.class));
                        xbox360.setBackgroundResource(R.drawable.rounded_frame_green);
                        xbox360.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        xbox360Str = dataSnapshot.child(userID).child("gamertag").child("Xbox 360").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("PS4").getValue() != null){
                        //ps4.setText(dataSnapshot.child(userID).child("gamertag").child("PS4").getValue(String.class));
                        ps4.setBackgroundResource(R.drawable.rounded_frame_green);
                        ps4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        ps4Str = dataSnapshot.child(userID).child("gamertag").child("PS4").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("PS3").getValue() != null){
                        //ps3.setText(dataSnapshot.child(userID).child("gamertag").child("PS3").getValue(String.class));
                        ps3.setBackgroundResource(R.drawable.rounded_frame_green);
                        ps3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        ps3Str = dataSnapshot.child(userID).child("gamertag").child("PS3").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("Steam").getValue() != null){
                        //steam.setText(dataSnapshot.child(userID).child("gamertag").child("Steam").getValue(String.class));
                        steam.setBackgroundResource(R.drawable.rounded_frame_green);
                        steam.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        steamStr = dataSnapshot.child(userID).child("gamertag").child("Steam").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("Origin").getValue() != null){
                        //origin.setText(dataSnapshot.child(userID).child("gamertag").child("Origin").getValue(String.class));
                        origin.setBackgroundResource(R.drawable.rounded_frame_green);
                        origin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        originStr = dataSnapshot.child(userID).child("gamertag").child("Origin").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("Ubisoft").getValue() != null){
                        //ubisoft.setText(dataSnapshot.child(userID).child("gamertag").child("Ubisoft").getValue(String.class));
                        ubisoft.setBackgroundResource(R.drawable.rounded_frame_green);
                        ubisoft.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        ubisoftStr = dataSnapshot.child(userID).child("gamertag").child("Ubisoft").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("Epic Games").getValue() != null){
                        //epicgames.setText(dataSnapshot.child(userID).child("gamertag").child("Fortnite").getValue(String.class));
                        epicgames.setBackgroundResource(R.drawable.rounded_frame_green);
                        epicgames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        epicgamesStr = dataSnapshot.child(userID).child("gamertag").child("Epic Games").getValue(String.class);
                    }
                    if(dataSnapshot.child(userID).child("gamertag").child("BNet").getValue() != null){
                        //bnet.setText(dataSnapshot.child(userID).child("gamertag").child("Fortnite").getValue(String.class));
                        bnet.setBackgroundResource(R.drawable.rounded_frame_green);
                        bnet.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        bnetStr = dataSnapshot.child(userID).child("gamertag").child("BNet").getValue(String.class);
                    }

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        xboxOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(xboxOneStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Xbox One").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        xbox360.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(xbox360Str);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Xbox 360").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        ps4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(ps4Str);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("PS4").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        ps3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(ps3Str);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("PS3").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        steam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(steamStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Steam").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(originStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Origin").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        ubisoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(ubisoftStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Ubisoft").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        epicgames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(epicgamesStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("Epic Games").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        bnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                View mView = getLayoutInflater().inflate(R.layout.inputgtag, null);
                final EditText tag = mView.findViewById(R.id.gTagInput);
                tag.setHint(bnetStr);
                Button gTagBtn = mView.findViewById(R.id.gTagBtn);

                gTagBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tag.getText().toString().isEmpty()){
                            mRef.child(userID).child("gamertag").child("BNet").setValue(tag.getText().toString());
                            Toast.makeText(Profile.this, "Gamertag Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile.this, "Fill Empty Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


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

