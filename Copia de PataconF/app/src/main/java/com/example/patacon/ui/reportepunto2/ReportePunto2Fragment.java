package com.example.patacon.ui.reportepunto2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class ReportePunto2Fragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private String userid;
    private TextView motivo;
    private ArrayList<Punto> data2 = new ArrayList<>();

    private Button volver;
    private boolean stateMap;
    private Button selectfoto;
    private Button guardar;
    private Button eliminarfoto;
    private Button continuar;
    private Button exit;
    private ImageView foto;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;
    private ModeloVistaPunto punto;
    private String motivoreporte;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_reportepunto2, container, false);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");
        this.motivoreporte = getArguments().getString("motivo");
        title = (TextView) root.findViewById(R.id.title);



        this.motivo = root.findViewById(R.id.motivo);
        this.motivo.setText(this.motivoreporte);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);

        System.out.println("Direccion en ver punto: " + punto.getDireccion());
        this.tvdireccion.setText(punto.getDireccion());

        this.volver = root.findViewById(R.id.volver);

        this.volver.setOnClickListener(this);

        System.out.println("Observ: " + punto.getObservacion());
        System.out.println("id: "  +punto.getId());
        this.foto = (ImageView) root.findViewById(R.id.imageView4);
        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);
        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        if (this.motivoreporte.compareTo("otro")==0) {

            this.motivo.setText("OTRO MOTIVO");
            this.foto.setImageResource(R.drawable.otro);
        }
        else if (this.motivoreporte.compareTo("inexistente")==0) {
            this.motivo.setText("PUNTO INEXISTENTE");
            this.foto.setImageResource(R.drawable.inexistente);
        }
        else if (this.motivoreporte.compareTo("lleno")==0) {
            this.motivo.setText("PUNTO LLENO");
            this.foto.setImageResource(R.drawable.full);
        }
        else if (this.motivoreporte.compareTo("deteriorado")==0){
            this.motivo.setText("PUNTO DETERIORADO");
            this.foto.setImageResource(R.drawable.roto);
        }

        this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

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


        this.continuar = root.findViewById(R.id.continuar);
        this.continuar.setOnClickListener(this);



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


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }



    @Override
    public void onClick(View view) {
        if (view == this.eliminarfoto){
            this.foto.setImageResource(R.drawable.imagen2);
            this.eliminarfoto.setEnabled(false);
        }

        if (view==this.continuar){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            InformacionFragment fi = new InformacionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("mensaje", "Reporte enviado correctamente");
            bundle.putBoolean("estado", true);
            fi.setArguments(bundle);

            fragmentTransaction.replace(R.id.nav_host_fragment, fi);
            fragmentTransaction.commit();
        }

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
    }




}