package org.ejfr.petsrescue.petsrescue;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.ejfr.petsrescue.petsrescue.model.Pet;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    private Button buttonDate;
    private Button buttonTime;
    private Button buttonSelectPhoto;
    private Button buttonLocation;
    private Button buttonSendAlert;
    private Button buttonCancelAlert;
    private ImageView imageViewPhoto;
    private CheckBox checkBoxLocation;
    private Spinner spinnerType;
    private Spinner spinnerSize;
    private TextView textViewDate;
    private TextView textViewTime;
    private EditText editTextBreed;
    private EditText editTextObservations;
    private TextView textViewTypeOfAlert;
    private TextView textViewType;
    private TextView textViewSize;
    private TextView textViewBreed;
    private TextView textViewObservations;
    private RadioGroup radioGroupTypeAlert;
    private RadioButton radioButtonAbandoned;
    private RadioButton radioButtonMissed;
    private RadioButton radioButtonPickedUp;
    private Context context = this;
    private static final int KEY_DIALOG_DATE_PICKER = 0;
    private static final int KEY_DIALOG_TIME_PICKER = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_ACTIVITY_MAPS=3;
    private static final int REQUEST_SELECT_PHOTO=4;
    private Intent mapsIntent;
    private static LatLng latLngPosition;
    private static String typeAlert = Pet.TYPE_ALERT_PET_DEFAULT;
    private static String type="dog";
    private static String size="big";
    private static int year=2017, month=04, day=03;
    private static int hour=12,minutes=30;
    private static String currentPhotoPath=null;
    private static String breed=null;
    private static String observations=null;
    private static boolean onlyOnce=true;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0,int arg1, int arg2, int arg3) {
                showDate(arg1, arg2+1, arg3);
        }
     };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                showTime(hourOfDay,minute);
        }
    };

    private void setAlertActivityTitle(String typeAlert){
        if(typeAlert.equals(Pet.TYPE_ALERT_PET_ABANDON)){
            this.setTitle(getResources().getString(R.string.title_type_alert_abandoned));
        }else if(typeAlert.equals(Pet.TYPE_ALERT_PET_PICKED_UP)){
            this.setTitle(getResources().getString(R.string.title_type_alert_picked_up));
        }else if(typeAlert.equals(Pet.TYPE_ALERT_PET_MISSED)){
            this.setTitle(getResources().getString(R.string.title_type_alert_missed));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        //this.typeAlert = Pet.TYPE_ALERT_PET_ABANDON;
        this.spinnerType = (Spinner) findViewById(R.id.spinnerType);
        this.spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
        this.imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        this.checkBoxLocation = (CheckBox) findViewById(R.id.checkBoxLocation);
        this.buttonDate = (Button) findViewById(R.id.buttonDate);
        this.buttonTime = (Button) findViewById(R.id.buttonTime);
        this.buttonSelectPhoto = (Button) findViewById(R.id.buttonSelectPhoto);
        this.buttonLocation = (Button) findViewById(R.id.buttonLocation);
        this.buttonSendAlert = (Button) findViewById(R.id.buttonSendAlert);
        this.textViewDate = (TextView) findViewById(R.id.textViewDate);
        this.textViewTime = (TextView) findViewById(R.id.textViewTime);
        this.editTextBreed = (EditText) findViewById(R.id.editTextBreed);
        this.editTextObservations = (EditText) findViewById(R.id.editTextObservations);
        this.buttonCancelAlert = (Button) findViewById(R.id.buttonCancelAlert);
        this.radioGroupTypeAlert = (RadioGroup) findViewById(R.id.radioGroupAlertType);
        this.radioButtonAbandoned = (RadioButton) findViewById(R.id.radioButtonAlertAbandoned);
        this.radioButtonMissed = (RadioButton) findViewById(R.id.radioButtonAlertMissed);
        this.radioButtonPickedUp = (RadioButton) findViewById(R.id.radioButtonAlertPickedUp);

        this.textViewTypeOfAlert = (TextView) findViewById(R.id.textViewTypeOfAlert);
        this.textViewSize = (TextView) findViewById(R.id.textViewLabelSize);
        this.textViewType = (TextView) findViewById(R.id.textViewLabelType);
        this.textViewDate = (TextView) findViewById(R.id.textViewDate);
        this.textViewTime = (TextView) findViewById(R.id.textViewTime);
        this.textViewBreed = (TextView) findViewById(R.id.textViewBreed);
        this.textViewObservations = (TextView) findViewById(R.id.textViewObservations);

        this.setInitDate();
        this.setInitTime();

        this.radioGroupTypeAlert.check(R.id.radioButtonAlertAbandoned);
        setSelectedRadioButton(R.id.radioButtonAlertAbandoned);
        this.radioGroupTypeAlert.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                setSelectedRadioButton(checkedId);
            }
        });
        //Init spinners
        String[] types = getResources().getStringArray(R.array.string_array_type_animals);
        String[] sizes = getResources().getStringArray(R.array.string_array_size_animals);
        List<String> list1 = Arrays.asList(types);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter1);

        List<String> list2 = Arrays.asList(sizes);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dataAdapter2);


        this.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(AlertActivity.KEY_DIALOG_DATE_PICKER);
            }
        });

        this.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(AlertActivity.KEY_DIALOG_TIME_PICKER);
            }
        });

        this.buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dispatchSelectPictureIntent();
                selectPicture();
            }
        });

        this.buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsIntent = new Intent(context, MapsActivity.class);
                startActivityForResult(mapsIntent,REQUEST_ACTIVITY_MAPS);
            }
        });

        this.buttonSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //I have to send the alert
                if(sendAlert()) {
                    finish();
                    initalizeDefaultValues();
                }
            }
        });

        this.buttonCancelAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                initalizeDefaultValues();
            }
        });

        this.checkBoxLocation.setClickable(false);
        this.checkStateCheckBoxLocalitation();
        this.showInformationMessage();
        this.initializeValues();
        this.initFonts();
    }//end onCreate

    private void initFonts(){
        Utils.setTextFontButton(this.buttonCancelAlert);
        Utils.setTextFontButton(this.buttonDate);
        Utils.setTextFontButton(this.buttonLocation);
        Utils.setTextFontButton(this.buttonSelectPhoto);
        Utils.setTextFontButton(this.buttonSendAlert);
        Utils.setTextFontButton(this.buttonTime);

        Utils.setTextFontTextView(this.textViewTypeOfAlert);
        Utils.setTextFontTextView(this.textViewType);
        Utils.setTextFontTextView(this.textViewSize);
        Utils.setTextFontTextView(this.textViewBreed);
        Utils.setTextFontTextView(this.textViewObservations);

        Utils.setTextFontTextView(this.radioButtonAbandoned);
        Utils.setTextFontTextView(this.radioButtonMissed);
        Utils.setTextFontTextView(this.radioButtonPickedUp);
//        Utils.setTextFontTextView(this.textViewDate);
//        Utils.setTextFontTextView(this.textViewTime);
    }

    private void initalizeDefaultValues(){
        typeAlert=Pet.TYPE_ALERT_PET_DEFAULT;
        type = getResources().getStringArray(R.array.string_array_type_animals)[0];
        size = getResources().getStringArray(R.array.string_array_size_animals)[0];
        day= Calendar.getInstance().getTime().getDay();
        month = Calendar.getInstance().getTime().getMonth();
        year = Calendar.getInstance().getTime().getYear();
        hour = Calendar.getInstance().getTime().getHours();
        minutes = Calendar.getInstance().getTime().getMinutes();
        currentPhotoPath= "";
        latLngPosition= null;//new LatLng(Utils.DEFAULT_LOCATION.latitude,Utils.DEFAULT_LOCATION.longitude);
        breed=null;
        observations=null;
        this.initializeValues();
    }

    private void initializeValues(){
        if(typeAlert!=null && typeAlert.trim().length()>0&& typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_ABANDON)){
            this.radioGroupTypeAlert.check(R.id.radioButtonAlertAbandoned);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_MISSED)){
            this.radioGroupTypeAlert.check(R.id.radioButtonAlertMissed);
        }else if(typeAlert.equalsIgnoreCase(Pet.TYPE_ALERT_PET_PICKED_UP)){
            this.radioGroupTypeAlert.check(R.id.radioButtonAlertPickedUp);
        }
        if(type!=null && type.trim().length()>0){
            int position= Utils.getArrayPositionOfValue(type,getResources().getStringArray(R.array.string_array_type_animals));
            if(position!=-1) {
                this.spinnerType.setSelection(position);
            }
        }
        if(size!=null && size.trim().length()>0){
            int position= Utils.getArrayPositionOfValue(type,getResources().getStringArray(R.array.string_array_size_animals));
            if(position!=-1) {
                this.spinnerSize.setSelection(position);
            }
        }
        if(day !=0 && month!=0 && year !=0){
            String date = Utils.getFormattedDate(year,month,day);
            this.textViewDate.setText(date);
        }
        if(hour !=0 && minutes != 0){
            String time = Utils.getFormattedTime(hour,minutes);
            this.textViewTime.setText(time);
        }
        if (currentPhotoPath != null && currentPhotoPath.trim().length() > 0) {
            try {
                File file = new File(currentPhotoPath);
                if(file.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageViewPhoto.setImageBitmap(bitmap);
                }else{
                    Log.e("ERROR","Error setting photo");
                    currentPhotoPath="";
                }
            }catch (Exception e){
                Log.e("ERROR","Error setting photo");
                currentPhotoPath="";
            }
        }else{
            imageViewPhoto.setImageResource(R.drawable.default_pet_image);
        }

        if(latLngPosition==null) {
            this.checkBoxLocation.setChecked(false);
        }

        if(breed!=null && breed.trim().length()>0){
            editTextBreed.setText(breed);
        }else{
            editTextBreed.setText("");
        }
        if(observations!=null && observations.trim().length()>0){
            editTextObservations.setText(observations);
        }else{
            editTextObservations.setText("");
        }
    }

    private void showInformationMessage(){
        if(onlyOnce) {
            new AlertDialog.Builder(AlertActivity.this)
                    .setTitle(getResources().getString(R.string.toast_title_new_alert))
                    .setMessage(getResources().getString(R.string.toast_message_new_alert))
                    .setIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void checkStateCheckBoxLocalitation(){
        this.checkBoxLocation.setChecked(this.latLngPosition!=null
                && this.latLngPosition.latitude!=0 && this.latLngPosition.longitude!=0);
    }

    private void setSelectedRadioButton(int checkId){
        switch (checkId){
            case R.id.radioButtonAlertAbandoned:
                this.typeAlert = Pet.TYPE_ALERT_PET_ABANDON;
                break;
            case R.id.radioButtonAlertMissed:
                this.typeAlert = Pet.TYPE_ALERT_PET_MISSED;
                break;
            case R.id.radioButtonAlertPickedUp:
                this.typeAlert = Pet.TYPE_ALERT_PET_PICKED_UP;
                break;
        }
        this.setAlertActivityTitle(typeAlert);
    }

    private boolean sendAlert(){
        String type = this.spinnerType.getSelectedItem().toString();
        String size = this.spinnerSize.getSelectedItem().toString();
        if(this.currentPhotoPath==null){
            this.currentPhotoPath="";
        }
        //Si hay imagen guardarla en firebase
        String photoPath = this.currentPhotoPath;
        String breed = this.editTextBreed.getText().toString();
        String observations = this.editTextObservations.getText().toString();

        boolean alertSent=false;

        Pet newPet;
        if (latLngPosition != null) {
            if(Pet.checkFields(type,size,year,month,day,hour,minutes,currentPhotoPath,latLngPosition.latitude,latLngPosition.longitude,breed,observations,typeAlert)) {
                newPet = new Pet(type, size,year,month,day,hour,minutes,currentPhotoPath,
                        latLngPosition.latitude,latLngPosition.longitude,breed,observations,typeAlert);
                boolean success= Utils.addPetAlerToFirebaseDatabase(newPet);
                if(success){
                    Toast.makeText(context,getResources().getString(R.string.success_storing_pet_alert),Toast.LENGTH_LONG).show();
                    alertSent=true;
                }else{
                    Toast.makeText(context,getResources().getString(R.string.error_storing_pet_alert),Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, getResources().getString(R.string.error_checking_fields_in_pet_alert), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, getResources().getString(R.string.error_checking_fields_in_pet_alert), Toast.LENGTH_SHORT).show();
        }
        return alertSent;
    }

    public void setInitDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    private void showDate(int year, int month, int day) {
        this.year=year;
        this.month=month;
        this.day=day;
        this.textViewDate.setText(Utils.getFormattedDate(this.year,this.month,this.day));
    }

    public void setInitTime(){
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        showTime(hour,minutes);
    }

    private void showTime(int hour,int minutes){
        this.hour=hour;
        this.minutes=minutes;
        this.textViewTime.setText(Utils.getFormattedTime(this.hour,this.minutes));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == AlertActivity.KEY_DIALOG_DATE_PICKER) {
            if(month>0) month--;
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } else if(id== AlertActivity.KEY_DIALOG_TIME_PICKER){
            return new TimePickerDialog(this,myTimeListener,hour,minutes,false);
        }
        return null;
    }

    private void selectPicture(){
        Intent galleryIntent = this.getGalleryIntent();
        Intent cameraIntent = this.getCameraIntent();

        Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select_picture));

        List<Intent> list = new ArrayList<>();
        list.add(cameraIntent);

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,list.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, REQUEST_SELECT_PHOTO);
    }

    private Intent getGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        return galleryIntent;
    }

    private Intent getCameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ERROR","Error creating photo phile");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "org.ejfr.petsrescue.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        return takePictureIntent;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult","requestCode: "+requestCode+ ", resultCode: "+ resultCode+", data: "+data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACTIVITY_MAPS:
                    try {
                        Bundle resultData = data.getExtras();
                        Double latitude = Double.parseDouble(resultData.get("latitude").toString());
                        Double longitude = Double.parseDouble(resultData.get("longitude").toString());
                        this.latLngPosition = new LatLng(latitude,longitude);
                    }catch (Exception e){
                        Log.e("ERROR","Error getting position from MapsActivity");
                        this.latLngPosition=new LatLng(Utils.DEFAULT_LOCATION.latitude,Utils.DEFAULT_LOCATION.longitude);
                    }
                    this.checkStateCheckBoxLocalitation();
                    break;
                case REQUEST_TAKE_PHOTO:
                    try {
                        File file = new File(currentPhotoPath);
                        if(file.exists()){
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imageViewPhoto.setImageBitmap(bitmap);
                        }else{
                            Log.e("ERROR","Error getting photo");
                            currentPhotoPath="";
                        }
                    }catch (Exception e){
                        Log.e("ERROR","Error getting photo");
                        currentPhotoPath="";
                    }
                    break;
                case REQUEST_SELECT_PHOTO:
                    if (data == null && currentPhotoPath!= null && currentPhotoPath.length()>0) {
                        try {
                            File file = new File(currentPhotoPath);
                            if(file.exists()){
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                imageViewPhoto.setImageBitmap(bitmap);
                            }else{
                                Log.e("ERROR","Error getting photo");
                                currentPhotoPath="";
                            }
                        }catch (Exception e){
                            Log.e("ERROR","Error getting photo");
                            currentPhotoPath="";
                        }
                    }else{
                        if(data!=null){//I have to update the currentPhotoPath
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };

                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            currentPhotoPath = cursor.getString(columnIndex);
                            cursor.close();

                            imageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
                        }
                    }
                    break;
            }//end switch
        }//end if resultCode==RESULT_OK
    }//end onActivityResult

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onlyOnce=false;
        if(this.radioGroupTypeAlert.getId()==R.id.radioButtonAlertAbandoned){
            typeAlert=Pet.TYPE_ALERT_PET_ABANDON;
        } else if (this.radioGroupTypeAlert.getId()==R.id.radioButtonAlertMissed){
            typeAlert=Pet.TYPE_ALERT_PET_MISSED;
        }else if(this.radioGroupTypeAlert.getId()==R.id.radioButtonAlertPickedUp){
            typeAlert=Pet.TYPE_ALERT_PET_PICKED_UP;
        }
        type = this.spinnerType.getSelectedItem().toString();
        //type = getResources().getStringArray(R.array.string_array_type_animals)[position];

        size = this.spinnerSize.getSelectedItem().toString();
        //size = getResources().getStringArray(R.array.string_array_size_animals)[position];

        breed = this.editTextBreed.getText().toString();
        observations = this.editTextObservations.getText().toString();
    }
}
