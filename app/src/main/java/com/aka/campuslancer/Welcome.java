package com.aka.campuslancer;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Welcome extends Activity {

    // Declare Variable
    private String categories[];
    Button logout,hire,work;
    public static boolean loggedIn = true;
    public static String category = "Android Development";
    public static int userMobileNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.WelcomeHeader);

        // Set the currentUser String into TextView
        txtuser.setText("Welcome to Campus Lancer "+struser+"! It is a platform for people who need to get their work, and people work and earn.");

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.LogOutWelcome);
        hire=(Button) findViewById(R.id.HireWelcome);
        work=(Button)findViewById(R.id.WorkWelcome);

        this.categories = new String[] {"Android Development","Web Development","Web Design","Content Writing","Bakar"};

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
                Intent intent = new Intent(getApplicationContext(),LoginSignupActivity.class);
                startActivity(intent);
            }
        });


    }
}