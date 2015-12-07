package com.aka.campuslancer;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Welcome extends Activity {

    // Declare Variable
//    private String categories[];
    Button logout,hire,work;
    String struser;
    public static boolean loggedIn = true;
//    public static String category = "Android Development";
    public static int userMobileNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);
        //Parse.initialize(this, "gpSqLXFDsQg0oBtIg3ITgoYZLFiI9wkEF2tGiUR3", "pzEksVGPBG1iX8NkIoJ4V7hAPGoaTPo7dyNRkDs4");


        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        if(!currentUser.isAuthenticated())
        {
            Intent i=new Intent(Welcome.this,LoginSignupActivity.class);
            startActivity(i);
            finish();
        }
        else {
            struser = ParseUser.getCurrentUser().getUsername();
            try{
                ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("Instal","object obtained");
                    }
                });
                ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
                currentInstall.put("user", currentUser);
                currentInstall.put("username",currentUser.getUsername());
                currentInstall.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("Instal", "Registered");
                    } else {
                        Log.e("Instal", e.getMessage());
                    }
                }
            });}
            catch (Exception e)
            {
                Log.e("Error",e.getMessage());
            }
        }
        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.WelcomeHeader);

        // Set the currentUser String into TextView
        txtuser.setText("Welcome to EmployMe "+struser+"! It is a platform for people who need to get their work, and people work and earn.");

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.LogOutWelcome);
        hire=(Button) findViewById(R.id.HireWelcome);
        work=(Button)findViewById(R.id.WorkWelcome);

//        this.categories = new String[] {"Android Development","Web Development","Web Design","Content Writing","Bakar"};

        work.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),WorkStarted.class);
                startActivity(i);

            }
        });

        //hire button click listener
        hire.setOnClickListener(
                new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),HireStarted.class);
                startActivity(i);

            }
        });

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                loggedIn = false;
                Intent intent = new Intent(getApplicationContext(), LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
