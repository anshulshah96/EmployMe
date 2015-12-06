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

public class Hire extends Activity implements MapsActivity.OnLocatinChosenListener {
    Button postButton,mapbutton;
    EditText topic, description, bid;
    private String[] categories;
    public String category;
    public CustomProgressDialogBox dialog;
    double lat=0,longi=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire);
        ParseObject.registerSubclass(HirePost.class);
        //Parse.initialize(this, "gpSqLXFDsQg0oBtIg3ITgoYZLFiI9wkEF2tGiUR3", "pzEksVGPBG1iX8NkIoJ4V7hAPGoaTPo7dyNRkDs4");

        Resources res = getResources();
        this.categories = res.getStringArray(R.array.categories_array);
        category = getString(R.string.category_default);

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
        mapbutton=(Button)findViewById(R.id.map);
            topic = (EditText) findViewById(R.id.TopicField);
            description = (EditText) findViewById(R.id.DescriptionField);
            bid = (EditText) findViewById(R.id.InitialBidField);
            postButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    post();
                }
            });
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Hire.this,MapsActivity.class);
                startActivity(i);
            }
        });
        }

    private void post() {
        // 1
        HirePost post = new HirePost();
        String text =  topic.getText().toString().trim();
        String text1 = description.getText().toString().trim();

        MapsActivity a=new MapsActivity();
        a.setOnLocationChosenListner(this);

        post.setUsername();
        post.setUser(ParseUser.getCurrentUser());
        post.setTopic(text);
        post.setDescription(text1);
        if(lat==0 || longi==0)
        {
         Toast.makeText(Hire.this,"Select Location",Toast.LENGTH_LONG).show();
        }
        post.setLat(lat+"");
        post.setLongi(longi+"");
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

    @Override
    public void onLocationChosen(double lat, double longi) {
        this.lat=lat;
        this.longi=longi;
    }
}


