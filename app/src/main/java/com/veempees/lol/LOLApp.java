package com.veempees.lol;

import android.app.Application;
import android.content.Context;

public class LOLApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ItemFramework.initInstance();
        Globals.initInstance(this);
    }
}
