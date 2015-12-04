package com.aka.campuslancer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class HireStarted extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_started);
    }
    public void onclick(View view){
        switch (view.getId()){
            case R.id.newprojectbutton:
                Intent i= new Intent(getApplicationContext(),Hire.class);
                startActivity(i);
                break;
            case R.id.viewexistingprojectsbutton:
                Intent j= new Intent(getApplicationContext(),ViewExistingProjects.class);
                startActivity(j);
                break;

        }

    }
}
