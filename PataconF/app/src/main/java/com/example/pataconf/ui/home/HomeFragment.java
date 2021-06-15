package com.example.pataconf.ui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.example.pataconf.ModeloVistaDashboardAdapter;
import com.example.pataconf.R;
import com.example.pataconf.ui.agregarpunto.AgregarPuntoFragment;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.cargando.SinDocumentoFragment;
import com.example.pataconf.ui.puntosmapa.PuntosMapaFragment;
import com.example.pataconf.ui.reportes.ReportesListFragment;
import com.example.pataconf.ui.retiroslist.RetirosListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.Generador;
import Modelo.ModeloVistaReporte;
import Modelo.ModeloVistaRetiro;
import Modelo.Punto;
import Modelo.Recicladora;
import Modelo.Reporte;
import Modelo.Retiro;
import Modelo.TramoRetiro;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaDashboardAdapter adapter;
    private FirebaseAuth mAuth;
    private Recicladora recicladora;
    private TextView name;
    private Button mapa;
    private FirebaseFirestore db;
    private Button agendar;
    private Button reportar;
    private ArrayList<Punto> data2 = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        name = (TextView) root.findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mapa = (Button) root.findViewById(R.id.mapa);
        agendar = (Button) root.findViewById(R.id.agendar);
        reportar = (Button) root.findViewById(R.id.reportar);

        mapa.setOnClickListener(this);
        agendar.setOnClickListener(this);
        reportar.setOnClickListener(this);

        //Generador prueba = getArguments().getSerializable("generador");
        //System.out.println("Prueba: " + prueba.toString());

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("recicladora").document(currentUser.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        recicladora = (Recicladora) document.toObject(Recicladora.class);
                        name.setText(recicladora.getNombreEmpresa());
                    } else {
                        System.out.println("error1");
                    }
                } else {
                    System.out.println("error2");
                }
            }
        });

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        getActivity().setTitle("f title");



        /*rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaDashboardAdapter(dataSet());
        rvMusicas.setAdapter(adapter);*/
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view==this.mapa){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new AgregarPuntoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        if (view==this.reportar){
            cargarReportes("pendiente");
        }

        if (view == this.agendar){
            cargarRetiros("pendiente");
        }
    }

    public void cargarRetiros(String estado){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lp = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Retiro> retiros = new ArrayList<>();
        ArrayList<TramoRetiro> tramos = new ArrayList<>();
        ArrayList<Direccion> direccions = new ArrayList<>();
        ArrayList<ModeloVistaRetiro> data = new ArrayList<>();
        ArrayList<Generador> generadores = new ArrayList<>();

        db.collection("retiro")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Retiro r = document.toObject(Retiro.class);
                                if (r.getIdrecicladora().compareTo(user.getUid())==0){
                                    r.setId(document.getId());
                                    retiros.add(r);
                                }
                            }

                            db.collection("tramoretiro")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                    TramoRetiro r = document2.toObject(TramoRetiro.class);
                                                    r.setId(document2.getId());
                                                    tramos.add(r);
                                                }

                                                db.collection("direccion")
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                                        Direccion d = document2.toObject(Direccion.class);
                                                                        d.setId(document2.getId());
                                                                        direccions.add(d);
                                                                    }

                                                                    db.collection("generador")
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                                                            Generador d = document2.toObject(Generador.class);
                                                                                            d.setUid(document2.getId());
                                                                                            generadores.add(d);
                                                                                        }

                                                                                        for (Retiro p: retiros){
                                                                                            for (TramoRetiro r: tramos){
                                                                                                if (p.getIdTramo().compareTo(r.getId())==0){
                                                                                                    if (estado.compareTo("pendiente")==0){
                                                                                                        if (p.getEstado().compareTo("solicitado")==0) {
                                                                                                            ModeloVistaRetiro mvr = new ModeloVistaRetiro();
                                                                                                            mvr.setRetiro(p);
                                                                                                            mvr.setTramo(r);
                                                                                                            data.add(mvr);
                                                                                                        }
                                                                                                    }
                                                                                                    else if (estado.compareTo("aprobado")==0){
                                                                                                        if (p.getEstado().compareTo("aprobado")==0) {
                                                                                                            ModeloVistaRetiro mvr = new ModeloVistaRetiro();
                                                                                                            mvr.setRetiro(p);
                                                                                                            mvr.setTramo(r);
                                                                                                            data.add(mvr);
                                                                                                        }
                                                                                                    }
                                                                                                    else if (estado.compareTo("rechazado")==0){
                                                                                                        if (p.getEstado().compareTo("rechazado")==0) {
                                                                                                            ModeloVistaRetiro mvr = new ModeloVistaRetiro();
                                                                                                            mvr.setRetiro(p);
                                                                                                            mvr.setTramo(r);
                                                                                                            data.add(mvr);
                                                                                                        }
                                                                                                    }
                                                                                                    else if (estado.compareTo("finalizado")==0){
                                                                                                        if (p.getEstado().compareTo("finalizado")==0) {
                                                                                                            ModeloVistaRetiro mvr = new ModeloVistaRetiro();
                                                                                                            mvr.setRetiro(p);
                                                                                                            mvr.setTramo(r);
                                                                                                            data.add(mvr);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        for (ModeloVistaRetiro mv: data){
                                                                                            for (Direccion di: direccions){
                                                                                                if (mv.getRetiro().getIdDireccion().compareTo(di.getId())==0){
                                                                                                    mv.setDireccion(di);
                                                                                                }
                                                                                            }
                                                                                            for (Generador g: generadores){
                                                                                                if (mv.getRetiro().getUid().compareTo(g.getUid())==0){
                                                                                                    mv.setGenerador(g);
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        if (data.size()==0){
                                                                                            Bundle bundle = new Bundle();
                                                                                            bundle.putSerializable("mensaje", "retiro");

                                                                                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                                                                                            Fragment lp = new SinDocumentoFragment();
                                                                                            lp.setArguments(bundle);
                                                                                            fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
                                                                                            fragmentTransaction2.addToBackStack(null);
                                                                                            fragmentTransaction2.commit();
                                                                                        }
                                                                                        else {
                                                                                            Bundle bundle = new Bundle();
                                                                                            bundle.putSerializable("retiros", data);


                                                                                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                                                                            Fragment lp = new RetirosListFragment();
                                                                                            lp.setArguments(bundle);
                                                                                            fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
                                                                                            fragmentTransaction2.addToBackStack(null);
                                                                                            fragmentTransaction2.commit();
                                                                                        }


                                                                                    } else {

                                                                                    }
                                                                                }
                                                                            });



                                                                } else {

                                                                }
                                                            }
                                                        });


                                            } else {

                                            }
                                        }
                                    });

                        } else {

                        }
                    }
                });
    }

    public void cargarReportes(String estado){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lp = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Reporte> reportes = new ArrayList<>();
        ArrayList<Punto> puntos = new ArrayList<>();
        ArrayList<ModeloVistaReporte> data = new ArrayList<>();

        db.collection("punto")
                .whereEqualTo("pid", user.getUid())
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

                            db.collection("reporte")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                    Reporte r = document2.toObject(Reporte.class);
                                                    r.setIdReporte(document2.getId());
                                                    reportes.add(r);
                                                }

                                                for (Punto p: puntos){
                                                    for (Reporte r: reportes){
                                                        if (p.getId().compareTo(r.getIdPunto())==0){
                                                            if (r.getEstado().compareTo(estado)==0) {
                                                                ModeloVistaReporte mvr = new ModeloVistaReporte();
                                                                mvr.setPunto(p);
                                                                mvr.setReporte(r);
                                                                data.add(mvr);
                                                            }
                                                        }
                                                    }
                                                }

                                                if (data.size()==0){
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("mensaje", "reporte");

                                                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                                                    Fragment lp = new SinDocumentoFragment();
                                                    lp.setArguments(bundle);
                                                    fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
                                                    fragmentTransaction2.addToBackStack(null);
                                                    fragmentTransaction2.commit();
                                                }

                                                else {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("reportes", data);

                                                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                                    Fragment lp = new ReportesListFragment();
                                                    lp.setArguments(bundle);
                                                    fragmentTransaction2.replace(R.id.nav_host_fragment, lp);
                                                    fragmentTransaction2.addToBackStack(null);
                                                    fragmentTransaction2.commit();
                                                }


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