package com.example.patacon;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;

public class PermisosRegistro extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private boolean ubicacion;
    private boolean camara = false;
    private Button permitir1;
    private Button permitir2;
    private Button continuar;
    private ProgressBar progressBar;
    private LinearLayout lala;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    private GoogleApiClient googleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_solicitarpermisos);

        permitir1 = findViewById(R.id.permitir1);
        permitir2 = findViewById(R.id.permitir2);
        continuar = findViewById(R.id.continuar);
        progressBar = findViewById(R.id.progressBar);
        lala = findViewById(R.id.lala);

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
                        Toast.makeText(getApplication(), "GPS se encuentra habilitado", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(PermisosRegistro.this, REQUEST_CHECK_SETTINGS);
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
    public void onClick(View view) {
        if (view==this.permitir1){
            if (ContextCompat.checkSelfPermission(PermisosRegistro.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)   ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, /* Este codigo es para identificar tu request */ 1);
            else {
                requestGPSSettings();
            }
        }

        if (view == this.permitir2){
            // verifico si el usuario dio los permisos para la camara
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // verificamos la version de ANdroid que sea al menos la M para mostrar
                    // el dialog de la solicitud de la camara
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.CAMERA)) ;
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return;
            } else {
                Toast.makeText(this, "Permiso de cámara habilitado", Toast.LENGTH_LONG).show();
                camara = true;
            }
        }

        if (view==this.continuar){
            if (ubicacion){
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                System.out.println("Provider contains=> " + provider);
                if (provider.contains("gps") || provider.contains("network")){
                    if (camara){
                        progressBar.setIndeterminate(true);
                        progressBar.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getApplication(), "Otorgar permisos de cámara para continuar", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplication(), "Activa el GPS para continuar...", Toast.LENGTH_SHORT).show();
                }

            }
            else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                System.out.println("Provider contains=> " + provider);
                if (provider.contains("gps") || provider.contains("network")){
                    if (camara){
                        progressBar.setIndeterminate(true);
                        progressBar.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getApplication(), "Otorgar permisos de cámara para continuar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed (){
        Toast.makeText(this, "Debes otorgar los permisos para poder continuar", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 /* El codigo que puse a mi request */: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso de ubicación habilitado", Toast.LENGTH_LONG).show();
                    permitir1.setText("HABILITADO");
                    lala.requestLayout();
                    ubicacion = true;
                    requestGPSSettings();
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show();
                    ubicacion = false;
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso de cámara habilitado", Toast.LENGTH_LONG).show();
                    camara = true;
                    permitir2.setText("HABILITADO");
                    lala.requestLayout();
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_LONG).show();
                    camara = false;
                }
            }
        }
    }
}
