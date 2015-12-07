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
        Spinner s =(Spinner) findViewById(R.id.spinners);

        Resources res = getResources();
        this.categories = res.getStringArray(R.array.categories_array);
        category = getString(R.string.category_default);
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

        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Hire.this, MapsActivity.class);
                startActivityForResult(i, 1);
                i.putExtra("caller", "Hirer");
            }
        });

        postButton = (Button) findViewById(R.id.HirePost);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
                post();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == 1)
        {
            mapbutton.setText("Location Saved");
            lat     = data.getStringExtra("lat");
            longi   = data.getStringExtra("longi");
            Log.d("mapResp",lat+","+longi);
        }
        else
        {
            Log.d("mapResp","Null");
        }
    }

    private void post() {
        HirePost post = new HirePost();
        text =  topic.getText().toString().trim();

        text1 = description.getText().toString().trim();

        post.setUsername();
        post.setUser(ParseUser.getCurrentUser());
        post.setTopic(text);
        post.setDescription(text1);

        Log.d("mapPost",lat+","+longi);

        if(lat == "" || longi == "" || lat =="0.0" | longi == "0.0")
        {
            post.setLat("0.0");
            post.setLongi("0.0");
            post.setLocationSet(false);
        }
        else {
            try {
                post.setLat(lat);
                post.setLongi(longi);
                post.setLocationSet(true);
            } catch (Exception e) {
                try {
                    post.setLat("0.0");
                    post.setLongi("0.0");
                    post.setLocationSet(false);
                } catch (Exception e2) {
                    return;
                }
            }
        }
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


