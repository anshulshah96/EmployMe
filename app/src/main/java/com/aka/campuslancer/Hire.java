package com.aka.campuslancer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Hire extends Activity  {
    Button postButton,mapbutton;
    EditText topic, description, bid;
    private String[] categories;
    public String category;
    public CustomProgressDialogBox dialog;
    String lat,longi,text,text1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire);
        ParseObject.registerSubclass(HirePost.class);
        mapbutton=(Button)findViewById(R.id.map);
        topic = (EditText) findViewById(R.id.TopicField);
        description = (EditText) findViewById(R.id.DescriptionField);
        bid = (EditText) findViewById(R.id.InitialBidField);
        Resources res = getResources();
        this.categories = res.getStringArray(R.array.categories_array);
        category = getString(R.string.category_default);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ;
            } else {
                lat=extras.getString("lat");
                longi=extras.getString("longi");
                mapbutton.setText("Location Set");
                topic.setText(text);
                description.setText(text1);
                bid.setText(bid.getText().toString());
            }
        } else {
            ;
        }


        Spinner s =(Spinner) findViewById(R.id.spinners);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categories);
        s.setAdapter(adapter);

        dialog = new CustomProgressDialogBox(Hire.this,"Posting...");

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category =  categories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = getString(R.string.category_default);
            }
        });


        postButton = (Button) findViewById(R.id.HirePost);

            postButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    post();
                }
            });
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Hire.this,MapsActivity.class);
                i.putExtra("caller","Hirer");
                onDestroy();
                startActivity(i);

            }
        });
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text",text);
        outState.putString("text1",text1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        text=savedInstanceState.getString("text");
        text1=savedInstanceState.getString("text1");
    }

    private void post() {
        // 1
        HirePost post = new HirePost();
         text =  topic.getText().toString().trim();

         text1 = description.getText().toString().trim();

        post.setUsername();
        post.setUser(ParseUser.getCurrentUser());
        post.setTopic(text);
        post.setDescription(text1);
        if(lat==null || longi==null)
        {
         Toast.makeText(Hire.this,"Select Location",Toast.LENGTH_LONG).show();
            onCreate(null);
            finish();
        }
        post.setLat(lat);
        post.setLongi(longi);
        post.setCategory(category);
        if(bid.getText().toString()!=""){
        post.setBid(Integer.parseInt(bid.getText().toString()));}
        else
        Toast.makeText(Hire.this,"Enter a bid to post project",Toast.LENGTH_LONG).show();

        String mobNo="";

        ParseUser parseUser = ParseUser.getCurrentUser();
        try {
            parseUser.fetchFromLocalDatastore();
            mobNo = parseUser.get("mobile_no").toString();
        }
        catch (Exception e){
            Log.d("Hire", e.getMessage());
        }

        post.setMobileNo(mobNo);

        dialog.setMessage("Posting...");
        dialog.show();

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(ParseUser.getCurrentUser(), true);
        post.setACL(acl);

        // 3
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    dialog.dismiss();
                    finish();
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                    Log.d("Error: ",e.getMessage());
                }
            }
        });

    }


}


