package com.aka.campuslancer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityBid extends FragmentActivity implements OnMapReadyCallback ,PeopleForProject.onGetLocationListner,WorkActivity.onGetLocationListner{
    GoogleMap map;
    double lat,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_bid);
        Toast.makeText(MapsActivityBid.this,"Turn on your GPS for getting location",Toast.LENGTH_LONG).show();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        PeopleForProject a=new PeopleForProject();
        a.setOnGetLocation(this);

        WorkActivity b=new WorkActivity();
        b.setOnGetLocation(this);

        mapFragment.getMapAsync(this);
        map=mapFragment.getMap();
        map.addMarker(new MarkerOptions().position(new LatLng(lat,longi))
                .title("bidder").draggable(true));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onGetLocation(double lat, double longi) {
        this.lat=lat;
        this.longi=longi;
    }


}
