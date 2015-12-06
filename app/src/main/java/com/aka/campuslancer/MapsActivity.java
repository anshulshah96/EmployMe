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
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
                Log.e("Yayy","Yipee");
                    Intent i=new Intent(MapsActivity.this,Hire.class);
                    Bundle b=new Bundle();
                    b.putString("lat",lat+"");
                    b.putString("longi",longi+"");
                    i.putExtras(b);
                    startActivity(i);
                finish();

            }
        });
    }


    @Override
    public void onMapReady (GoogleMap map){

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
