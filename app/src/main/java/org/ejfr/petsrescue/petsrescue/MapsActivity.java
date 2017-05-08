package org.ejfr.petsrescue.petsrescue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ejfr.petsrescue.petsrescue.adapters.MapInfoAdapter;
import org.ejfr.petsrescue.petsrescue.utils.PosicionGPS;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button buttonCancelLocation;
    private Button buttonCurrentLocation;
    private Button buttonAddMarker;

    private GoogleMap mMap;
    private static Marker marker=null;
    private LocationManager mLocationManager;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonCancelLocation = (Button) findViewById(R.id.buttonCancelLocation);
        buttonCurrentLocation = (Button) findViewById(R.id.buttonSelectCurrentLocation);

        buttonCancelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                LatLng latLng = setCurrentLocation();
                marker= mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
        this.initFonts();
    }

    public void initFonts(){
        Utils.setTextFontButton(this.buttonCancelLocation);
        Utils.setTextFontButton(this.buttonCurrentLocation);
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("latitude",marker.getPosition().latitude);
        data.putExtra("longitude",marker.getPosition().longitude);
        setResult(RESULT_OK,data);
        marker=null;
        super.finish();
    }

    /**Method that set the current location and returns it
     *
     * @return LatLng
     */
    private LatLng setCurrentLocation(){
        LatLng newLatLong=null;
        try {
            newLatLong = Utils.getCurrentLocation(context);
            this.setLocation(newLatLong);
        }catch(SecurityException se){
            Log.e("Error GPS","GPS disabled");
        }
        return newLatLong;
    }

    /**Method that sets the current location
     *
     * @param latlng
     */
    private void setLocation(LatLng latlng){
        try{
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(Utils.DEFAULT_ZOOM).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }catch(SecurityException se){
            Log.e("Error GPS","GPS disabled");
        }
    }


    /**Method that returns the current location
     *
     * @return
     */
    /*
    private LatLng getCurrentLocation(){
        LatLng latLng;
        try {
            Location location = getLastKnownLocation();
            latLng= new LatLng(location.getLatitude(), location.getLongitude());
        }catch(Exception e){
            Log.e("Error GPS","GPS disabled/Could not get current location");//OJO esto lo tendré que cambiar
            latLng= Utils.DEFAULT_LOCATION;
        }
        return latLng;
    }*/

    /*
    private Location getLastKnownLocation() throws SecurityException{
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setInfoWindowAdapter(new MapInfoAdapter(this));

        if(marker==null) {
            marker = mMap.addMarker(new MarkerOptions().position(Utils.getCurrentLocation(context)));
        }else{
            marker  = mMap.addMarker(new MarkerOptions().position(
                    new LatLng(marker.getPosition().latitude,marker.getPosition().longitude)));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        this.setCurrentLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onlyOnce=false;
    }
}
