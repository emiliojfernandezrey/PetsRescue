package org.ejfr.petsrescue.petsrescue.model;

import android.view.View;

import org.ejfr.petsrescue.petsrescue.R;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

/**
 * Created by EmilioJosé on 26/04/2017.
 */

public class Pet {

    /** Key that defines the state of abandoning pets*/
    public final static String TYPE_ALERT_PET_ABANDON="ABANDONED";
    /**Key that defines the state of missing pets*/
    public final static String TYPE_ALERT_PET_MISSED="MISSED";
    /**Key that defines the state of finding pets*/
    public final static String TYPE_ALERT_PET_PICKED_UP ="PICKED UP";
    /**Key that defines the state of finding pets*/
    public final static String TYPE_ALERT_PET_MATCHED ="PET MATCH OWNER";
    /**Key that defines the deault Type Alert*/
    public final static String TYPE_ALERT_PET_DEFAULT=TYPE_ALERT_PET_ABANDON;
    private String type;
    private String size;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private String photoPath;
    private Double latitude;
    private Double longitude;
    private String breed;
    private String observations;
    private String typeAlert;

    public Pet(){
        this.type="";
        this.size="";
        this.photoPath="";
        this.breed="";
        this.observations="";
        this.typeAlert=Pet.TYPE_ALERT_PET_DEFAULT;
    }

    public Pet(String type, String size, int year, int month, int day, int hour, int minutes,
               String photoPath, Double latitude, Double longitude, String breed, String observations,
               String typeAlert){
        this.type=type;
        this.size=size;
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minutes=minutes;
        this.photoPath=photoPath;
        this.latitude=latitude;
        this.longitude=longitude;
        this.breed=breed;
        this.observations=observations;
        this.typeAlert= typeAlert;
    }

    public String getType() {
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size){
        this.size=size;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year){
        this.year=year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month){
        this.month=month;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day){
        this.day=day;
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour){
        this.hour=hour;
    }

    public int getMinutes() {
        return minutes;
    }
    public void setMinutes(int minutes){
        this.minutes=minutes;
    }

    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath){
        this.photoPath=photoPath;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude){
        this.latitude=latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude){
        this.longitude=longitude;
    }

    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed){
        this.breed=breed;
    }

    public String getObservations() {
        return observations;
    }
    public void setObservations(String observations){
        this.observations=observations;
    }

    public String getTypeAlert(){
        return typeAlert;
    }
    public void setTypeAlert(String typeAlert){
        this.typeAlert=typeAlert;
    }

    public String getTypeAlertString(){
        return Utils.getTypeAlertString(this.typeAlert);
    }

    public String getFormattedDate(){
        return Utils.getFormattedDate(this.year,this.month,this.day);
    }

    public String getFormattedTime(){
        return Utils.getFormattedTime(this.hour,this.minutes);
    }

    public static boolean checkFields(String type, String size, int year, int month, int day,
                                int hour, int minutes, String currentPhotoPath,
                                Double latitude, Double longitude, String breed, String observations,
                                String typeAlert){
        if(type==null || type.length()==0){
            return false;
        }
        if(size==null || size.length()==0){
            return false;
        }
        if(year==0 ||  month==0 || day==0 || hour==0 || minutes ==0){
            return false;
        }
        if(currentPhotoPath== null || currentPhotoPath.length()==0){
            return false;
        }
        if(latitude==null || longitude==null){
            return false;
        }
        if(breed==null || breed.length()==0){
            return false;
        }
        if(observations==null || observations.length()==0){
            return false;
        }
        if(typeAlert==null || typeAlert.length()==0){
            return false;
        }
        return true;
    }

    public boolean equals(Pet pet){
        if(this.type!=pet.type){
            return false;
        }
        if(this.typeAlert!=pet.typeAlert){
            return  false;
        }
        if(this.photoPath!=pet.photoPath){
            return false;
        }
        if(this.year!=pet.year){
            return false;
        }
        if(this.month!=pet.month){
            return false;
        }
        if(this.day!=pet.day){
            return false;
        }
        if(this.hour!=pet.hour){
            return false;
        }
        if(this.minutes!=pet.minutes){
            return false;
        }
        if(this.breed!=pet.breed){
            return false;
        }
        if(this.observations!=pet.observations){
            return false;
        }
        if(!this.latitude.equals(pet.latitude)){
            return false;
        }
        if(!this.longitude.equals(pet.longitude)){
            return false;
        }
        return true;
    }

}
