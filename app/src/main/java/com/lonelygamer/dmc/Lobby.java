package com.lonelygamer.dmc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.lonelygamer.dmc.CustomListAdapterLobby.leaderID;

public class Lobby extends AppCompatActivity {

    private Button sendMessage, btnLeaveLobby, btnDisplayGtags;
    private String userID, uid, consoleTemp, userTemp;
    private ListView gamertagList;
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<ChatMessage> adapter;
    private DatabaseReference mRef;
    private FrameLayout gtagFrame;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DrawerLayout mDrawerLayout;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private PlaceHolderView mGalleryView;
    private Toolbar mToolbar;
    private ValueEventListener temp;

    LinearLayout layout;
    RelativeLayout layout_2;
    ScrollView scrollView;

    private int playerCountTracker = 0;
    public static Boolean lobbyChecker = false;
    private String loggedInUserName = "";
    private Lobby activity;

    @Override
    protected void onStart() {
        lobbyChecker = true;
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //leaveLobby();
        //adapter.stopListening();
        mRef.removeEventListener(temp);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //leaveLobby();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        sendMessage = (Button) findViewById(R.id.sendMessage);
        btnLeaveLobby = (Button) findViewById(R.id.leaveLobby);
        btnDisplayGtags = findViewById(R.id.displayUsers);
        gamertagList = findViewById(R.id.gamertagList);
        gtagFrame = findViewById(R.id.gtagFrame);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mRef = mFirebaseDatabase.getInstance().getReference();
        temp = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    uid = dataSnapshot.child("users").child(userID).child("uname").getValue(String.class);

                    consoleTemp = dataSnapshot.child("Lobbies").child(leaderID).child("console").getValue().toString();
                    ArrayList<String> usersInLobby = new ArrayList<>();
                    Iterator<DataSnapshot> items = dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildren().iterator();
                    while(items.hasNext()){
                        DataSnapshot item = items.next();
                        usersInLobby.add(item.getValue().toString());
                    }
                    for(int i=0; i<usersInLobby.size();i++){
                        usersInLobby.set(i, dataSnapshot.child("users").child(usersInLobby.get(i)).child("uname").getValue() + " : " + dataSnapshot.child("users")
                                .child(usersInLobby.get(i)).child("gamertag").child(consoleTemp).getValue().toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, usersInLobby);
                    gamertagList.setAdapter(arrayAdapter);

                    System.out.println("HOWDY" + dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildrenCount());
                    System.out.println("HOWDY" + playerCountTracker);

                    if(dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildrenCount() > playerCountTracker){
                        playerCountTracker = (int) dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildrenCount();

                        Toast.makeText(getApplicationContext(), "Player has joined lobby", Toast.LENGTH_SHORT).show();

                    } else if(dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildrenCount() < playerCountTracker){
                        playerCountTracker = (int) dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").getChildrenCount();

                        Toast.makeText(getApplicationContext(), "Player has left lobby", Toast.LENGTH_SHORT).show();

                    }

                }

                if (dataSnapshot.exists()) {
                    try {
                        if(dataSnapshot.child("Lobbies").child(leaderID).child("allPlayers").child(userID).getValue().equals(userID)){

                        }
                        if(leaderID != userID && !dataSnapshot.child("Lobbies").child(leaderID).child("game").getValue().equals(LobbyList.currentGame)) {
                            mRef.removeEventListener(this);
                            Intent i = new Intent(Lobby.this, Home.class);
                            startActivity(i);
                            finish();
                        }
                    } catch(NullPointerException e) {
                        mRef.removeEventListener(this);
                        Intent i = new Intent(Lobby.this, Home.class);
                        startActivity(i);
                        finish();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.messageInput);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                if (!input.getText().toString().equals("")){
                    ChatMessage chatMSG = new ChatMessage();
                    chatMSG.setMessageText(input.getText().toString());


                    chatMSG.setMessageUser(user.getDisplayName());

                    FirebaseDatabase.getInstance()
                            .getReference("Lobbies")
                            .child(leaderID)
                            .child("Messages")
                            .push()
                            .setValue(chatMSG);

                    // Clear the input
                    input.setText("");
                }
            }
        });

        btnLeaveLobby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                leaveLobby();
            }

        });

        btnDisplayGtags.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gtagFrame.setVisibility(View.VISIBLE);
            }

        });

        gtagFrame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gtagFrame.setVisibility(View.INVISIBLE);
            }

        });

        FirebaseDatabase.getInstance()
                .getReference("Lobbies")
                .child(leaderID)
                .child("Messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot);
                //String message = map.get("message").toString();
                //String userName = map.get("user").toString();
                String message = dataSnapshot.child("messageText").getValue().toString();
                String userName = dataSnapshot.child("messageUser").getValue().toString();

                if(userName.equals(uid)){
                    addMessageBox(message, userName, 1);
                }
                else{
                    addMessageBox( message, userName, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent i = new Intent(Lobby.this, LandingLogin.class);
                    startActivity(i);
                }
                // ...
            }
        };

    }

    public void addMessageBox(String message, String Auth, int type){
        TextView textView = new TextView(Lobby.this);
        TextView textViewUser = new TextView(Lobby.this);
        textView.setText(message);
        textViewUser.setText(Auth);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        lp2.topMargin = 20;
        lp2.leftMargin = 20;
        lp2.rightMargin = 20;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textViewUser.setLayoutParams(lp2);
        textView.setLayoutParams(lp2);
        layout.addView(textViewUser);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void leaveLobby(){
        lobbyChecker = false;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lobbies");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    dataSnapshot.getRef().child(CustomListAdapterLobby.leaderID).child("allPlayers").child(userID).removeValue();
                    System.out.println("HEREHERHEHERHEHR "+ dataSnapshot.getValue());
                    try {
                        if (dataSnapshot.child(CustomListAdapterLobby.leaderID).child("leader").getValue().equals(userID)) {
                            dataSnapshot.getRef().child(CustomListAdapterLobby.leaderID).removeValue();
                        }
                    } catch(NullPointerException e) {

                    }
                    mRef.removeEventListener(this);
                    Intent i = new Intent(Lobby.this, Home.class);
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        ActionBarDrawerToggle  drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close){
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

}
