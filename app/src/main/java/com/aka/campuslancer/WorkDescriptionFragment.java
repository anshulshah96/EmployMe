package com.aka.campuslancer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkDescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    TextView topicTv,descriptionTv,usernameTv,mobilenoTv,navigationTv;
    EditText bidValue;
    Button bidButton;
    String projectId;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public CustomProgressDialogBox dialog;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkDescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkDescriptionFragment newInstance(String param1, String param2) {
        WorkDescriptionFragment fragment = new WorkDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_description, container, false);


        ParseObject.registerSubclass(BidPost.class);

        topicTv = (TextView) view.findViewById(R.id.WorkDescriptionTopic);
        descriptionTv= (TextView) view.findViewById(R.id.WorkDescriptionFragmentDescription);
        usernameTv = (TextView) view.findViewById(R.id.WorkDescriptionFragmentUsername);
        mobilenoTv = (TextView) view.findViewById(R.id.WorkDescriptionFragmentMobileNumber);
        bidValue = (EditText) view.findViewById(R.id.BidValue);
        bidButton = (Button) view.findViewById(R.id.bidButton);
        navigationTv = (TextView) view.findViewById(R.id.work_fragment_tv_navigation);

        topicTv.setText(WorkActivity.topic);
        descriptionTv.setText(WorkActivity.description);
        usernameTv.setText(WorkActivity.username);
        mobilenoTv.setText(WorkActivity.mobileno);

        projectId = WorkActivity.projectId;

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CustomProgressDialogBox(getActivity(),"Loading data...");
                dialog.setMessage("Loading data...");
                dialog.show();

                BidPost bpost = new BidPost();
                final int bid = Integer.parseInt(bidValue.getText().toString());
                bpost.setBid(bid);
                bpost.setProjectId(projectId);
                bpost.setProjectIdString(projectId);
                bpost.setUser(ParseUser.getCurrentUser());
                bpost.setBidderUsername(ParseUser.getCurrentUser().getUsername());

                String mobNo="";
                ParseUser parseUser = ParseUser.getCurrentUser();
                try {
                    parseUser.fetchFromLocalDatastore();
                    mobNo = parseUser.get("mobile_no").toString();
                }
                catch (Exception e){
                    Log.d("local data error",e.toString());
                }

                bpost.setMobileNo(mobNo);

                Log.d("bidset",bid+"\tpid: "+projectId+"\tuser: "+ParseUser.getCurrentUser()+"\tmobno:"+mobNo);

                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                bpost.setACL(acl);

                bpost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if(e==null) {
                            dialog.dismiss();
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("ProjectId",projectId);
                            params.put("BidderName",ParseUser.getCurrentUser().getUsername());
                            params.put("Bid",bid);
                            params.put("Topic",WorkActivity.topic);
                            ParseCloud.callFunctionInBackground("newBid", params, new FunctionCallback<String>() {
                                @Override
                                public void done(String s, com.parse.ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                                        Log.d("Cloud Response", s);
                                        getActivity().finish();
                                    }
                                    else {
                                        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                                        Log.d("Cloud Error", e.getMessage());
                                    }
                                }
                            });
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(),"Saving error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("Saving error",e.getMessage());
                        }
                    }
                });
            }
        });

        navigationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == view.findViewById(R.id.work_fragment_tv_navigation)){
                    Intent intent = new Intent(getActivity(),Welcome.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return view;
    }
//
//    public View onCreateView2(final LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_work_description, container, false);
//
//        topicTv = (TextView) view.findViewById(R.id.WorkDescriptionTopic);
//        descriptionTv= (TextView) view.findViewById(R.id.WorkDescriptionFragmentDescription);
//        usernameTv = (TextView) view.findViewById(R.id.WorkDescriptionFragmentUsername);
//        mobilenoTv = (TextView) view.findViewById(R.id.WorkDescriptionFragmentMobileNumber);
//        bidValue = (EditText) view.findViewById(R.id.BidValue);
//        bidButton = (Button) view.findViewById(R.id.bidButton);
//
//        topicTv.setText(WorkActivity.topic);
//        descriptionTv.setText(WorkActivity.description);
//        usernameTv.setText(WorkActivity.username);
//        mobilenoTv.setText(WorkActivity.mobileno);
//
//        projectId = WorkActivity.projectId;
//
//        bidButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final ProgressDialog dialog = new ProgressDialog(getActivity());
//                dialog.setMessage("Loading data...");
//                dialog.show();
//
//                int bid = Integer.parseInt(bidValue.getText().toString());
//                final ParseQuery<HirePost> query = ParseQuery.getQuery("HireData");
//                query.whereContains("objectId",projectId);
//
//                HirePost obj = new HirePost();
//
//                query.findInBackground(new FindCallback<HirePost>() {
//                    @Override
//                    public void done(List<HirePost> hirePosts, com.parse.ParseException e) {
//                        if (e == null) {
//                            Log.i("bids ", "Retrieved " + hirePosts.get(0).getObjectId());
//                            HirePost project = hirePosts.get(0);
//                            if(project.getBidderIds()==null){
//                                project.setBidderIds((new JSONArray()).put(ParseUser.getCurrentUser().getUsername()));
//                            }
//                            else {
//                                JSONArray bidderids = project.getBidderIds();
//                                bidderids.put(ParseUser.getCurrentUser().getUsername());
//                                project.setBidderIds(bidderids);
//                            }
//
//
//                            ParseACL acl = new ParseACL();
//                            project.setACL(acl);
//
//                            project.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(com.parse.ParseException e) {
//                                    if(e==null) {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(getActivity().getApplicationContext(),WorkActivity.class);
//                                        startActivity(intent);
//                                    }
//                                    else{
//                                        Log.e("Save in background",e.getMessage());
//                                        dialog.dismiss();
//                                    }
//                                }
//                            });
//
//                        } else {
//                            Log.i("Error", "Error: " + e.getMessage());
//                        }
//                    }
//                });
//
//
//
//
//
//
//                JSONArray userIds = new JSONArray();
//
//
//
//            }
//        });
//
//
//
//        return  view;
//
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
