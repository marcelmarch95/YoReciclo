package com.example.patacon.ui.verpunto;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.example.patacon.ui.reportepunto.ReportePuntoFragment;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.GPSTracker;
import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class VerPuntoFragment extends Fragment implements View.OnClickListener, LocationListener{

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private TextView tvarea;
    private TextView tvrecinto;
    private TextView tvsector;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private String userid;
    private ArrayList<Punto> data2 = new ArrayList<>();

    private Button volver;
    private Button reportar;

    MapView mMapView;
    private GoogleMap googleMap;
    private TextView distancia;
    private Location location;

    private FirebaseFirestore db;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;
    private ModeloVistaPunto punto;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_verpunto, container, false);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");
        title = (TextView) root.findViewById(R.id.title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);
        this.tvarea = root.findViewById(R.id.tvarea);
        this.tvrecinto = root.findViewById(R.id.tvrecinto);
        this.tvsector = root.findViewById(R.id.tvsector);

        this.distancia = root.findViewById(R.id.distancia);

        this.reportar = root.findViewById(R.id.reportar);
        this.reportar.setOnClickListener(this);

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(getContext());

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(gpsTracker.latitude);
            locationA.setLongitude(gpsTracker.longitude);

            Location locationB = new Location("point B");

            locationB.setLatitude(Double.valueOf(this.punto.getLat()));
            locationB.setLongitude(Double.valueOf(this.punto.getLng()));

            System.out.println("Location A: " + locationA.getLatitude() + " " + locationA.getLongitude());
            System.out.println("Location B: " + locationB.getLatitude() + " " + locationB.getLongitude());

            float distance = locationA.distanceTo(locationB);
            int dis = math(distance);

            if (dis>1000){
                int km = dis/1000;
                int dif = dis-(km*1000);
                this.distancia.setText("A " + km + "km " + dif + "m de tu ubicacion");
            }
            else {
                this.distancia.setText("A " + dis + " metros de tu ubicación");
            }

        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

        System.out.println("Direccion en ver punto: " + punto.getDireccion());
        this.tvdireccion.setText(punto.getDireccion());
        this.tvarea.setText(punto.getArea());
        this.tvrecinto.setText(punto.getRecinto());
        this.tvsector.setText(punto.getSector());

        this.volver = root.findViewById(R.id.volver);

        this.volver.setOnClickListener(this);

        System.out.println("Observ: " + punto.getObservacion());
        System.out.println("id: "  +punto.getId());


        this.imgp = root.findViewById(R.id.imgp);
        this.imgl = root.findViewById(R.id.imgl);
        this.imgv = root.findViewById(R.id.imgv);

        if (punto.isIsplastico()==false) {
            imgp.setVisibility(View.INVISIBLE);
            imgp.getLayoutParams().height = 0;
            imgp.getLayoutParams().width = 0;
            imgp.requestLayout();
        }
        if (punto.isIslatas()==false){
            imgl.setVisibility(View.INVISIBLE);
            imgl.getLayoutParams().height = 0;
            imgl.getLayoutParams().width = 0;
            imgl.requestLayout();
        }
        if (punto.isIsvidrio()==false){
            imgv.setVisibility(View.INVISIBLE);
            imgv.getLayoutParams().height = 0;
            imgv.getLayoutParams().width = 0;
            imgv.requestLayout();
        }



        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.valueOf(punto.getLat()), Double.valueOf(punto.getLng()));
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Punto Limpio").snippet(punto.getDireccion()));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        //this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        //this.eliminarfoto.setOnClickListener(this);

        /*this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });*/

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Detalle Punto");



        return root;
    }

    public static int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }


    @Override
    public void onClick(View view) {
        if (view == this.volver){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    data2.add(document.toObject(Punto.class));
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("puntos", data2);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new PuntosMapaFragment();
                                lp.setArguments(bundle);
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();
                            } else {
                                System.out.println("Error al conectarse ");
                            }
                        }
                    });

        }

        if (view == this.reportar){
            Bundle bundle = new Bundle();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ModeloVistaPunto mvp = new ModeloVistaPunto();
            mvp.setPid(punto.getPid());
            mvp.setObservacion(punto.getObservacion());
            mvp.setId(punto.getId());
            mvp.setIsvidrio(punto.isIsvidrio());
            mvp.setIsplastico(punto.isIsplastico());
            mvp.setIslatas(punto.isIslatas());
            mvp.setLng(punto.getLng());
            mvp.setLat(punto.getLat());
            mvp.setArea(punto.getArea());
            mvp.setRecinto(punto.getRecinto());
            mvp.setSector(punto.getSector());
            mvp.setDireccion(punto.getDireccion());


            bundle.putSerializable("punto", mvp);
            Fragment lp = new ReportePuntoFragment();
            lp.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        /*if (view==this.eliminar){
            db = FirebaseFirestore.getInstance();
            System.out.println("Eliminar el punto con id:  " + punto.getId());

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new CargandoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();

            db.collection("punto").document(punto.getId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Eliminado");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Punto eliminado correctamente");
                            bundle.putSerializable("estado", true);

                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            Fragment info = new InformacionFragment();
                            info.setArguments(bundle);
                            fragmentTransaction2.replace(R.id.nav_host_fragment, info);
                            fragmentTransaction2.commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error al eliminar");

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Error al eliminar el punto.");
                            bundle.putSerializable("estado", false);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            Fragment info = new InformacionFragment();
                            info.setArguments(bundle);
                            fragmentTransaction.replace(R.id.nav_host_fragment, info);
                            fragmentTransaction.commit();
                        }
                    });
        }*/
    }

    public void validarDatos(){


    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}