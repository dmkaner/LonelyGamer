package com.lonelygamer.dmc;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class LobbyList extends AppCompatActivity {

    private ListView lobbyListView;
    private FirebaseAuth mAuth;
    private DatabaseReference nRef;
    private FirebaseDatabase mFirebaseDatabase;
    private String userID;
    public static String currentGame;
    private String game, console, mic, players;
    private Button refresh;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    private String dbGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby_list);

        refresh = findViewById(R.id.refresh);
        lobbyListView = (ListView) findViewById(R.id.lobbyListView);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setupDrawer();

        nRef = mFirebaseDatabase.getReference();
        nRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    showDataLobReq(dataSnapshot);
                    showData(dataSnapshot);
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nRef = mFirebaseDatabase.getReference();
                nRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            showDataLobReq(dataSnapshot);
                            showData(dataSnapshot);
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent i = new Intent(LobbyList.this, LandingLogin.class);
                    startActivity(i);
                }
                // ...
            }
        };

    }

    public void showDataLobReq(DataSnapshot dataSnapshot){

        for(DataSnapshot ds : dataSnapshot.child("Lobby_Requests").getChildren()){
            game = ds.child(userID).child("game").getValue(String.class);
            console = ds.child(userID).child("console").getValue(String.class);
            mic = ds.child(userID).child("mic").getValue(String.class);
            players = ds.child(userID).child("players").getValue(String.class);

            if(game!=null)
                break;
        }


    }

    public void showData(DataSnapshot dataSnapshot){

        ArrayList<LobbyInformation> Lobbies = new ArrayList<>();

        Iterator<DataSnapshot> items = dataSnapshot.child("Lobbies").getChildren().iterator();
        Lobbies.clear();
        while(items.hasNext()){

            try {

                LobbyInformation lobby = new LobbyInformation();
                DataSnapshot item = items.next();

                if (item.child("players").getValue() != null && (int) item.child("allPlayers").getChildrenCount() >= Integer.parseInt(item.child("players").getValue(String.class)))
                    break;

                lobby.setGameName(item.child("game").getValue(String.class));
                lobby.setConsole(item.child("console").getValue(String.class));
                lobby.setMic(item.child("mic").getValue(String.class));
                lobby.setNote(item.child("note").getValue(String.class));
                lobby.setDbImgName(item.child("gameDbName").getValue(String.class));

                String leaderInstance = item.child("leader").getValue(String.class);
                System.out.println(";alsjdf;aklsd;fklj" + leaderInstance);
                lobby.setPlayerCountTotal(dataSnapshot.child("Lobbies").child(leaderInstance).child("allPlayers").getChildrenCount() + "/" + item.child("players").getValue(String.class));
                System.out.println("-__---" + item.child("leader").getValue(String.class));
                lobby.setHost(item.child("leader").getValue(String.class));

                if (console.equals(item.child("console").getValue(String.class)) &&
                        players.equals(item.child("players").getValue(String.class)) &&
                        mic.equals(item.child("mic").getValue(String.class)) &&
                        game.equals(item.child("game").getValue(String.class))) {

                    Lobbies.add(lobby);
                }
            } catch(NullPointerException e) {

            }

        }
        ArrayAdapter adapter = new CustomListAdapterLobby(this, Lobbies);
        lobbyListView.setAdapter(adapter);

    }

    public void onClickHandlerLobby(View v){

        ConstraintLayout vwParentRow = (ConstraintLayout) v.getParent();

        final TextView holder = (TextView)vwParentRow.getChildAt(7);
        final TextView holderGame = (TextView)vwParentRow.getChildAt(1);
        System.out.println("-------" + holder.getText().toString());
        System.out.println("-------" + holderGame.getText().toString());
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lobbies").child(holder.getText().toString());
        currentGame = holderGame.getText().toString();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = (int)dataSnapshot.child("allPlayers").getChildrenCount();
                if (count<Integer.parseInt(dataSnapshot.child("players").getValue(String.class))){
                    CustomListAdapterLobby.leaderID = holder.getText().toString();
                    ref.child("allPlayers").child(userID).setValue(userID);

                    Intent i = new Intent(LobbyList.this, Lobby.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LobbyList.this, "Lobby Full", Toast.LENGTH_SHORT).show();
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
