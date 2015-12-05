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
        prefs=getSharedPreferences("loginprefs", Context.MODE_PRIVATE);

        Parse.initialize(this,Keys.API_KEY,Keys.CLIENT_KEY);

        if(prefs.getInt("logged",0)!=1)
        {
        Intent intent1 = new Intent(MainActivity.this, LoginSignupActivity.class);
        startActivity(intent1);
        finish();
        }


        else
        {
            Intent intent = new Intent(MainActivity.this, Welcome.class);
            startActivity(intent);
            finish();
        }

    }


}