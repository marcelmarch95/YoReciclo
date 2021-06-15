package com.example.patacon.ui.optionretiros;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.patacon.ModeloVistaOpcionesProductoAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.cargando.SinDocumentoFragment;
import com.example.patacon.ui.retiroslist.RetirosListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.Generador;
import Modelo.ModeloOpcionesProducto;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaRetiro;
import Modelo.Punto;
import Modelo.Retiro;
import Modelo.TramoRetiro;

public class OptionsRetirosListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ArrayList<Punto> data2 = new ArrayList<>();
    private FirebaseFirestore db;
    private ModeloVistaOpcionesProductoAdapter adapter;

    private ArrayList<ModeloOpcionesProducto> dataSet() {
        ArrayList<ModeloOpcionesProducto> data = new ArrayList<>();
        data.add(new ModeloOpcionesProducto("Retiros Pendientes",  R.drawable.reportarpendiente));
        data.add(new ModeloOpcionesProducto("Retiros Aprobados",  R.drawable.reportarok));
        data.add(new ModeloOpcionesProducto("Retiros Rechazados",  R.drawable.reportarok));
        data.add(new ModeloOpcionesProducto("Retiros Finalizados",  R.drawable.reportarok));
        //data.add(new ModeloOpcionesProducto("Ver Productos",  R.drawable.productos));
        return data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_optionsretiros, container, false);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Menú Retiros");


        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaOpcionesProductoAdapter(dataSet(), this);
        rvMusicas.setAdapter(adapter);
        return root;
    }

    @Override
    public void onClick(View view) {
        CardView cv = (CardView) view;
        TextView tv = (TextView) view.findViewById(R.id.accion);

        if (tv.getText().toString().compareTo("Retiros Pendientes")==0){
            cargarRetiros("pendiente");
        }
        if (tv.getText().toString().compareTo("Retiros Rechazados")==0){
            cargarRetiros("rechazado");
        }
        if (tv.getText().toString().compareTo("Retiros Aprobados")==0){
            cargarRetiros("aprobado");
        }
        if (tv.getText().toString().compareTo("Retiros Finalizados")==0){
            cargarRetiros("finalizado");
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
                        System.out.println("retiro encontrado: " + r.toString());
                        if (r.getUid().compareTo(user.getUid())==0){
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
}