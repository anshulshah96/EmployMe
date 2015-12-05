package com.aka.campuslancer;
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.animation.Animation;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

public class LoginSignupActivity extends Activity {
    // Declare Variables
    Button loginbutton;
    Button signup;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;
    TextView reset_password;
    CustomProgressDialogBox dialog;
    SharedPreferences loginpref;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.loginsignup);
        Parse.initialize(this, "gpSqLXFDsQg0oBtIg3ITgoYZLFiI9wkEF2tGiUR3", "pzEksVGPBG1iX8NkIoJ4V7hAPGoaTPo7dyNRkDs4");

        loginpref=getSharedPreferences("loginprefs",Context.MODE_PRIVATE);


        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        reset_password = (TextView) findViewById(R.id.reset_password);
        reset_password.setText(Html.fromHtml(getString(R.string.forgotPassword)));


        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        dialog = new CustomProgressDialogBox(LoginSignupActivity.this,"Logging In...");
        // Login Button Click Listener
        loginbutton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();


                dialog.show();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    SharedPreferences.Editor edit=loginpref.edit();
                                    edit.putInt("logged",1);
                                    edit.commit();
                                    Intent intent = new Intent(LoginSignupActivity.this, Welcome.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Successfully Logged in",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"No such user exist, please signup",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        });
        // Sign up Button Click Listener

        reset_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),forgetpassword.class);
                startActivity(i);
            }
        });
    }

    public void SignUp(View view){
        // Save new user data into Parse.com Data Storage
        Intent intent = new Intent(LoginSignupActivity.this,SignupActivity.class);
        startActivity(intent);
    }



}