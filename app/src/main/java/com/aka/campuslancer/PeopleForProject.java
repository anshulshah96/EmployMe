package com.aka.campuslancer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public class PeopleForProject extends Activity {

    ParseQuery<BidPost> q = BidPost.getQuery();
    ParseQueryAdapter<BidPost> postsQueryAdapter;
    public CustomProgressDialogBox dialog;
    onGetLocationListner got;
    double lat,longi;

    public interface onGetLocationListner{
        public void onGetLocation(double lat,double longi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_for_project);
        Intent intent = getIntent();
        final String pid = intent.getStringExtra("project_id");

        dialog = new CustomProgressDialogBox(PeopleForProject.this,"Loading data...");
        dialog.setMessage("Loading data...");
        dialog.show();

        ParseObject.registerSubclass(BidPost.class);


        ParseQueryAdapter.QueryFactory<BidPost> factory =
                new ParseQueryAdapter.QueryFactory<BidPost>() {
                    public ParseQuery<BidPost> create() {
                        ParseQuery<BidPost> query = BidPost.getQuery();
                        query.include("bidder_user");
//                        query.include("project_id");
//                        ParseObject pobj = ParseObject.createWithoutData("HireData",pid);
//                        query.whereEqualTo("pointer_id",pobj);
                        query.whereContains("projectId",pid);
                        Log.d("project id", pid);
                        query.orderByDescending("createdAt");
//                        query.setLimit(20);
                        q = query;
                        return query;
                    }
                };
        postsQueryAdapter = new ParseQueryAdapter<BidPost>(this, factory) {
            @Override
            public View getItemView(BidPost post, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.peopleforprojectitem, null);
                }
                TextView projectuser = (TextView) view.findViewById(R.id.peopleproject_user);
                TextView projectmobile = (TextView) view.findViewById(R.id.peopleproject_mobile);
                TextView projectbid = (TextView) view.findViewById(R.id.bid_amount);
                Button location=(Button)view.findViewById(R.id.location);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        got.onGetLocation(lat,longi);
                        Intent i=new Intent(PeopleForProject.this,MapsActivityBid.class);
                        startActivity(i);
                    }
                });

                String pb = "" + post.getBid();
                String pu = "" + post.getBidderUsername();
                 lat=Double.parseDouble(post.getLat());
                 longi=Double.parseDouble(post.getLongi());

                projectbid.setText("Bid: Rs."+pb);
                projectuser.setText("Bidder: "+pu);
                projectmobile.setText(""+post.getMobileNo());

                return view;

            }
        };

        postsQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<BidPost>() {
            @Override
            public void onLoading() {
                dialog.setMessage("Loading data...");
                dialog.show();
            }

            @Override
            public void onLoaded(List<BidPost> bidPosts, Exception e) {
                if(e==null&&bidPosts.isEmpty()){
                    Toast.makeText(getApplication(),"No bidder for your project.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(e==null&&!bidPosts.isEmpty()){
                    dialog.dismiss();
                }
                else if (e!=null){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("bidders",e.toString());
                    finish();
                }
            }
        });


        // Disable automatic loading when the adapter is attached to a view.
        postsQueryAdapter.setAutoload(false);

        // Disable pagination, we'll manage the query limit ourselves
        postsQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView postsListView = (ListView) findViewById(R.id.peopleproject_listview);


        postsListView.setAdapter(postsQueryAdapter);

        doListQuery();
    }
    private void doListQuery() {
        postsQueryAdapter.loadObjects();
    }

    public void navigationClick(View view){
        if(view == view.findViewById(R.id.peopleproject_tv_navigation)){
            Intent intent = new Intent(getApplicationContext(),Welcome.class);
            startActivity(intent);
            finish();
        }
    }

    public void setOnGetLocation(onGetLocationListner l)
    {
        got=l;
    }
}



