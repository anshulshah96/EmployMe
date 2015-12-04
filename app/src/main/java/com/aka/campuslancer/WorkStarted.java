package com.aka.campuslancer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class WorkStarted extends Activity {

    Button submitButton;
    public static String category = "Android Development";
    private String categories[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_started);
        submitButton = (Button)findViewById(R.id.browseProjectsButton);


        this.categories = new String[] {"Mobile Development","Web Development","Design","Writing","Volunteering","Question-Answer","Miscellaneous"};
        Spinner s =(Spinner) findViewById(R.id.categories_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categories);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category =  categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Android Development";
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),WorkActivity.class);
                startActivity(i);
            }
        });
    }



}
