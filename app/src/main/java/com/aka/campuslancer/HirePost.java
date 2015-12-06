package com.aka.campuslancer;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

/**
 * Created by anshul on 15/3/15.
 */
 @ParseClassName("HireData")
   public class HirePost extends ParseObject {
    public String getUsername() {
        return getString("username");
    }

    public void setUsername() {
        put("username", ParseUser.getCurrentUser().getUsername());
    }
    public String getname() {
        return getString("name");
    }

    public void setname(String s) {
        put("name",s);
    }

    public String getEmail(){
        return getString("email");
    }
    public void setEmail(){
        put("email",ParseUser.getCurrentUser().getEmail());
    }
    public String getTopic() {
        return getString("topic");
    }

    public void setTopic(String value) {
        put("topic", value);
    }
    public String getDescription() {
        return getString("description");
    }

    public void setMobileNo(String value) {
        put("mobile_no", value);
    }
    public String getMobileNo() {
        return getString("mobile_no");
    }

    public void setCategory(String value){
        put("category",value);
    }

    public String getCategory(){
        return getString("category");
    }

    public void setDescription(String value) {
        put("description", value);
    }

    public int getBid() {
        return getInt("initialbid");
        //return getString("initialbid");
    }
    public void setBid(int value) {
        put("initialbid", value);
    }
//
//    public int getEnrol() {
//        return getInt("enrollment");
//    }
//
//    public void setEnrol(int value) {
//        put("enrollment", value);
//    }


    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

//    public String getObjectId(){
//        return getString("objectId");
//    }

    public JSONArray getBidderIds(){
        return  getJSONArray("bidder_object_ids");
    }

    public void setBidderIds(JSONArray objectIds){
        put("bidder_object_ids",objectIds);
    }


    public static ParseQuery<HirePost> getQuery() {
        return ParseQuery.getQuery(HirePost.class);
    }

    public void setLat(String lat){put("lat",lat);}
    public void setLongi(String longi){put("longi",longi);}
    public String getLat(){return getString("lat");}
    public String getLongi(){return getString("longi");}
}