package com.lonelygamer.dmc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by dyl on 8/11/17.
 */

class CustomListAdapterProfile extends ArrayAdapter<ProfileInformation> {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private String userID;
    private FirebaseAuth mAuth;

    public CustomListAdapterProfile(@NonNull Context context, ArrayList<ProfileInformation> profile) {
        super(context, R.layout.custom_row_profile, profile);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater lobbyInflater = LayoutInflater.from(getContext());
        View customView = lobbyInflater.inflate(R.layout.custom_row_profile, parent, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        final ProfileInformation singleProfile = getItem(position);
        TextView txtConsole = (TextView) customView.findViewById(R.id.txtConsole);
        TextView txtGamertag = (TextView) customView.findViewById(R.id.txtGamertag);

        txtConsole.setText(String.valueOf(singleProfile.getConsole()));
        txtGamertag.setText(String.valueOf(singleProfile.getGamertag()));

        return customView;

    }

}
