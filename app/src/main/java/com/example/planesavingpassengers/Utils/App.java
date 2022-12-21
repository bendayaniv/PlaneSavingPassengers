package com.example.planesavingpassengers.Utils;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySP.initMySP(this);
    }
}
