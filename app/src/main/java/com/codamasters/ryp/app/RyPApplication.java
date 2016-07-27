package com.codamasters.ryp.app;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Juan on 27/07/2016.
 */
public class RyPApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
