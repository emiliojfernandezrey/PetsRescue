package org.ejfr.petsrescue.petsrescue.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.ejfr.petsrescue.petsrescue.R;
import org.ejfr.petsrescue.petsrescue.model.Pet;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

import java.util.List;

/**
 * Created by EmilioJosé on 01/05/2017.
 */

public class SearchAlertInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity activity;
    private TextView textViewAlertType;
    private TextView textViewObservations;
    private TextView textViewPosition;
    //private ImageView imageViewPet;
    private List<Marker> listMarker;

    public SearchAlertInfoAdapter(Activity activity, List<Marker> listMarker){
        this.activity=activity;
        this.listMarker=listMarker;
    }

    private void setBackgroundColor(String typeAlert, View view){
        int color=-1;
        if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_ABANDON)){
            color = view.getResources().getColor(R.color.colorPetAlertAbandoned);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_MISSED)){
            color = view.getResources().getColor(R.color.colorPetAlertMissed);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_PICKED_UP)){
            color = view.getResources().getColor(R.color.colorPetAlertPickedUp);
        }else{
            color = view.getResources().getColor(R.color.colorPetAlertDefault);
        }
        view.setBackgroundColor(color);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.maps_search_alerts_adapter_layout,null);
        textViewObservations= (TextView) view.findViewById(R.id.textViewAdapterObservationsSearchAlertMap);
        textViewPosition = (TextView) view.findViewById(R.id.textViewAdapterPositionSearchAlertMap);
        textViewAlertType = (TextView) view.findViewById(R.id.textViewAdapterAlertTypeSearchAlertMap);
        //imageViewPet = (ImageView) view.findViewById(R.id.imageAdapterSearchAlertMap);

        int index=0;
        for(int i=0;i<listMarker.size();i++){
            if(listMarker.get(i).equals(marker)){
                Log.d("CHUPIII","Me ahorro buscarlo y tal!!");
                index=i;
                break;
            }
        }

        final Pet pet;
        Pet aux = null;
        if(index<listMarker.size()){
            aux = Utils.getPetListFromFirebase().get(index);
        }

        String position = "(Lat. "+ marker.getPosition().latitude + ", Lng. " + marker.getPosition().longitude+")";
        textViewPosition.setText(position);
        if(aux!=null) {
            pet=aux;
            textViewObservations.setText(pet.getObservations());
            textViewAlertType.setText(pet.getTypeAlertString().toUpperCase());
            //this.setBackgroundColor(pet.getTypeAlert(),view);
            //Utils.loadImageIntoImageViewFromFirebase(view.getContext(),pet.getPhotoPath(),imageViewPet);
        }else{
            pet=null;
        }

        /*
        Pet pet = Utils.getPetFromPetList(marker.getPosition().latitude,marker.getPosition().longitude);
        if (pet != null) {
            //String text = Utils.getPetListFromFirebase().get(indexMarker).getObservations();
            textViewObservations.setText(pet.getObservations());
        }
        String position = "(Lat. "+ marker.getPosition().latitude + ", Lng. " + marker.getPosition().longitude+")";
        textViewPosition.setText(position);*/

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
