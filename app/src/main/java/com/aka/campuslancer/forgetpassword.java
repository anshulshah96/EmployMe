package com.aka.campuslancer;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;


public class forgetpassword extends Activity {

    EditText emailtxt;
    String email;
    Button reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        emailtxt = (EditText) findViewById(R.id.editText);

        reset = (Button) findViewById(R.id.reset_submit);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    email = emailtxt.getText().toString();
                    ParseUser.requestPasswordReset(email);
                    Toast.makeText(getApplication(),"confirmation email sent",Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }
            }
        });
    }
}