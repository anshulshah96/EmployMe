package com.aka.campuslancer;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,LocationListener {
    GoogleMap map;
    Button done;
    double lat,longi;
    OnLocatinChosenListener changed;
    public interface OnLocatinChosenListener{
        public void onLocationChosen(double lat,double longi);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toast.makeText(MapsActivity.this,"Turn on your GPS for getting location",Toast.LENGTH_LONG).show();

        setOnLocationChosenListner(Hire.instance);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map=mapFragment.getMap();
        map.setMyLocationEnabled(true);
        done=(Button)findViewById(R.id.butdone);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestp= locationManager.getBestProvider(criteria,true);
        Location location=locationManager.getLastKnownLocation(bestp);

        if(location!=null)
        {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestp,20000,0,this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed.onLocationChosen(lat,longi);
                onBackPressed();
            }
        });
    }


    @Override
    public void onMapReady (GoogleMap map){

    }

    public void setOnLocationChosenListner(OnLocatinChosenListener l)
    {
     changed=l;
    }
    @Override
    public void onMapClick (LatLng latLng){
        Toast.makeText(getApplicationContext(), "" + latLng.latitude, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "" + latLng.longitude, Toast.LENGTH_LONG).show();
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

    @Override
    public void onLocationChanged(Location location) {
         lat=location.getLatitude();
         longi=location.getLongitude();
        LatLng latLng=new LatLng(lat,longi);
        map.addMarker(new MarkerOptions().position(latLng).title("workplace").draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }


}
