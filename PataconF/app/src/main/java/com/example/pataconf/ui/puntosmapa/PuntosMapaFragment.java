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

public class EliminarPuntoFragment extends Fragment implements View.OnClickListener{

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private TextView tvarea;
    private TextView tvrecinto;
    private TextView tvsector;
    private EditText tvobservacion;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
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
    private ModeloVistaPunto punto;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_eliminarpunto, container, false);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");
        title = (TextView) root.findViewById(R.id.title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);
        this.tvarea = root.findViewById(R.id.tvarea);
        this.tvrecinto = root.findViewById(R.id.tvrecinto);
        this.tvsector = root.findViewById(R.id.tvsector);
        this.tvobservacion = root.findViewById(R.id.tvobservacion);

        this.tvdireccion.setText(punto.getDireccion());
        this.tvarea.setText(punto.getArea());
        this.tvrecinto.setText(punto.getRecinto());
        this.tvsector.setText(punto.getSector());
        this.tvobservacion.setText(punto.getObservacion());

        this.volver = root.findViewById(R.id.volver);
        this.eliminar = root.findViewById(R.id.eliminar);

        this.volver.setOnClickListener(this);
        this.eliminar.setOnClickListener(this);

        System.out.println("Observ: " + punto.getObservacion());
        System.out.println("id: "  +punto.getId());

        this.disableEditText(tvobservacion);

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

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Eliminar Punto");

        return root;
    }



    @Override
    public void onClick(View view) {
        if (view == this.volver){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
            .whereEqualTo("pid", user.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Punto p = document.toObject(Punto.class);
                            ModeloVistaPunto pu = new ModeloVistaPunto();
                            pu.setDireccion(p.getDireccion());
                            pu.setLat(p.getLat());
                            pu.setLng(p.getLng());
                            pu.setSector(p.getSector());
                            pu.setRecinto(p.getRecinto());
                            pu.setArea(p.getArea());
                            pu.setFoto(p.getFoto());
                            pu.setIslatas(p.isIslatas());
                            pu.setIsplastico(p.isIsplastico());
                            pu.setIsvidrio(p.isIsvidrio());
                            pu.setId(document.getId());
                            pu.setObservacion(p.getObservacion());
                            data.add(pu);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("puntos", data);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment lp = new PuntosListFragment();
                        lp.setArguments(bundle);
                        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                        fragmentTransaction.commit();
                    } else {

                    }
                }
            });
        }

        if (view==this.eliminar){
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
        }
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