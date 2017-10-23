package com.alexpersaud.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by AlexPersaud on 10/23/2017.
 */

public class StarterApplication extends Application {

    String appId;
    String clientKey;
    String serverUrl;

    @Override
    public void onCreate() {
        super.onCreate();

        appId = getResources().getString(R.string.parse_app_id);
        clientKey = getResources().getString(R.string.parse_client_key);
        serverUrl = getResources().getString(R.string.parse_server);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(appId)
                .clientKey(clientKey)
                .server(serverUrl)
                .build()
        );

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
