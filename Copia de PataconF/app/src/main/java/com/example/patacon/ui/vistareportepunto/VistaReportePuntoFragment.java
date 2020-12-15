package com.example.patacon.ui.vistareportepunto;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.reportes.ReportesListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaReporte;
import Modelo.Punto;
import Modelo.Reporte;

import static android.support.constraint.Constraints.TAG;

public class VistaReportePuntoFragment extends Fragment implements View.OnClickListener {

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
    private TextView comentarios;
    private TextView fecha;

    private Button volver;
    private ImageView foto;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private View root;
    private ModeloVistaReporte mvr;

    private Reporte reporte;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_vistareportepunto, container, false);

        this.mvr = (ModeloVistaReporte) getArguments().getSerializable("mvr");

        title = (TextView) root.findViewById(R.id.title);

        mAuth = FirebaseAuth.getInstance();

        this.motivo = root.findViewById(R.id.motivo);
        this.motivo.setText(this.mvr.getReporte().getMotivo());

        this.comentarios = root.findViewById(R.id.comentarios);
        this.fecha = root.findViewById(R.id.fecha);
        this.fecha.setText("Creado el " + this.mvr.getReporte().getFecha() + " a las " + this.mvr.getReporte().getHora());

        if (this.mvr.getReporte().getComentarios().compareTo("")!=0){
            this.comentarios.setText(this.mvr.getReporte().getComentarios());
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);
        this.tvdireccion.setText(this.mvr.getPunto().getDireccion());

        this.volver = root.findViewById(R.id.volver);
        this.volver.setOnClickListener(this);

        this.foto = (ImageView) root.findViewById(R.id.imageView4);

        db = FirebaseFirestore.getInstance();

        this.reporte = new Reporte();

        if (this.mvr.getReporte().getMotivo().compareTo("otro")==0) {

            this.motivo.setText("OTRO MOTIVO");
            this.foto.setImageResource(R.drawable.otro);
        }
        else if (this.mvr.getReporte().getMotivo().compareTo("inexistente")==0) {
            this.motivo.setText("PUNTO INEXISTENTE");
            this.foto.setImageResource(R.drawable.inexistente);
        }
        else if (this.mvr.getReporte().getMotivo().compareTo("lleno")==0) {
            this.motivo.setText("PUNTO LLENO");
            this.foto.setImageResource(R.drawable.full);
        }
        else if (this.mvr.getReporte().getMotivo().compareTo("deteriorado")==0){
            this.motivo.setText("PUNTO DETERIORADO");
            this.foto.setImageResource(R.drawable.roto);
        }


        this.imgp = root.findViewById(R.id.imgp);
        this.imgl = root.findViewById(R.id.imgl);
        this.imgv = root.findViewById(R.id.imgv);

        if (this.mvr.getPunto().isIsplastico()==false) {
            imgp.setVisibility(View.INVISIBLE);
            imgp.getLayoutParams().height = 0;
            imgp.getLayoutParams().width = 0;
            imgp.requestLayout();
        }
        if (this.mvr.getPunto().isIslatas()==false){
            imgl.setVisibility(View.INVISIBLE);
            imgl.getLayoutParams().height = 0;
            imgl.getLayoutParams().width = 0;
            imgl.requestLayout();
        }
        if (this.mvr.getPunto().isIsvidrio()==false){
            imgv.setVisibility(View.INVISIBLE);
            imgv.getLayoutParams().height = 0;
            imgv.getLayoutParams().width = 0;
            imgv.requestLayout();
        }




        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Vista Reporte Punto");

        return root;
    }



    @Override
    public void onClick(View view) {

        if (view==this.volver){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            ArrayList<Reporte> reportes = new ArrayList<>();
            ArrayList<Punto> puntos = new ArrayList<>();
            ArrayList<ModeloVistaReporte> data = new ArrayList<>();

            db.collection("reporte")
            .whereEqualTo("idGenerador", user.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reporte p = document.toObject(Reporte.class);
                            if (p.getEstado().compareTo(mvr.getReporte().getEstado())==0){
                                p.setIdReporte(document.getId());
                                reportes.add(p);
                            }
                        }

                        db.collection("punto")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Punto p = document.toObject(Punto.class);
                                                p.setId(document.getId());
                                                puntos.add(p);
                                            }

                                            for (Reporte r: reportes){
                                                for (Punto p: puntos){
                                                    if (r.getIdPunto().compareTo(p.getId())==0){
                                                        ModeloVistaReporte mvr = new ModeloVistaReporte();
                                                        mvr.setPunto(p);
                                                        mvr.setReporte(r);
                                                        data.add(mvr);
                                                    }
                                                }
                                            }

                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("reportes", data);

                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                            Fragment lp = new ReportesListFragment();
                                            lp.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                            fragmentTransaction.commit();

                                        } else {

                                        }
                                    }
                                });

                    } else {

                    }
                }
            });
        }


    }

    private void aprobarReporte(){
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference washingtonRef = db.collection("reporte").document(this.mvr.getReporte().getIdReporte());

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("estado", "aprobado")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    private void rechazarReporte(){
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference washingtonRef = db.collection("reporte").document(this.mvr.getReporte().getIdReporte());

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("estado", "rechazado")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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



        reporte.setFoto("null");
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
                });

    }

}