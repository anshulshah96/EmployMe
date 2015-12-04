package com.aka.campuslancer;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by root on 4/12/15.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,Keys.API_KEY,Keys.CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultacl=new ParseACL();
        defaultacl.setPublicReadAccess(true);
        PushService.setDefaultPushCallback(this,MainActivity.class);
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                Log.d("com.parse.push","success");

                else
                    Log.d("com.parse.push","failed");
            }
        });
    }
}
