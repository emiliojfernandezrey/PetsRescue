package org.ejfr.petsrescue.petsrescue;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.ejfr.petsrescue.petsrescue.model.FirebaseReferences;
import org.ejfr.petsrescue.petsrescue.model.Pet;
import org.ejfr.petsrescue.petsrescue.utils.PosicionGPS;
import org.ejfr.petsrescue.petsrescue.utils.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private Button buttonAddAlert;
    private Button buttonSearchAlerts;
    private Button buttonExitApp;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setActivity(this);

        buttonAddAlert = (Button) findViewById(R.id.buttonAddAlert);
        buttonSearchAlerts =(Button) findViewById(R.id.buttonSearchAlerts);
        buttonExitApp = (Button) findViewById(R.id.buttonExitApp);

        this.initFirebase();

        buttonAddAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlertActivity.class);
                startActivity(intent);
            }
        });

        buttonSearchAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,SearchAlertsMapsActivity.class);
                startActivity(intent);
            }
        });

        buttonExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.initFonts();
    }

    private void initFonts(){
        Utils.setTextFontButton(this.buttonAddAlert);
        Utils.setTextFontButton(this.buttonSearchAlerts);
        Utils.setTextFontButton(this.buttonExitApp);
    }

    private void initFirebase(){
        //https://firebase.google.com/docs/auth/android/start/
        //https://firebase.google.com/docs/auth/android/manage-users
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseAuth.signInWithEmailAndPassword(FirebaseReferences.DATABASE_USER, FirebaseReferences.DATABASE_PASSWORD);

        //Esto ha funcionado y user!= null , pero user.getname= null
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Cuando se cambia el inicio de sesion, se abre sesion o se cierra
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Si se cierra sesion el usuario es null
                if (user != null) {
                    // User is signed in
                    Log.i("SESSION", "User signed_in:" + user.getEmail());
                } else {
                    // User is signed out
                    Log.i("SESSION", "User signed_out");
                    Toast.makeText(context,getResources().getString(R.string.label_error_signin),Toast.LENGTH_LONG);
                    //finish();
                }
            }
        };
        Utils.getPetsDatabaseReference(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
