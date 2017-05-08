package org.ejfr.petsrescue.petsrescue.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.ejfr.petsrescue.petsrescue.MapsActivity;
import org.ejfr.petsrescue.petsrescue.R;

/**
 * Created by EmilioJosé on 23/04/2017.
 */

public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity activity;
    private TextView textViewMarker;
    private TextView textViewPosition;

    public MapInfoAdapter(Activity activity){
        this.activity=activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.maps_info_adapter_layout,null);
        textViewMarker = (TextView) view.findViewById(R.id.textViewMarkerText);
        textViewPosition = (TextView) view.findViewById(R.id.textViewMarkerPosition);

        String text = this.activity.getResources().getString(R.string.label_maps_marker);
        textViewMarker.setText(text);
        String position = "(Lat. "+ marker.getPosition().latitude + ", Lng. " + marker.getPosition().longitude+")";
        textViewPosition.setText(position);

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}