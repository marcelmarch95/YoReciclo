package com.example.pataconf.ui.optionproducts;

import android.os.Bundle;
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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.example.pataconf.ModeloVistaOpcionesProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.OptionsGestionProductosFragment;
import com.example.pataconf.ui.agregarpunto.AgregarPuntoFragment;
import com.example.pataconf.ui.puntos.PuntosListFragment;
import com.example.pataconf.ui.puntosmapa.PuntosMapaFragment;
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
import Modelo.Punto;

public class OptionsPuntosListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ArrayList<Punto> data2 = new ArrayList<>();
    private FirebaseFirestore db;
    private ModeloVistaOpcionesProductoAdapter adapter;

    private ArrayList<ModeloOpcionesProducto> dataSet() {
        ArrayList<ModeloOpcionesProducto> data = new ArrayList<>();
        data.add(new ModeloOpcionesProducto("Agregar Punto Limpio",  R.drawable.addpunto));
        data.add(new ModeloOpcionesProducto("Gestionar Puntos",  R.drawable.listp));
        data.add(new ModeloOpcionesProducto("Ver Puntos en Mapa",  R.drawable.puntosmapa));
        data.add(new ModeloOpcionesProducto("Ver últimos Reportes",  R.drawable.notific));
        //data.add(new ModeloOpcionesProducto("Ver Productos",  R.drawable.productos));
        return data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_optionsproduct, container, false);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Menú Puntos Limpios");


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

        if (tv.getText().toString().compareTo("Ver Puntos en Mapa")==0){


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

        if (tv.getText().toString().compareTo("Agregar Punto Limpio")==0){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new AgregarPuntoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        if (tv.getText().toString().compareTo("Gestionar Puntos")==0){
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
                            pu.setPid(p.getPid());
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

        if (tv.getText().toString().compareTo("Gestionar Productos")==0){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsGestionProductosFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
    }
}