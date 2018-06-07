package com.lonelygamer.dmc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;

/**
 * Created by dyl on 7/24/17.
 */

class CustomListAdapterLobby extends ArrayAdapter<LobbyInformation> {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private String userID;
    private FirebaseAuth mAuth;
    public static String leaderID;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public CustomListAdapterLobby(@NonNull Context context, ArrayList<LobbyInformation> lobby) {
        super(context, R.layout.custom_row_lobby, lobby);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater lobbyInflater = LayoutInflater.from(getContext());
        View customView = lobbyInflater.inflate(R.layout.custom_row_lobby, parent, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        final LobbyInformation singleLobby = getItem(position);
        TextView gameNameView = (TextView) customView.findViewById(R.id.gameNameView);
        TextView playerCountView = (TextView) customView.findViewById(R.id.playerCountView);
        TextView noteView = (TextView) customView.findViewById(R.id.noteView);
        TextView txtHostID = (TextView) customView.findViewById(R.id.txtHostID);

        gameNameView.setText(String.valueOf(singleLobby.getGameName()));
        playerCountView.setText(String.valueOf(singleLobby.getPlayerCountTotal()));
        noteView.setText(String.valueOf(singleLobby.getNote()));
        txtHostID.setText(String.valueOf(singleLobby.getHost()));

        final ImageView gameImageID = (ImageView) customView.findViewById(R.id.gameImageID);
        gameImageID.setClipToOutline(true);


        return customView;

    }

}

