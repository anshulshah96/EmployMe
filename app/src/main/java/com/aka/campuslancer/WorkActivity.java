package com.aka.campuslancer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.widget.AdapterView.OnItemClickListener;

import java.util.List;


public class WorkActivity extends Activity implements WorkDescriptionFragment.OnFragmentInteractionListener{
    private ParseQueryAdapter<HirePost> postsQueryAdapter;
    private static final int MAX_POST_SEARCH_RESULTS = 20;
    public static String username;
    public static String description;
    public static String topic;
    public static String mobileno;
    public static String projectId;
    public CustomProgressDialogBox dialog;
    double lat,longi;
    ParseQuery<HirePost> q = HirePost.getQuery();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);


        dialog = new CustomProgressDialogBox(WorkActivity.this,"Loading data...");

        ParseObject.registerSubclass(HirePost.class);



        ParseQueryAdapter.QueryFactory<HirePost> factory =
                new ParseQueryAdapter.QueryFactory<HirePost>() {
                    public ParseQuery<HirePost> create() {
                        ParseQuery<HirePost> query = HirePost.getQuery();
                        query.include("user");
                        query.include("objectId");
                        query.whereEqualTo("category", WorkStarted.category);
                        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                        query.orderByDescending("createdAt");
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        q=query;
                        return query;
                    }
                };
        
        postsQueryAdapter = new ParseQueryAdapter<HirePost>(this, factory) {
            @Override
            public View getItemView(HirePost post, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.post_item, null);
                }
                TextView usernameView = (TextView) view.findViewById(R.id.post_username);
                TextView topicView = (TextView) view.findViewById(R.id.post_topic);
                TextView budgetView = (TextView) view.findViewById(R.id.post_budget);
                TextView enrolView = (TextView) view.findViewById(R.id.post_mobile);
                TextView descriptionView = (TextView) view.findViewById(R.id.post_description);
                TextView projectId = (TextView) view.findViewById(R.id.post_project_id);
                Button location=(Button)view.findViewById(R.id.location);
                String topictxt=post.getTopic();
                String bidtxt="Budget: Rs."+post.getBid();
                String enrolltxt=""+post.getMobileNo();
                String unametxt=post.getUsername();
                String descriptiontxt = post.getDescription();
                String projectIdtxt = post.getObjectId();
                lat=Double.parseDouble(post.getLat());
                longi=Double.parseDouble(post.getLongi());
                topicView.setText(topictxt);
                budgetView.setText(bidtxt);
                enrolView.setText(enrolltxt);
                usernameView.setText(unametxt);
                descriptionView.setText(descriptiontxt);
                projectId.setText(projectIdtxt);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(WorkActivity.this,MapsActivityBid.class);
                        Bundle b=new Bundle();
                        b.putString("lat",lat+"");
                        b.putString("longi",longi+"");
                        i.putExtras(b);
                        startActivity(i);
                    }
                });

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        postsQueryAdapter.setAutoload(false);
        // Disable pagination, we'll manage the query limit ourselves
        postsQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView postsListView = (ListView) findViewById(R.id.work_listview);
        postsListView.setAdapter(postsQueryAdapter);
        postsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                username = ((TextView) (view.findViewById(R.id.post_username))).getText().toString();
                description = ((TextView) (view.findViewById(R.id.post_description))).getText().toString();
                topic = ((TextView) (view.findViewById(R.id.post_topic))).getText().toString();
                mobileno = ((TextView) (view.findViewById(R.id.post_mobile))).getText().toString();
                projectId = ((TextView) (view.findViewById(R.id.post_project_id))).getText().toString();
                Intent intent = new Intent(WorkActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });


        postsQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<HirePost>() {
            @Override
            public void onLoading() {
                dialog.setMessage("Loading data...");
                dialog.show();
            }
            @Override
            public void onLoaded(List<HirePost> hirePosts, Exception e) {
                if(e==null){
                    dialog.dismiss();
                    if(hirePosts.isEmpty()){
                        Toast.makeText(getApplication(),"No Active Projects.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("Works list error",e.toString());
                    finish();
                }
            }
        });
        doListQuery();
    }

    /*
     * Set up a query to update the list view
     */
    private void doListQuery() {
        postsQueryAdapter.loadObjects();
    }

    public void onFragmentInteraction(Uri uri){

    }
    public void navigationClick(View view){
        if(view == view.findViewById(R.id.work_tv_navigation)){
            Intent intent = new Intent(getApplicationContext(),Welcome.class);
            startActivity(intent);
            finish();
        }
    }

}