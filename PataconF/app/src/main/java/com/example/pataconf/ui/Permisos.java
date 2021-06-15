package com.example.pataconf.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.SelectorDireccionMapa;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.InputStream;
import java.net.URL;

import Modelo.Recicladora;

public class Permisos extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private boolean ubicacion;
    private Button permitir1;
    private Button permitir2;
    private Button continuar;
    private ProgressBar progressBar;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient googleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_solicitarpermisos);

        permitir1 = findViewById(R.id.permitir1);
        permitir2 = findViewById(R.id.permitir2);
        continuar = findViewById(R.id.continuar);
        progressBar = findViewById(R.id.progressBar);

        permitir1.setOnClickListener(this);
        permitir2.setOnClickListener(this);
        continuar.setOnClickListener(this);

        googleApiClient = getAPIClientInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }


    }

    private void requestGPSSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        Toast.makeText(getApplication(), "GPS is already enable", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(Permisos.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        Toast.makeText(getApplication(), "Location settings are inadequate, and cannot be fixed here", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private GoogleApiClient getAPIClientInstance() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        return mGoogleApiClient;
    }

    @Override
    public void onBackPressed (){
        Toast.makeText(this, "Debes otorgar los permisos para poder continuar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view==this.permitir1){
            if (ContextCompat.checkSelfPermission(Permisos.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)   ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, /* Este codigo es para identificar tu request */ 1);
            else {
                requestGPSSettings();
            }
        }
        if (view==this.continuar){
            if (ubicacion){
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                System.out.println("Provider contains=> " + provider);
                if (provider.contains("gps") || provider.contains("network")){
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("tag", "signInWithEmail:success");
                    Intent i = new Intent(getBaseContext(), PerfilComerciante.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplication(), "Activa el GPS para continuar...", Toast.LENGTH_SHORT).show();
                }

            }
            else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                System.out.println("Provider contains=> " + provider);
                if (provider.contains("gps") || provider.contains("network")){
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("tag", "signInWithEmail:success");
                    Intent i = new Intent(getBaseContext(), PerfilComerciante.class);
                    startActivity(i);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 /* El codigo que puse a mi request */: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("tengo permiso");
                    ubicacion = true;
                    requestGPSSettings();
                } else {

                    System.out.println("no tengo permiso");
                }
                return;
            }
        }
    }
}
