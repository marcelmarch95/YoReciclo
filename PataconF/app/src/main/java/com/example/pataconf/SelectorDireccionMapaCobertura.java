package com.example.pataconf;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Modelo.Cobertura;

public class SelectorDireccionMapaCobertura extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private Location location;
    private double lat, lng;
    private Geocoder geocoder;
    private Button confirmar;

    private Marker mCurrLocationMarker, markerClient;
    private Marker markerActual;
    private LatLng latLng, latLngOtro;
    private boolean posModificada = false;
    private ProgressBar progressBar;
    private AppCompatSeekBar progress;
    private boolean editar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean primera = true;
    private Uri uri;
    private int radio = 1000;
    private LatLng curico = new LatLng(-34.98284,-71.23843);

    private Circle circle;
    private TextView progresotext;

    private Cobertura cobertura;

    private boolean stateMap;
    private SharedPreferences prefs = null;//Place it before onCreate you can access its values any where in this class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geocoder = new Geocoder(this, Locale.getDefault());

        db = FirebaseFirestore.getInstance();



        setContentView(R.layout.activity_selector_direccion_mapa_cobertura);
        initGoogleAPIClient();  //Init Google API Client
        checkPermissions();     //Check Permission


        progresotext = (TextView) findViewById(R.id.textView21);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stateMap = false;

        this.confirmar = findViewById(R.id.confirmar);
        this.confirmar.setOnClickListener(this);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.progressBar.setIndeterminate(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirmar dirección");

        Intent i = getIntent();
        //punto = (Punto) i.getExtras().get("punto");
        editar = (boolean) i.getExtras().get("editar");
        //uri = (Uri) i.getExtras().get("uri");

        cobertura = (Cobertura) i.getExtras().get("dire");

        progress = (AppCompatSeekBar) findViewById(R.id.seekbar);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress<1000){
                    seekBar.setProgress(1000);
                }
                radio = progress;
                if (circle!=null)
                    circle.setRadius(progress);
                int km = progress/1000;
                progresotext.setText(String.valueOf(km) + " km");
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });

        if (editar){
            this.lat = Double.valueOf(this.cobertura.getLat());
            this.lng = Double.valueOf(this.cobertura.getLng());
            this.radio = Integer.valueOf(this.cobertura.getRadio());
            progress.setProgress(radio);
            progresotext.setText(String.valueOf(radio/1000)+" km");

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    mGoogleMap = mMap;

                    // For showing a move to my location button
                    mGoogleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(Double.valueOf(cobertura.getLat()), Double.valueOf(cobertura.getLng()));
                    //mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Cobertura").snippet(direccion.getNombre()));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));//AQUI MODIFICA EL ZOOM AL MAPA SEGUN TUS NECESIDADES


                    circle = mGoogleMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat,lng))
                            .radius(radio)
                            .strokeWidth(10)
                            .strokeColor(R.color.colorPrimary)
                            .fillColor(Color.argb(170,227, 234, 243))
                            .clickable(true));
                }
            });
        }



        //System.out.println("Punto: " + punto.toString());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("volver");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("no hacer nada");
    }

    //////////////////////////////////////////////////////////////GPS/////////////////////////////////////////////////////////////////
    private void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(SelectorDireccionMapaCobertura.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(SelectorDireccionMapaCobertura.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SelectorDireccionMapaCobertura.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(SelectorDireccionMapaCobertura.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INTENT_ID);
        } else {
            ActivityCompat.requestPermissions(SelectorDireccionMapaCobertura.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("TAG", "SUCCESS");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("TAG", "RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(SelectorDireccionMapaCobertura.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("TAG", "GPS NO DISPONIBLE");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel, La aplicación se cerrará");
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Registrar el receptor de difusión para comprobar el estado del GPS.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }

    //Ejecutar en la interfaz de usuario
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Receptor de difusión para comprobar el estado del GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Si la acción es la ubicación
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Compruebe si el GPS está encendido o apagado
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                } else {
                    //Si el GPS está apagado, muestre el diálogo de ubicación
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    Log.e("About GPS", "GPS is Disabled in your device");
                    finish();
                }

            }
        }
    };

    /* Método de permiso On Request para verificar si el permiso se ha otorgado o no a Marshmallow Devices */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("TAG", "onRequestPermissionsResult");
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // Si se cancela la solicitud, las matrices de resultados están vacías.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso otorgado muestra el cuadro de diálogo de ubicación si APIClient no es nulo
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                    Toast.makeText(SelectorDireccionMapaCobertura.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("TAG", "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(9000);
        mLocationRequest.setFastestInterval(9000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        try {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("TAG", "onConnectionSuspended");
        mGoogleApiClient.connect(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionSuspended");
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("TAG", "onMapReady");
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            } else {
                Toast.makeText(this, "No cuentas con los permisos necesarios, cierra y abre de nuevo la aplicación", Toast.LENGTH_SHORT).show();
            }
        } else {
            buildGoogleApiClient();
        }



        CameraPosition cameraPosition = new CameraPosition.Builder().target(curico).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curico, 12));//AQUI MODIFICA EL ZOOM AL MAPA SEGUN TUS NECESIDADES

        if (!editar){
            circle = mGoogleMap.addCircle(new CircleOptions()
                    .center(curico)
                    .radius(1000)
                    .strokeWidth(10)
                    .strokeColor(R.color.colorPrimary)
                    .fillColor(Color.argb(170,227, 234, 243))
                    .clickable(true));
        }





        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {

                /* This code will save your location coordinates in SharedPrefrence when you click on the map and later you use it  */

                    lat = point.latitude;
                    lng = point.longitude;
                    posModificada = true;
                    mGoogleMap.clear();
                    //mGoogleMap.addMarker(new MarkerOptions().position(point));

                    circle.setCenter(point);
                    circle.remove();
                    circle = mGoogleMap.addCircle(new CircleOptions()
                            .center(point)
                            .radius(radio)
                            .strokeWidth(10)
                            .strokeColor(R.color.colorPrimary)
                            .fillColor(Color.argb(170,227, 234, 243))
                            .clickable(true));

                    List<Address> direccion;

                    try {
                        direccion = geocoder.getFromLocation(lat,lng, 1); // 1 representa la cantidad de resultados a obtener
                        String address = direccion.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = direccion.get(0).getLocality();
                        String state = direccion.get(0).getAdminArea();
                        String country = direccion.get(0).getCountryName();
                        String postalCode = direccion.get(0).getPostalCode();
                        System.out.println(direccion.toArray());
                        //TextView dire = findViewById(R.id.nombre);
                        //dire.setText(address);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected synchronized void buildGoogleApiClient() {
        Log.e("TAG", "buildGoogleApiClient");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.reconnect();
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == this.confirmar){

            mAuth = FirebaseAuth.getInstance();

            if (this.lat==0 || this.lng==0){
                this.lat = curico.latitude;
                this.lng = curico.longitude;
            }

            this.cobertura.setLat(String.valueOf(this.lat));
            this.cobertura.setLng(String.valueOf(this.lng));
            this.progressBar.setVisibility(View.VISIBLE);
            System.out.println("Radio: " + radio);
            this.cobertura.setRadio(String.valueOf(radio));
            this.cobertura.setUid(mAuth.getUid());

            if (editar){
                db = FirebaseFirestore.getInstance();
                db.collection("cobertura").document(this.cobertura.getId())
                        .set(this.cobertura)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Dirección editada correctamente");
                                Intent i = new Intent(getBaseContext(), Informacion.class);
                                i.putExtra("result", true);
                                i.putExtra("mensaje", "Dirección editada correctamente");
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                System.out.println("Error al editar el punto");
                                Intent i = new Intent(getBaseContext(), Informacion.class);
                                i.putExtra("result", false);
                                i.putExtra("mensaje", "Error al editar cobertura");
                                startActivity(i);
                            }
                        });
            }
            else {
                db.collection("cobertura")
                        .add(cobertura)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                System.out.println("Cobertura creada correctamente con ID: " + documentReference.getId());
                                Intent i = new Intent(getBaseContext(), Informacion.class);
                                i.putExtra("result", true);
                                i.putExtra("mensaje", "Cobertura creada correctamente");
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                System.out.println("Error al crear la cobertura");
                                Intent i = new Intent(getBaseContext(), Informacion.class);
                                i.putExtra("result", false);
                                i.putExtra("mensaje", "Error al crear cobertura");
                                startActivity(i);
                            }
                        });
            }



        }



    }

    @Override
    public void onLocationChanged(Location location) {

    }
}