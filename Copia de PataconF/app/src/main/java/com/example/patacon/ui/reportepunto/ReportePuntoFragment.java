package com.example.patacon.ui.reportepunto;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.SelectorDireccionMapa;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.example.patacon.ui.reportepunto2.ReportePunto2Fragment;
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
import java.util.Objects;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class ReportePuntoFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private String userid;
    private ArrayList<Punto> data2 = new ArrayList<>();

    private Button volver;
    private Button continuar;
    private boolean stateMap;
    private TextView error;

    private RadioButton lleno;
    private RadioButton deteriorado;
    private RadioButton otro;
    private RadioButton inexistente;
    private RadioGroup grupo;

    private FirebaseFirestore db;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;
    private ModeloVistaPunto punto;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_reportepunto, container, false);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");
        title = (TextView) root.findViewById(R.id.title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);
        this.error = root.findViewById(R.id.error);
        this.error.setVisibility(View.INVISIBLE);

        this.otro = root.findViewById(R.id.otro);
        this.inexistente = root.findViewById(R.id.inexistente);
        this.lleno = root.findViewById(R.id.lleno);
        this.deteriorado = root.findViewById(R.id.deteriorado);

        this.otro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                otro.setChecked(true);
                inexistente.setChecked(false);
                lleno.setChecked(false);
                deteriorado.setChecked(false);
                error.setVisibility(View.INVISIBLE);
            }
        });

        this.inexistente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inexistente.setChecked(true);
                otro.setChecked(false);
                lleno.setChecked(false);
                deteriorado.setChecked(false);
                error.setVisibility(View.INVISIBLE);
            }
        });

        this.deteriorado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inexistente.setChecked(false);
                otro.setChecked(false);
                lleno.setChecked(false);
                deteriorado.setChecked(true);
                error.setVisibility(View.INVISIBLE);
            }
        });

        this.lleno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inexistente.setChecked(false);
                otro.setChecked(false);
                lleno.setChecked(true);
                deteriorado.setChecked(false);
                error.setVisibility(View.INVISIBLE);
            }
        });


        System.out.println("Direccion en ver punto: " + punto.getDireccion());
        this.tvdireccion.setText(punto.getDireccion());

        this.volver = root.findViewById(R.id.volver);
        this.continuar = root.findViewById(R.id.continuar);

        this.volver.setOnClickListener(this);
        this.continuar.setOnClickListener(this);

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






        //this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        //this.eliminarfoto.setOnClickListener(this);

        /*this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });*/

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Reporte Punto");

        return root;
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

        if (view==this.continuar){
            Bundle bundle = new Bundle();

            if (this.deteriorado.isChecked() || this.inexistente.isChecked() || this.otro.isChecked() || this.lleno.isChecked()) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                bundle.putSerializable("punto", this.punto);
                if (this.deteriorado.isChecked())
                    bundle.putSerializable("motivo", "deteriorado");
                else if (this.inexistente.isChecked())
                    bundle.putSerializable("motivo", "inexistente");
                else if (this.otro.isChecked())
                    bundle.putSerializable("motivo", "otro");
                else if (this.lleno.isChecked())
                    bundle.putSerializable("motivo", "lleno");

                Fragment lp = new ReportePunto2Fragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
            else {
                this.error.setVisibility(View.VISIBLE);
            }
        }
    }




}