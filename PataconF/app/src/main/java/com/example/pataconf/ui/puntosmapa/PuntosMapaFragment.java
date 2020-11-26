package com.example.pataconf.ui.puntosmapa;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
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

import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.pataconf.ui.puntos.PuntosListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class PuntosMapaFragment extends Fragment implements View.OnClickListener{

    private OptionsPuntosListViewModel homeViewModel;
    private ArrayList<Punto> puntos = new ArrayList<>();
    ImageView imgp;
    ImageView imgl;
    ImageView imgv;
    private String userid;

    private Button volver;
    private Button eliminar;

    MapView mMapView;
    private GoogleMap googleMap;

    private FirebaseFirestore db;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_puntosmapa, container, false);

        this.puntos = (ArrayList<Punto>) getArguments().getSerializable("puntos");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();


        this.volver = root.findViewById(R.id.volver);
        this.eliminar = root.findViewById(R.id.eliminar);

        this.volver.setOnClickListener(this);
        this.eliminar.setOnClickListener(this);


        /*this.imgp = root.findViewById(R.id.imgp);
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
        */


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

                for(Punto p: puntos){
                    LatLng sydney = new LatLng(Double.valueOf(p.getLat()),Double.valueOf(p.getLng()));
                    googleMap.addMarker(new MarkerOptions().position(sydney).title(p.getDireccion()).snippet(p.getArea()));
                }

                // For dropping a marker at a point on the Map

                LatLng curico = new LatLng(Double.valueOf("-34.979923"),Double.valueOf("-71.225535"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(curico).zoom(12).build();
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

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Puntos Limpios");

        return root;
    }



    @Override
    public void onClick(View view) {

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

}