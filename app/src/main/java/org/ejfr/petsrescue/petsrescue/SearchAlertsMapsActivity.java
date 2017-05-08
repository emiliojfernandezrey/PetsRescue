package org.ejfr.petsrescue.petsrescue;

import android.content.Context;
import android.content.Intent;
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
import org.ejfr.petsrescue.petsrescue.adapters.SearchAlertInfoAdapter;
import org.ejfr.petsrescue.petsrescue.model.Pet;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchAlertsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button buttonBack;
    private Button buttonGoToSelectedAlert;
    private GoogleMap mMap;
    private List<Marker> listMarker;
    private static Marker selectedMarker=null;
    private Context context=this;
    private static final int REQUEST_VIEW_ALERT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_alerts_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listMarker= new ArrayList<Marker>();

        buttonBack = (Button) findViewById(R.id.buttonBackSearchAlertsMaps);
        buttonGoToSelectedAlert = (Button) findViewById(R.id.buttonGoToSelectedAlert);
        //selectedMarker=null;
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMarker=null;
                finish();
            }
        });

        buttonGoToSelectedAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonGoToSelectedAlert.isEnabled()) {
                    Log.d("SearchAlertsMapActiv", "Aqui abrimos una nueva actividad con los datos del marker selected");
                    Intent intent = new Intent(context, ViewAlertActivity.class);
                    intent.putExtra("latitude",selectedMarker.getPosition().latitude);
                    intent.putExtra("longitude",selectedMarker.getPosition().longitude);
                    startActivityForResult(intent,REQUEST_VIEW_ALERT);
                }
            }
        });
        this.checkEnabledGoToSelectAlertButton();
        initFonts();
    }

    private void initFonts(){
        Utils.setTextFontButton(this.buttonBack);
        Utils.setTextFontButton(this.buttonGoToSelectedAlert);
    }


    private void checkEnabledGoToSelectAlertButton(){
        if(selectedMarker!=null){
            buttonGoToSelectedAlert.setTextColor(getResources().getColor(R.color.colorTextButton));
            buttonGoToSelectedAlert.setEnabled(true);
            buttonGoToSelectedAlert.setText(getResources().getString(R.string.label_go_to_selected_pet_alert));
        }else{
            buttonGoToSelectedAlert.setTextColor(getResources().getColor(R.color.colorButtonBackground));
            buttonGoToSelectedAlert.setEnabled(false);
            buttonGoToSelectedAlert.setText("");
        }
        //buttonGoToSelectedAlert.setEnabled(selectedMarker!=null);
    }

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
        mMap.setInfoWindowAdapter(new SearchAlertInfoAdapter(this,listMarker));

        LatLng currentLoc;
        try{
            currentLoc = Utils.getCurrentLocation(context);
        }catch (Exception e){
            currentLoc = Utils.getCurrentLocation(context);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLoc).zoom(Utils.DEFAULT_ZOOM).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        addMarkers(Utils.getListLatLngPetAlerts());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker=marker;
                checkEnabledGoToSelectAlertButton();
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedMarker=null;
                checkEnabledGoToSelectAlertButton();
            }
        });
    }

    private void addMarkers(List<LatLng> list){
        Marker marker;
        for(int i =0; i<list.size(); i++) {
            marker = mMap.addMarker(new MarkerOptions().position(list.get(i)).draggable(false));
            listMarker.add(marker);
        }
    }

    private void removeMarkers(){
        while(listMarker.size()>0){
            listMarker.get(0).remove();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==REQUEST_VIEW_ALERT){
            Bundle resultData = data.getExtras();
            Boolean updateRequired= (Boolean) resultData.get("updateRequired");
            if(updateRequired) {
                this.removeMarkers();
                addMarkers(Utils.getListLatLngPetAlerts());
            }
        }

    }
}
