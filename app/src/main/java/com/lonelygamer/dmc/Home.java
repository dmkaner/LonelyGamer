package com.lonelygamer.dmc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lonelygamer.dmc.model.HighlightedResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mindorks.placeholderview.PlaceHolderView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";
    private ListView mListView;
    private String userID;
    private boolean checker;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DrawerLayout mDrawerLayout;

    private Client client;
    private Index index;
    private Query query;
    private SearchResultsJsonParser resultsParser = new SearchResultsJsonParser();
    private boolean endReached;

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    private ImageView cpimg;

    private MaterialSearchView searchView;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ArrayList<String> picPathArray;
    private ArrayList<RequestBuilder> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        client = new Client("ZYDCCSNEJO", "273893c906ef86a82ee5f2c520b8fcf8");
        index = client.getIndex("games2");
        query = new Query();
        query.setAttributesToRetrieve("Name", "FilePathName");
        query.setAttributesToHighlight("Name", "FilePathName");

        query.setHitsPerPage(10);

        mListView = (ListView) findViewById(R.id.mListView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);

        MobileAds.initialize(this, "ca-app-pub-1353709946361725~3614333615");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                search(query);

                /*mRef = mFirebaseDatabase.getReference();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            showData(dataSnapshot, query);


                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mRef = mFirebaseDatabase.getReference();
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "This: "+dataSnapshot.getValue());
                            if (dataSnapshot.exists()){
                                showData(dataSnapshot);

                                if ( dataSnapshot.child("users").child(userID).hasChild("gamertag") ){
                                    checker = true;
                                } else {
                                    checker = false;
                                }
                            } else {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Intent i = new Intent(Home.this, LandingLogin.class);
                    startActivity(i);
                }
                // ...
            }
        };

        setupDrawer();

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

    public void makeImgList(DataSnapshot dataSnapshot){

        Iterator<DataSnapshot> items = dataSnapshot.child("Games").getChildren().iterator();
        while(items.hasNext()){
            DataSnapshot item = items.next();
            String tempPath = item.child("FilePathName").getValue().toString();

            storageRef.child("Games/"+tempPath+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    RequestBuilder<Bitmap> tempBit = Glide.with(getApplicationContext()).asBitmap().load(uri);
                    //Bitmap tempBit = Glide.with(getApplicationContext()).asBitmap().load(uri);
                    imgList.add(Glide.with(getApplicationContext()).asBitmap().load(uri));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    public void showData(DataSnapshot dataSnapshot){
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Popular Games");

        ArrayList<GameInformation> PopularGames = new ArrayList<>();

        Iterator<DataSnapshot> items = dataSnapshot.child("Games").getChildren().iterator();
        PopularGames.clear();
        while(items.hasNext()){
            GameInformation game = new GameInformation();
            DataSnapshot item = items.next();
            game.setGameName(item.child("Name").getValue(String.class));
            game.setActiveLobbies(Integer.parseInt(item.child("Live Lobbies").getValue().toString()));
            //picPathArray.add(item.child("FilePathName").getValue().toString());
            game.setPicturePath(item.child("FilePathName").getValue().toString());

            Iterator<DataSnapshot> itemsDeep1 = item.child("consoles").getChildren().iterator();
            while(itemsDeep1.hasNext()){
                DataSnapshot itemDeep = itemsDeep1.next();
                game.setConsoles(itemDeep.getValue(String.class));
            }

            Iterator<DataSnapshot> itemsDeep2 = item.child("genres").getChildren().iterator();
            while(itemsDeep2.hasNext()){
                DataSnapshot itemDeep = itemsDeep2.next();
                game.setGenres(itemDeep.getValue(String.class));
            }

            if (game.getActiveLobbies() == 1){
                PopularGames.add(game);
            }

        }
        Log.d(TAG, "Hello Here" + PopularGames.size());
        ArrayAdapter adapter = new CustomListAdapter(this, PopularGames);
        mListView.setAdapter(adapter);

        makeImgList(dataSnapshot);
        for(int i=0;i<mListView.getCount();i++){
            System.out.print("HIHIHIH" + mListView.getChildAt(i));
            View vThing = mListView.getChildAt(i);
            ImageView imgViewThing = vThing.findViewById(R.id.gameImageID);
            //imgViewThing.setImageBitmap();
            imgList.get(i).into(imgViewThing);
        }

    }

    public void myClickHandlerCreate(View v){

        if (checker) {

            mRef = mFirebaseDatabase.getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        showData(dataSnapshot);
                        if(dataSnapshot.child("Lobbies").child(userID).hasChild("allPlayers")){
                            dataSnapshot.getRef().child("Lobbies").child(userID).child("allPlayers").removeValue();
                        } else {
                            checker = false;
                        }
                        if(dataSnapshot.child("Lobbies").child(userID).hasChild("Messages")){
                            dataSnapshot.getRef().child("Lobbies").child(userID).child("Messages").removeValue();
                        } else {
                            checker = false;
                        }
                    } else {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            RelativeLayout vwParentRow = (RelativeLayout) v.getParent().getParent();
            TextView holder = (TextView) vwParentRow.getChildAt(1);
            String holderTxt = holder.getText().toString();

            CustomListAdapterLobby.leaderID = userID;

            mRef.child("Lobbies").child(userID).child("game").setValue("CHANGE");
            mRef.child("Lobbies").child(userID).child("game").setValue(holderTxt);

            /*Intent i = new Intent(Home.this, CreateLobby.class);
            startActivity(i);*/

            Intent i = new Intent(Home.this, AdCreate.class);
            startActivity(i);
        } else {
            cpimg = findViewById(R.id.cpimg);
            cpimg.setVisibility(View.VISIBLE);
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpimg.setVisibility(View.INVISIBLE);
                }
            }, 3000);

            //toastMessage("Go to profile and add some gamertags first");
        }

    }

    public void myClickHandlerJoin(View v){

        if (checker) {

            mRef = mFirebaseDatabase.getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        showData(dataSnapshot);
                        if(dataSnapshot.child("Lobbies").hasChild(userID)){
                            dataSnapshot.getRef().child("Lobbies").child(userID).removeValue();
                        } else {
                            checker = false;
                        }
                    } else {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            RelativeLayout vwParentRow = (RelativeLayout) v.getParent().getParent();
            final TextView holder = (TextView) vwParentRow.getChildAt(1);

            mRef.child("Lobby_Requests").child(userID).child(userID).child("game").setValue(holder.getText());

            /*Intent i = new Intent(Home.this, JoinLobby.class);
            startActivity(i);*/

            Intent i = new Intent(Home.this, AdJoin.class);
            startActivity(i);

        } else {
            cpimg = findViewById(R.id.cpimg);
            cpimg.setVisibility(View.VISIBLE);
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cpimg.setVisibility(View.INVISIBLE);
                }
            }, 3000);

            //toastMessage("Go to profile and add some gamertags first");
        }
    }

    private void search(String querySearch) {
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Search Results");
        query.setQuery(querySearch);
        endReached = false;
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                System.out.print(content);
                if (content != null && error == null) {

                    List<HighlightedResult<GameInformation>> results = resultsParser.parseResults(content);
                    if (results.isEmpty()) {
                        Toast.makeText(Home.this, "No results, make sure spelling is correct", Toast.LENGTH_LONG).show();
                    } else {

                        ArrayList<GameInformation> PopularGames = new ArrayList<>();
                        PopularGames.clear();

                        for(int i =0; i<results.size(); i++){
                            GameInformation game = new GameInformation();
                            game.setGameName(results.get(i).getResult().getGameName());
                            game.setPicturePath(results.get(i).getResult().getPicturePath());
                            PopularGames.add(game);
                        }
                        ArrayAdapter adapter = new CustomListAdapter(Home.this, PopularGames);
                        mListView.setAdapter(adapter);
                    }

                    // Scroll the list back to the top.
                    mListView.smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
