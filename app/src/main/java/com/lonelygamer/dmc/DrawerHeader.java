package com.lonelygamer.dmc;


import android.widget.ImageView;
import android.widget.TextView;

import com.lonelygamer.dmc.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @Resolve
    private void onResolved() {
        nameTxt.setText("LONELY GAMER");
    }
}