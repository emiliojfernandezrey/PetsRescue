package org.ejfr.petsrescue.petsrescue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ejfr.petsrescue.petsrescue.model.Pet;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

public class ViewAlertActivity extends AppCompatActivity {

    private ImageView imageViewPhoto;
    private TextView textViewAlertType;
    private TextView textViewPetType;
    private TextView textViewPetSize;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewBreed;
    private TextView textViewObservations;
    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private TextView textViewLocation;
    private Button buttonBack;
    private Button buttonMatch;
    private Pet pet;
    private Context context=this;
    private static Boolean updateRequired=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alert);

        //initialize components
        this.imageViewPhoto = (ImageView) findViewById(R.id.imageViewViewAlertPhoto);
        this.textViewAlertType = (TextView) findViewById(R.id.textViewViewAlertTypeAlert);
        this.textViewPetType = (TextView) findViewById(R.id.textViewAlertTypePetType);
        this.textViewPetSize = (TextView) findViewById(R.id.textViewViewAlertPetSize);
        this.textViewDate = (TextView) findViewById(R.id.textViewAlertViewDate);
        this.textViewTime = (TextView) findViewById(R.id.textViewViewAlertTime);
        this.textViewBreed = (TextView) findViewById(R.id.textViewViewAlertBreed);
        this.textViewObservations = (TextView) findViewById(R.id.textViewViewAlertObservations);
        this.textViewLocation = (TextView) findViewById(R.id.textViewViewAlertLocation);
        this.textViewLatitude = (TextView) findViewById(R.id.textViewViewAlertLatitude);
        this.textViewLongitude = (TextView) findViewById(R.id.textViewViewAlertLongitude);
        this.buttonBack = (Button) findViewById(R.id.buttonViewAlertBack);
        this.buttonMatch = (Button) findViewById(R.id.buttonViewAlertMatch);

        this.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=null;
                try {
                    Utils.updateTypeAlertOfPetInFirebaseDatabase(Utils.getKeyOfPetHashMap(pet), Pet.TYPE_ALERT_PET_MATCHED);
                    message = getResources().getString(R.string.toast_message_type_alert_updated_successfully);
                    updateRequired=true;
                }catch (Exception e){
                    Log.e("ERROR",""+e.toString());
                    message = getResources().getString(R.string.toast_message_type_alert_updated_error);
                    updateRequired=false;
                }
/*
                new AlertDialog.Builder(ViewAlertActivity.this)
                        .setTitle(getResources().getString(R.string.toast_message_type_alert_updated_title))
                        .setMessage(message)
                        .setIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .setPositiveButton("OK", null)
                        .show();*/
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            Double latitude = extras.getDouble("latitude");
            Double longitude = extras.getDouble("longitude");
            Pet pet = Utils.getPetFromPetList(latitude,longitude);
            if (pet != null) {
                this.pet=pet;
            }
        }

        if(pet!=null){
            this.initializeComponentsValue();
        }
        this.buttonMatch.setEnabled(pet!=null && !pet.getTypeAlert().equals(Pet.TYPE_ALERT_PET_MATCHED));
        if(this.buttonMatch.isEnabled()){
            this.buttonMatch.setText(getResources().getString(R.string.label_view_alert_button_match));
        }else{
            this.buttonMatch.setText("");
        }
        initFonts();
    }

    private void initFonts(){
        Utils.setTextFontButton(this.buttonBack);
        Utils.setTextFontButton(this.buttonMatch);

        Utils.setTextFontBalooBhainaTextView(this.textViewAlertType);
        /*
        Utils.setTextFontBalooBhainaTextView(this.textViewPetType);
        Utils.setTextFontBalooBhainaTextView(this.textViewPetSize);
        Utils.setTextFontBalooBhainaTextView(this.textViewDate);
        Utils.setTextFontBalooBhainaTextView(this.textViewTime);
        Utils.setTextFontBalooBhainaTextView(this.textViewLocation);
        Utils.setTextFontBalooBhainaTextView(this.textViewLongitude);
        Utils.setTextFontBalooBhainaTextView(this.textViewLatitude);
        Utils.setTextFontBalooBhainaTextView(this.textViewBreed);
        Utils.setTextFontBalooBhainaTextView(this.textViewObservations);
        */
    }

    private void initializeComponentsValue(){
        if(pet!=null) {
            Utils.loadImageIntoImageViewFromFirebase(context,pet.getPhotoPath(),imageViewPhoto);
            this.textViewAlertType.setText(getResources().getString(R.string.label_view_alert_type_alert)+" "+pet.getTypeAlertString());
            this.textViewPetType.setText(getResources().getString(R.string.label_view_alert_type_pet)+ " "+pet.getType());
            this.textViewPetSize.setText(getResources().getString(R.string.label_view_alert_size_pet) +" "+pet.getSize());
            this.textViewDate.setText(getResources().getString(R.string.label_view_alert_date)+" "+pet.getFormattedDate());
            this.textViewTime.setText(getResources().getString(R.string.label_view_alert_time)+" "+pet.getFormattedTime());
            this.textViewBreed.setText(getResources().getString(R.string.label_view_alert_breed)+ " "+pet.getBreed());
            this.textViewObservations.setText(getResources().getString(R.string.label_view_alert_observations)+" "+pet.getObservations());
            this.textViewLatitude.setText(getResources().getString(R.string.label_view_alert_latitude)+ " "+pet.getLatitude());
            this.textViewLongitude.setText(getResources().getString(R.string.label_view_alert_longitude)+" "+pet.getLongitude());
        }
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("updateRequired",updateRequired);
        super.finish();
    }
}
