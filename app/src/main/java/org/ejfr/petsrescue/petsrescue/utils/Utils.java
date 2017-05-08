package org.ejfr.petsrescue.petsrescue.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.ejfr.petsrescue.petsrescue.R;
import org.ejfr.petsrescue.petsrescue.model.FirebaseReferences;
import org.ejfr.petsrescue.petsrescue.model.Pet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by EmilioJosé on 21/04/2017.
 */

public class Utils {
    private static FirebaseDatabase database;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;
    private static FirebaseStorage firebaseStorage;
    private static HashMap<String,Pet> petHashMap;
    public static final LatLng DEFAULT_LOCATION = new LatLng(40.4173,-3.6830);
    public final static int DEFAULT_ZOOM = 16;//18;
    private static final String fontBalooBhaina="fonts/BalooBhaina.ttf";
    private static final String fontIndieFlower="fonts/IndieFlower.ttf";
    private static final String myFont= fontIndieFlower;
    public static final int DEFAULT_FONT_SIZE= 20;
    private static Typeface typeFace=null;
    private static Activity myActivity;
    private static boolean WANT_TO_USE_FONT=true;
    private static List<String> typeList;
    private static List<String> sizeList;

    public static void setActivity(Activity activity){
        myActivity=activity;
    }

    public static void setFontsOverride(Context context){
        FontsOverride.setDefaultFont(context, "DEFAULT", myFont);
        FontsOverride.setDefaultFont(context, "MONOSPACE", myFont);
        FontsOverride.setDefaultFont(context, "SERIF", myFont);
        FontsOverride.setDefaultFont(context, "SANS_SERIF", myFont);
    }

    public static void setTextFontButton(Button component){
        if(WANT_TO_USE_FONT) {
            Utils.setComponentFont(myFont, myActivity, component, false, 20);
        }
    }
    public static void setTextFontTextView(TextView component){
        if(WANT_TO_USE_FONT) {
            Utils.setComponentFont(myFont,myActivity, component, false, 16);
        }
    }

    public static void setTextFontBalooBhainaButton(Button component){
        if(WANT_TO_USE_FONT) {
            Utils.setComponentFont(fontBalooBhaina, myActivity, component, false, 20);
        }
    }

    public static void setTextFontBalooBhainaTextView(TextView component){
        if(WANT_TO_USE_FONT) {
            Utils.setComponentFont(myFont,myActivity, component, false, 16);
        }
    }

    public static void setComponentFont(String font, Activity activity, TextView component, boolean bold, int size){
        if(typeFace==null) {
            typeFace = Typeface.createFromAsset(activity.getAssets(), font);
        }
        if(bold){
            component.setTypeface(typeFace,Typeface.BOLD);
        }else{
            component.setTypeface(typeFace);
        }
        setComponentSize(component,size);
    }

    public static void setComponentFont(Activity myActivity, TextView component){
        if(typeFace==null) {
            typeFace = Typeface.createFromAsset(myActivity.getAssets(), myFont);
        }
        component.setTypeface(typeFace,Typeface.BOLD);
    }

    public static void setSize(TextView component,int size){
        component.setTextSize(size);
    }

    public static void setComponentSize(TextView component, int size){
        component.setTextSize(size);
    }

    public static void setComponentFontAndSize(Activity myActivity, TextView component, int size){
        setComponentFont(myActivity, component);
        setComponentSize(component, size);
    }

    public static void setComponentFontAndDefaultSize(Activity myActivity, TextView component){
        setComponentFontAndSize(myActivity, component, DEFAULT_FONT_SIZE);
    }

    private static FirebaseStorage getFirebaseStorage(){
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }

    private static StorageReference getStorageReference(){
        if (storageReference == null) {
            storageReference = getFirebaseStorage().getReference();
        }
        return storageReference;
    }

    @NonNull
    private static StorageReference getStoragePetsRescueImages(){
        return getStorageReference().child(FirebaseReferences.FIREBASE_STORAGE_REFERENCE_NAME);
    }

