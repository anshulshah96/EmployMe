package com.aka.campuslancer;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityBid extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap googleMap;
    double lat,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_bid);
        Bundle b=getIntent().getExtras();
        lat=Double.parseDouble(b.getString("lat"));
        longi=Double.parseDouble(b.getString("longi"));
//        Toast.makeText(MapsActivityBid.this,"Turn on your GPS for getting location",Toast.LENGTH_LONG).show();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Log.d("map", "Map is ready");
        setupMap();
    }

    private void setupMap(){
        googleMap.setMyLocationEnabled(false);
        LatLng latLng=new LatLng(lat,longi);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Workplace").draggable(false));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}
