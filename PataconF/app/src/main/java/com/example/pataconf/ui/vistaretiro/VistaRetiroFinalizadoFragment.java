package com.example.pataconf.ui.vistaretiro;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.cargando.VistaFotografiaFragment;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.example.pataconf.ui.optionpuntos.OptionsPuntosListViewModel;
import com.example.pataconf.ui.optionretiros.OptionsRetirosListFragment;
import com.example.pataconf.ui.vistadireccion.VistaDireccionFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaRetiro;
import Modelo.Punto;
import Modelo.Retiro;

import static android.support.constraint.Constraints.TAG;

public class VistaRetiroFinalizadoFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private ImageView imgc;

    private TextView tplastico;
    private TextView tlata;
    private TextView tvidrio;
    private TextView tcarton;

    private LinearLayout flayout;
    private LinearLayout flayout1;

    private String userid;
    private TextView motivo;
    private ArrayList<Punto> data2 = new ArrayList<>();
    private TextView comentarios;

    private TextView fecha;
    private TextView hora;

    private TextView direccion;
    private TextView sector;

    private TextView nombre;
    private TextView numero;

    private Button volver;
    private ImageView foto;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private View root;
    private ModeloVistaRetiro mvr;

    private Button verdireccion;
    private Retiro retiro;

    private Button verfoto;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_vistaretirofinalizado, container, false);

        this.mvr = (ModeloVistaRetiro) getArguments().getSerializable("mvr");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.flayout = root.findViewById(R.id.flayout);
        this.flayout1 = root.findViewById(R.id.flayout1);

        this.imgc = root.findViewById(R.id.imgc);
        this.imgp = root.findViewById(R.id.imgp);
        this.imgl = root.findViewById(R.id.imgl);
        this.imgv = root.findViewById(R.id.imgv);

        this.nombre = root.findViewById(R.id.nombre);
        this.numero = root.findViewById(R.id.numero);

        this.direccion = root.findViewById(R.id.direccion);
        this.sector = root.findViewById(R.id.sector);

        this.fecha = root.findViewById(R.id.fecha);
        this.hora = root.findViewById(R.id.hora);

        this.comentarios = root.findViewById(R.id.comentarios);

        this.tcarton = root.findViewById(R.id.tcarton);
        this.tlata = root.findViewById(R.id.tlata);
        this.tvidrio = root.findViewById(R.id.tvidrio);
        this.tplastico = root.findViewById(R.id.tplastico);

        this.verdireccion = root.findViewById(R.id.verdireccion);
        this.verdireccion.setOnClickListener(this);

        this.tcarton.setText(mvr.getRetiro().getTotcarton());
        this.tvidrio.setText(mvr.getRetiro().getTotvidrio());
        this.tlata.setText(mvr.getRetiro().getTotlata());
        this.tplastico.setText(mvr.getRetiro().getTotplastico());

        if (mvr.getRetiro().getTotcarton().compareTo("0")==0){
            this.tcarton.setVisibility(View.INVISIBLE);
            this.imgc.setVisibility(View.INVISIBLE);
            this.imgc.getLayoutParams().width = 0;
            this.imgc.requestLayout();
        }
        if (mvr.getRetiro().getTotlata().compareTo("0")==0){
            this.tlata.setVisibility(View.INVISIBLE);
            this.imgl.setVisibility(View.INVISIBLE);
            this.imgl.getLayoutParams().width = 0;
            this.imgl.requestLayout();
        }
        if (mvr.getRetiro().getTotvidrio().compareTo("0")==0){
            this.tvidrio.setVisibility(View.INVISIBLE);
            this.imgv.setVisibility(View.INVISIBLE);
            this.imgv.getLayoutParams().width = 0;
            this.imgv.requestLayout();
        }
        if (mvr.getRetiro().getTotplastico().compareTo("0")==0){
            this.tplastico.setVisibility(View.INVISIBLE);
            this.imgp.setVisibility(View.INVISIBLE);
            this.imgp.getLayoutParams().width = 0;
            this.imgp.requestLayout();
        }

        this.flayout.requestLayout();
        this.flayout1.requestLayout();

        this.fecha.setText(mvr.getTramo().getFecha());
        this.hora.setText(mvr.getTramo().getHorainicio() + " - " + mvr.getTramo().getHorafinal());

        this.nombre.setText(mvr.getGenerador().getNombre() + " " + mvr.getGenerador().getApellido());
        this.numero.setText(mvr.getGenerador().getNumeroTelefono());

        this.direccion.setText(mvr.getDireccion().getCalle() + " #" + mvr.getDireccion().getNumero() + ", " + mvr.getDireccion().getCiudad());
        this.sector.setText(mvr.getDireccion().getSector());

        this.verfoto = root.findViewById(R.id.verfoto);
        this.verfoto.setOnClickListener(this);


        if (mvr.getRetiro().getComentarios().compareTo("")==0 || mvr.getRetiro().getComentarios()==null ||mvr.getRetiro().getComentarios().isEmpty())
            this.comentarios.setText("Sin Comentarios");
        else {
            this.comentarios.setText(mvr.getRetiro().getComentarios());
        }

        this.volver = root.findViewById(R.id.volver);
        this.volver.setOnClickListener(this);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Vista Retiro");

        return root;
    }



    @Override
    public void onClick(View view) {

        if (view==this.verdireccion){
            Bundle bundle = new Bundle();
            bundle.putSerializable("direccion", this.mvr.getDireccion());
            System.out.printf("aeiou");

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

            Fragment lp = new VistaDireccionFragment();
            lp.setArguments(bundle);
            fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction2.addToBackStack(null);
            fragmentTransaction2.commit();
        }

        if (view==this.verfoto){
            Bundle bundle = new Bundle();
            bundle.putSerializable("url", this.mvr.getRetiro().getFoto1());
            System.out.printf("aeiou");

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

            Fragment lp = new VistaFotografiaFragment();
            lp.setArguments(bundle);
            fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction2.addToBackStack(null);
            fragmentTransaction2.commit();
        }

        if (view== this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsRetirosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

    }

    private void finalizarRetiro(){
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference washingtonRef = db.collection("retiro").document(this.mvr.getRetiro().getId());

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("estado", "finalizado")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        InformacionFragment fi = new InformacionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mensaje", "Retiro finalizado");
                        bundle.putBoolean("estado", true);
                        fi.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                        fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                        fragmentTransaction2.commit();
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }




    private void crearReporte(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CargandoFragment cf = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, cf);
        fragmentTransaction.commit();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();



        /*reporte.setFoto("null");
        db.collection("reporte")
                .add(reporte)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        System.out.println("Reporte creado correctamente con ID: " + documentReference.getId());

                        InformacionFragment fi = new InformacionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mensaje", "Reporte enviado correctamente");
                        bundle.putBoolean("estado", true);
                        fi.setArguments(bundle);

                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                        fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                        fragmentTransaction2.commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        System.out.println("Error al crear el reporte");

                        InformacionFragment fi = new InformacionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mensaje", "Error al enviar reporte");
                        bundle.putBoolean("estado", false);
                        fi.setArguments(bundle);

                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                        fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                        fragmentTransaction2.commit();
                    }
                });*/

    }

}