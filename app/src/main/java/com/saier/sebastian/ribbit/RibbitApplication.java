package com.saier.sebastian.ribbit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Sebastian on 23.04.2015.
 */
public class RibbitApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "lNtH62vWSwwsQPZnDoafAU6kCqqmPzZ7bph8Wnk8", "qp5peWvm0MO9XKgjQ0sEirpXuxSJ1jmgZEGO3Tf6");

    }
}
