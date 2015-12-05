package com.aka.campuslancer;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;


public class ViewExistingProjects extends Activity {
    private ParseQueryAdapter<HirePost> existingProjectsQueryAdapter;
    private static final int MAX_POST_SEARCH_RESULTS = 30;
    ParseQuery<HirePost> q = HirePost.getQuery();
    public CustomProgressDialogBox dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_existing_projects);

        dialog = new CustomProgressDialogBox(ViewExistingProjects.this,"Loading data...");
        dialog.setMessage("Loading data...");
        dialog.show();

        ParseObject.registerSubclass(HirePost.class);
        ParseQueryAdapter.QueryFactory<HirePost> factory =
                new ParseQueryAdapter.QueryFactory<HirePost>() {
                    public ParseQuery<HirePost> create() {
                        ParseQuery<HirePost> query = HirePost.getQuery();
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.whereContains("username", ParseUser.getCurrentUser().getUsername());
                        //query.whereContains("objectId", "VX7MegqtmW");
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        q=query;
                        return query;
                }
        };
        if(q==null){

            Toast.makeText(getApplicationContext(),"You don't have any projects",Toast.LENGTH_SHORT).show();

        }
        existingProjectsQueryAdapter = new ParseQueryAdapter<HirePost>(this, factory) {
            @Override
            public View getItemView(HirePost post, View view, ViewGroup parent) {

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.existing_projects_item, null);
                }
                TextView topicView = (TextView) view.findViewById(R.id.existing_projects_topic);
                TextView bidView = (TextView) view.findViewById(R.id.existing_projects_bid);
                TextView description = (TextView) view.findViewById(R.id.existing_projects_description);
                TextView projectId = (TextView) view.findViewById(R.id.existing_project_id);
                Button deleteButton = (Button) view.findViewById(R.id.button_delete);

                String topictxt=post.getTopic();
                String bidtxt=""+post.getBid();
                String descriptiontxt = post.getDescription();
                final String projectidtxt = post.getObjectId();

                topicView.setText("Topic: "+topictxt);
                bidView.setText("Budget: "+bidtxt);
                description.setText("Description: "+descriptiontxt);
                projectId.setText(projectidtxt);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParseQuery<HirePost> query = ParseQuery.getQuery(HirePost.class);
                        query.whereContains("objectId", projectidtxt);
                        query.findInBackground(new FindCallback<HirePost>() {
                            public void done(List<HirePost> jobs, ParseException e) {
                                if (e == null) {
                                    for (ParseObject job : jobs) {
                                        Log.d("Deleting", job.getObjectId());
                                        job.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d("DeleteError", e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("DelFoundError", e.getMessage());
                                }
                            }
                        });
                        ParseQuery<BidPost> query2 = ParseQuery.getQuery(BidPost.class);
                        query2.whereContains("projectId",projectidtxt);
                        query2.findInBackground(new FindCallback<BidPost>() {
                            @Override
                            public void done(List<BidPost> bids, ParseException e) {
                                if (e == null) {
                                    for (ParseObject bid : bids) {
                                        Log.d("DeleteBid", bid.getObjectId());
                                        bid.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                } else {
                                                    Log.d("DelBidErr", e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("DelFoundError", e.getMessage());
                                }
                            }
                        });
                        finish();
                    }
                });

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        existingProjectsQueryAdapter.setAutoload(false);
        // Disable pagination, we'll manage the query limit ourselves
        existingProjectsQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView postsListView = (ListView) findViewById(R.id.existingprojectsLV);
        postsListView.setAdapter(existingProjectsQueryAdapter);

        existingProjectsQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<HirePost>() {

            @Override
            public void onLoading() {
                dialog.setMessage("Loading data...");
                dialog.show();
            }

            @Override
            public void onLoaded(List<HirePost> hirePosts, Exception e) {
                if(hirePosts.isEmpty()&&e==null){

                    Toast.makeText(getApplicationContext(),"No Existing Projects.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(e==null){
                    dialog.dismiss();
                }
                else if(e!=null){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        doListQuery();

        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplication(),PeopleForProject.class);
                intent.putExtra("project_id",((TextView)view.findViewById(R.id.existing_project_id)).getText().toString());
                startActivity(intent);
            }
        });
    }

    private void doListQuery() {
        existingProjectsQueryAdapter.loadObjects();
    }

    public void navigationClick(View view){
        if(view == view.findViewById(R.id.existingprojects_tv_navigation)){
            Intent intent = new Intent(getApplicationContext(),Welcome.class);
            startActivity(intent);
            finish();
        }
    }

}
