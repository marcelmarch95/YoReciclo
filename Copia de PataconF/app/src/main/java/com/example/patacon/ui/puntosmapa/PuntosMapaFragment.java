package com.example.patacon.ui.puntosmapa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.patacon.ui.verpunto.VerPuntoFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class PuntosMapaFragment extends Fragment implements View.OnClickListener{

    private OptionsPuntosListViewModel homeViewModel;
    private ArrayList<Punto> puntos = new ArrayList<>();
    private String userid;
    private TextView tvdireccion;

    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private TextView tvarea;
    private TextView tvrecinto;
    private TextView tvsector;

    private Button volver;
    private Button verpunto;
    private Punto puntosel;
    private LinearLayout la1;

    MapView mMapView;
    private GoogleMap googleMap;

    private FirebaseFirestore db;
    private View root;
    private Map<Marker, Punto> markers = new HashMap<>();

    private LinearLayout lay1;
    private LinearLayout lay2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_puntosmapa, container, false);

        this.puntos = (ArrayList<Punto>) getArguments().getSerializable("puntos");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.la1 = root.findViewById(R.id.la1);
        this.la1.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;


        //this.volver = root.findViewById(R.id.volver);
        this.tvdireccion = root.findViewById(R.id.tvdireccion);
        this.tvarea = root.findViewById(R.id.tvarea);
        this.tvrecinto = root.findViewById(R.id.tvrecinto);
        this.tvsector = root.findViewById(R.id.tvsector);

        //this.volver.setOnClickListener(this);
        this.verpunto = root.findViewById(R.id.verpunto);
        this.verpunto.setOnClickListener(this);
        this.verpunto.setVisibility(View.INVISIBLE);

        this.lay1 = root.findViewById(R.id.lay1);
        this.lay2 = root.findViewById(R.id.lay2);

        this.lay1.setVisibility(View.INVISIBLE);
        this.lay2.setVisibility(View.INVISIBLE);

        this.imgp = root.findViewById(R.id.imgp);
        this.imgl = root.findViewById(R.id.imgl);
        this.imgv = root.findViewById(R.id.imgv);


        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mMapView.onCreate(savedInstanceState);

        la1.requestLayout();
        mMapView.requestLayout();

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

                    int height = 100;
                    int width = 125;
                    BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.po);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    LatLng sydney = new LatLng(Double.valueOf(p.getLat()),Double.valueOf(p.getLng()));
                    Marker m = googleMap.addMarker(new MarkerOptions().position(sydney).title(p.getDireccion()).snippet(p.getArea())
                                //.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.po)));
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                    markers.put(m,p);
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                      @Override
                      public boolean onMarkerClick(Marker m) {
                          Punto punto = markers.get(m);
                          System.out.println("Punto seleccionado: " + punto.toString());

                          lay1.setVisibility(View.VISIBLE);
                          lay2.setVisibility(View.VISIBLE);

                          la1.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                          float density = getContext().getResources().getDisplayMetrics().density;
                          int dp = Math.round((float) 300 * density);

                          mMapView.getLayoutParams().height = dp;

                          la1.requestLayout();
                          mMapView.requestLayout();

                          tvdireccion.setText(punto.getDireccion());
                          tvarea.setText(punto.getArea());
                          tvrecinto.setText(punto.getRecinto());
                          tvsector.setText(punto.getSector());
                          verpunto.setVisibility(View.VISIBLE);

                          if (punto.isIsplastico()==false) {
                              imgp.setVisibility(View.INVISIBLE);
                              imgp.getLayoutParams().height = 0;
                              imgp.getLayoutParams().width = 0;
                              imgp.requestLayout();
                          }
                          else {
                              imgp.setVisibility(View.VISIBLE);
                              imgp.getLayoutParams().height =100;
                              imgp.getLayoutParams().width = 75;
                              imgp.requestLayout();
                          }
                          if (punto.isIslatas()==false){
                              imgl.setVisibility(View.INVISIBLE);
                              imgl.getLayoutParams().height = 0;
                              imgl.getLayoutParams().width = 0;
                              imgl.requestLayout();
                          }
                          else {
                              imgl.setVisibility(View.VISIBLE);
                              imgl.getLayoutParams().height =100;
                              imgl.getLayoutParams().width = 75;
                              imgl.requestLayout();
                          }
                          if (punto.isIsvidrio()==false){
                              imgv.setVisibility(View.INVISIBLE);
                              imgv.getLayoutParams().height = 0;
                              imgv.getLayoutParams().width = 0;
                              imgv.requestLayout();
                          }
                          else {
                              imgv.setVisibility(View.VISIBLE);
                              imgv.getLayoutParams().height =100;
                              imgv.getLayoutParams().width = 75;
                              imgv.requestLayout();
                          }

                          puntosel = punto;


                          return false;
                      }
                });

                // For dropping a marker at a point on the Map
                LatLng curico = new LatLng(Double.valueOf("-34.979923"),Double.valueOf("-71.225535"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(curico).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Puntos Limpios");

        return root;
    }





    @Override
    public void onClick(View view) {
        if (view==this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        if (view == this.verpunto){
            if (puntosel!=null){
                System.out.println("punto != null");
                ModeloVistaPunto mvp = new ModeloVistaPunto();
                mvp.setPid(puntosel.getPid());
                mvp.setObservacion(puntosel.getObservacion());
                mvp.setId(puntosel.getId());
                mvp.setIsvidrio(puntosel.isIsvidrio());
                mvp.setIsplastico(puntosel.isIsplastico());
                mvp.setIslatas(puntosel.isIslatas());
                mvp.setLng(puntosel.getLng());
                mvp.setLat(puntosel.getLat());
                mvp.setArea(puntosel.getArea());
                mvp.setRecinto(puntosel.getRecinto());
                mvp.setSector(puntosel.getSector());
                mvp.setDireccion(puntosel.getDireccion());

                Bundle bundle = new Bundle();
                bundle.putSerializable("punto", mvp);

                System.out.println("Punto antes de entrar a eliminar ." + puntosel.getPid());

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new VerPuntoFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}