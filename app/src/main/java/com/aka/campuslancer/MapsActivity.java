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

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_map);
        try {
            setContentView(R.layout.activity_maps);
        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            //  mapFragment.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady (GoogleMap map){
        map.addMarker(new MarkerOptions()
                .position(new LatLng(29.8662184, 77.89531809))
                .title("Marker").draggable(true));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    @Override
    public void onMapClick (LatLng latLng){
        Toast.makeText(getApplicationContext(), "" + latLng.latitude, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "" + latLng.longitude, Toast.LENGTH_LONG).show();
    }     }
