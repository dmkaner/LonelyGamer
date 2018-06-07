package com.lonelygamer.dmc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by dyl on 7/19/17.
 */

class CustomListAdapter extends ArrayAdapter<GameInformation>{

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    RequestOptions requestOptions;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public CustomListAdapter(@NonNull Context context, ArrayList<GameInformation> game) {
        super(context, R.layout.custom_row, game);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater gameInflater = LayoutInflater.from(getContext());
        View customView = gameInflater.inflate(R.layout.custom_row, parent, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        requestOptions = new RequestOptions();
        requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        final GameInformation singleGame = getItem(position);
        TextView gameNameID = (TextView) customView.findViewById(R.id.gameNameID);
        //TextView lobbyCountID = (TextView) customView.findViewById(R.id.lobbyCountID);
        final ImageView gameImageID = (ImageView) customView.findViewById(R.id.gameImageID);
        gameImageID.setClipToOutline(true);

        gameNameID.setText(String.valueOf(singleGame.getGameName()));
        //lobbyCountID.setText(String.valueOf(singleGame.getActiveLobbies()));

        storageRef.child("Games/"+singleGame.getPicturePath()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).apply(requestOptions).into(gameImageID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        return customView;

    }



}
