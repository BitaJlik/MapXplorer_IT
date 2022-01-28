package com.example.mapxplorer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

public class CustomDrawer extends DrawerLayout {
    ActionBarDrawerToggle toggle;


    public CustomDrawer(@NonNull Context context) {
        super(context);

        addDrawerListener(toggle);
    }


}
