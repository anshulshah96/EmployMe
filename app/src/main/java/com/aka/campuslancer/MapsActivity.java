package com.aka.campuslancer;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,LocationListener {
    GoogleMap googleMap;
    Button done;
    double lat,longi;
    String newString;
    Location location;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        done=(Button)findViewById(R.id.butdone);
        Toast.makeText(MapsActivity.this,"Turn on your GPS for getting location",Toast.LENGTH_LONG).show();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("caller");
                Log.e("string",newString);
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("caller");
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestp= locationManager.getBestProvider(criteria,true);

        location=locationManager.getLastKnownLocation(bestp);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat == 0.0 || longi==0.0)
                {
                    Toast.makeText(MapsActivity.this,"Please select a location",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("mapResp","returning "+lat+","+longi);
                Intent i=new Intent();
                i.putExtra("lat",""+lat);
                i.putExtra("longi",""+longi);
                setResult(1,i);
                finish();
            }
        });
    }

    @Override
    public void onMapReady (GoogleMap map){
        googleMap = map;
        Log.d("map","Map is ready");
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("mapClick",latLng.latitude+","+latLng.longitude);
                Location selected = new Location("Selected");
                selected.setLatitude(latLng.latitude);
                selected.setLongitude(latLng.longitude);
                onLocationChanged(selected);
            }
        });
        setupMap();
    }

    private void setupMap(){
        googleMap.setMyLocationEnabled(true);
        if(location!=null)
        {
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        longi=location.getLongitude();
        Log.d("mapLochange",lat+","+longi);
        LatLng latLng=new LatLng(lat,longi);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng).title("workplace").draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    public void onMapClick (LatLng latLng){
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
