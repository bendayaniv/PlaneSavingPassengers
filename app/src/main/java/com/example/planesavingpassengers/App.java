package com.example.planesavingpassengers;

import android.app.Application;

import com.example.planesavingpassengers.Utils.Location;
import com.example.planesavingpassengers.Utils.MySP;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Location.init(this);

//        Location.getInstance().getCurrentLocation();

        MySP.initMySP(this);
    }
}
