package com.example.pataconf.ui.optionreports;

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

import com.example.pataconf.ModeloVistaOpcionesProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.OptionsGestionProductosFragment;
import com.example.pataconf.ui.agregarpunto.AgregarPuntoFragment;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.puntos.PuntosListFragment;
import com.example.pataconf.ui.puntosmapa.PuntosMapaFragment;
import com.example.pataconf.ui.reportes.ReportesListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.ModeloOpcionesProducto;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaReporte;
import Modelo.Punto;
import Modelo.Reporte;

public class OptionsReportesListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsReportesListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ArrayList<Punto> data2 = new ArrayList<>();
    private FirebaseFirestore db;
    private ModeloVistaOpcionesProductoAdapter adapter;

    private ArrayList<ModeloOpcionesProducto> dataSet() {
        ArrayList<ModeloOpcionesProducto> data = new ArrayList<>();
        data.add(new ModeloOpcionesProducto("Reportes Pendientes",  R.drawable.reportarpendiente));
        data.add(new ModeloOpcionesProducto("Reportes Aprobados",  R.drawable.reportarok));
        data.add(new ModeloOpcionesProducto("Reportes Rechazados",  R.drawable.reportarok));
        //data.add(new ModeloOpcionesProducto("Ver Productos",  R.drawable.productos));
        return data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsReportesListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_optionsreports, container, false);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Menú Reportes");


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

        if (tv.getText().toString().compareTo("Reportes Pendientes")==0){
            cargarReportes("pendiente");
        }
        if (tv.getText().toString().compareTo("Reportes Rechazados")==0){
            cargarReportes("rechazado");
        }
        if (tv.getText().toString().compareTo("Reportes Aprobados")==0){
            cargarReportes("aprobado");
        }
    }

    public void cargarReportes(String estado){
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
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("reportes", data);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        Fragment lp = new ReportesListFragment();
                                        lp.setArguments(bundle);
                                        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                        fragmentTransaction.addToBackStack(null);
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