    @NonNull
    public static StorageReference getStoragePetsRescueImagesPhotos(){
        return getStoragePetsRescueImages().child(FirebaseReferences.FIREBASE_STORAGE_REFERENCE_PHOTOS);
    }

    private static FirebaseDatabase getDatabase(){
        if (database == null) {
            database= FirebaseDatabase.getInstance();
        }
        return database;
    }

    private static DatabaseReference getDatabaseReference(){
        if(databaseReference==null){
            databaseReference=getDatabase().getReference(FirebaseReferences.DATABASE_NAME_REFERENCE); //Entre paréntesis le pasamos la referencia
        }
        return databaseReference;
    }

    public static DatabaseReference getPetsDatabaseReference(boolean initialize){

        DatabaseReference refPets = getDatabaseReference().child(FirebaseReferences.PETS_REFERENCE);

        if(initialize) {
            //ValueEventListener valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            refPets.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.e("Count ", "" + snapshot.getChildrenCount());
                            if (petHashMap == null) {
                                petHashMap = new HashMap<String,Pet>();
                            }

                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Log.d("VALOR DE LA KEY",""+postSnapshot.getKey());
                                //petHashMap.add(postSnapshot.getValue(Pet.class));7
                                petHashMap.put(postSnapshot.getKey(),postSnapshot.getValue(Pet.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Error", "Error getting data from firebase database");
                        }
                    });
        }
        return refPets;
    }

    public static String getKeyOfPetHashMap(Pet pet){
        for (Map.Entry<String, Pet> entry : petHashMap.entrySet()) {
            Pet value = entry.getValue();
            if(pet.equals(value)){
                return entry.getKey();
            }
        }

        return null;
    }

    public static boolean updateTypeAlertOfPetInFirebaseDatabase(String key, String newTypeAlert) {
        Map<String, Object> taskMap = new HashMap<String, Object>();
        boolean ok = false;
        if (newTypeAlert.equals(Pet.TYPE_ALERT_PET_MATCHED)) {
            taskMap.put("typeAlert", newTypeAlert);
            try {
                Utils.getPetsDatabaseReference(false).child(key).updateChildren(taskMap);
                ok = true;
            } catch (Exception e) {
                Log.e("ERROR", "Error updating typeAlert: " + e.toString());
            }
        }
        return ok;
    }

    public static boolean addPetAlerToFirebaseDatabase(Pet pet){
        boolean success = false;
        try {
            //Store photoPath and pass it to addPetPictureToFirebaseStorage
            String photoPath = pet.getPhotoPath();
            File f = new File(pet.getPhotoPath());
            //We update the photoPath just to save it as filename.jpg without path
            pet.setPhotoPath(f.getName());
            Utils.getPetsDatabaseReference(false).push().setValue(pet);

            Utils.addPetPictureToFirebaseStorage(photoPath);
            success= true;
        }catch(Exception e){
            success= false;
        }
        return success;
    }

    public static void addPetPictureToFirebaseStorage(String photoPath){
        File f = new File(photoPath);
        if(f.exists()){
            StorageReference petRef = getStoragePetsRescueImagesPhotos().child(f.getName());
            //StorageReference petImagesRef = getStorage().child("images/"+f.getName());
            Uri file = Uri.fromFile(f);
            //StorageReference petRef = getStorage().child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = petRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ERROR","Error uploading picture to firabase storage");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("SUCCESS","Picture uploaded successfully");
                }
            });
        }
    }

    public static List<Pet> getPetListFromFirebase(){
        //The list already has its contents
        List<Pet> list = new ArrayList<Pet>();

        for (Map.Entry<String, Pet> entry : petHashMap.entrySet()) {
            String clave = entry.getKey();
            Pet value = entry.getValue();
            list.add(value);
        }

        return list;
    }

    public static List<LatLng> getListLatLngPetAlerts(){
        List<LatLng> list = new ArrayList<LatLng>();
        if (petHashMap != null) {
            for(Map.Entry<String,Pet> entry: petHashMap.entrySet()){
                Pet pet = entry.getValue();
                list.add(new LatLng(pet.getLatitude(),pet.getLongitude()));
            }
        }
        return list;
    }

    public static Pet getPetFromPetList(Double latitude, Double longitude){
        Pet petFound=null;
        if (petHashMap != null) {
            for(Map.Entry<String,Pet> entry: petHashMap.entrySet()){
                Pet pet = entry.getValue();
                Double lat = pet.getLatitude();
                Double lng = pet.getLongitude();
                if(lat.equals(latitude) && lng.equals(longitude)){
                    petFound= pet;
                    break;
                }
            }
        }
        return petFound;
    }

    public static String getFormattedDate(int year, int month, int day) {
        String m = month+"";
        String d = day+"";
        if(month<10){
            m="0"+month;
        }
        if(day<10){
            d="0"+day;
        }
        StringBuilder sb = new StringBuilder().append(d).append("/")
                .append(m).append("/").append(year);
        return sb.toString();
    }

    public static String getFormattedTime(int hour,int minutes){
        String h=hour+"";
        String m=minutes+"";
        if(hour<10){
            h="0"+hour;
        }
        if(minutes<10){
            m="0"+minutes;
        }
        StringBuilder sb = new StringBuilder().append(h).append(":")
                .append(m);
        return sb.toString();
    }

    public static boolean loadImageIntoImageViewFromFirebase(Context context, String photoName, ImageView imageViewPet){
        boolean result;
        try {
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(Utils.getStoragePetsRescueImagesPhotos().child(photoName))
                    //.thumbnail(0.1f)
                    .into(imageViewPet);
            result= true;
        }catch (Exception e){
            Log.e("Error","Error loading image: "+e.toString());
            System.out.println(""+e.toString());
            result= false;
        }
        return result;
    }

    public static int getArrayPositionOfValue(String value, String [] array){
        for(int i=0;i<array.length;i++){
            if(array[i].equalsIgnoreCase(value)){
                return i;
            }
        }
        return -1;
    }

    private static Location getLastKnownLocation(Context context) throws SecurityException{
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
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
    }

    /**Method that returns the current location
     *
     * @return
     */
    public static LatLng getCurrentLocation(Context context){
        LatLng latLng;
        try {
            Location location = Utils.getLastKnownLocation(context);
            latLng= new LatLng(location.getLatitude(), location.getLongitude());
        }catch(Exception e){
            Log.e("Error GPS","GPS disabled/Could not get current location");//OJO esto lo tendré que cambiar
            latLng= Utils.DEFAULT_LOCATION;
        }
        return latLng;
    }

    public static String getTypeAlertString(String typeAlert){
        String msg=null;
        if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_ABANDON)){
            msg = myActivity.getResources().getString(R.string.label_type_alert_abandoned);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_MISSED)){
            msg = myActivity.getResources().getString(R.string.label_type_alert_missed);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_PICKED_UP)){
            msg = myActivity.getResources().getString(R.string.label_type_alert_picked_up);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_MATCHED)){
            msg = myActivity.getResources().getString(R.string.label_type_alert_matched);
        }
        return msg;
    }

    public static List<String> getTypeList(){
        if(typeList==null) {
            String[] array = myActivity.getResources().getStringArray(R.array.string_array_type_animals);
            typeList = Arrays.asList(array);
        }
        return typeList;
    }

    public static List<String> getSizeList(){
        if(sizeList==null){
            String[] array = myActivity.getResources().getStringArray(R.array.string_array_size_animals);
            sizeList = Arrays.asList(array);
        }
        return sizeList;
    }

    public static int getPositionOfType(String type){
        for(int i=0;i<typeList.size();i++){
            if(typeList.get(i).equalsIgnoreCase(type)){
                return i;
            }
        }
        return -1;
    }

    public static int getPositionOfSize(String size){
        for(int i=0;i<sizeList.size();i++){
            if(sizeList.get(i).equalsIgnoreCase(size)){
                return i;
            }
        }
        return -1;
    }
}
