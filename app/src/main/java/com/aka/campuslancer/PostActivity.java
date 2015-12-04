package com.aka.campuslancer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class PostActivity extends FragmentActivity implements WorkDescriptionFragment.OnFragmentInteractionListener {

    Button postButton;
    EditText topic, description, bid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FragmentManager f1=getFragmentManager();
        FragmentTransaction f2=f1.beginTransaction();
        WorkDescriptionFragment f=new WorkDescriptionFragment();

        f2.replace(R.id.container,f);
        f2.commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}