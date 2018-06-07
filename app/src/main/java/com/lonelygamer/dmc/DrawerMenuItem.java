package com.lonelygamer.dmc;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;


@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_HOME = 1;
    public static final int DRAWER_MENU_ITEM_PROFILE = 2;
    public static final int DRAWER_MENU_ITEM_CURRENT_LOBBY = 3;
    public static final int DRAWER_MENU_ITEM_HELP = 4;
    public static final int DRAWER_MENU_ITEM_SIGN_OUT = 5;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_HOME:
                itemNameTxt.setText("Home");
                break;
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("Profile");
                break;
            case DRAWER_MENU_ITEM_CURRENT_LOBBY:
                itemNameTxt.setText("Current Lobby");
                break;
            case DRAWER_MENU_ITEM_HELP:
                itemNameTxt.setText("Guide");
                break;
            case DRAWER_MENU_ITEM_SIGN_OUT:
                itemNameTxt.setText("Sign Out");
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick(){
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_HOME:
                Intent i = new Intent(mContext, Home.class);
                mContext.startActivity(i);
                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_PROFILE:
                Intent e = new Intent(mContext, Profile.class);
                mContext.startActivity(e);
                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_CURRENT_LOBBY:
                if(Lobby.lobbyChecker == true) {
                    Intent a = new Intent(mContext, Lobby.class);
                    mContext.startActivity(a);
                } else {
                    Toast.makeText(mContext, "You are not currently in a lobby", Toast.LENGTH_SHORT).show();
                }
                if(mCallBack != null)mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_HELP:
                Intent a = new Intent(mContext, Help.class);
                mContext.startActivity(a);
                if(mCallBack != null)mCallBack.onGroupsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SIGN_OUT:
                mAuth.signOut();
                if(mCallBack != null)mCallBack.onMessagesMenuSelected();
                break;

        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onProfileMenuSelected();
        void onGroupsMenuSelected();
        void onMessagesMenuSelected();
    }
}
