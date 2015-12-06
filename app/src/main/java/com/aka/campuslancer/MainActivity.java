package com.aka.campuslancer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends Activity {
    SharedPreferences prefs;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity.class

                Intent intent = new Intent(MainActivity.this,
                        LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }
//        prefs=getSharedPreferences("loginprefs", Context.MODE_PRIVATE);
//        Parse.initialize(this,Keys.API_KEY,Keys.CLIENT_KEY);
//        if(prefs.getInt("logged",0)!=1)
//        {
//        Intent intent1 = new Intent(MainActivity.this, LoginSignupActivity.class);
//        startActivity(intent1);
//        finish();
//        }
//        else
//        {
//            Intent intent = new Intent(MainActivity.this, Welcome.class);
//            startActivity(intent);
//            finish();
//        }
    }


}