package com.localhost.jooyeon.final_project;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by yeona on 2017. 6. 17..
 */

public class CustomStartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "font1.ttf"))
                .addBold(Typekit.createFromAsset(this,"font1.ttf"))
                .addCustom1(Typekit.createFromAsset(this,"font1.ttf"));
    }

}