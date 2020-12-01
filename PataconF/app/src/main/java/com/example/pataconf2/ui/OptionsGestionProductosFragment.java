package com.example.pataconf.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.example.pataconf.ModeloVistaOpcionesProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.agregarpunto.AgregarPuntoFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.pataconf.ui.puntos.PuntosListFragment;
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
import Modelo.Producto;

public class OptionsGestionProductosFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private Button back;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private FirebaseFirestore db;
    private ModeloVistaOpcionesProductoAdapter adapter;

    private ArrayList<ModeloOpcionesProducto> dataSet() {
        ArrayList<ModeloOpcionesProducto> data = new ArrayList<>();
        data.add(new ModeloOpcionesProducto("Agregar Producto",  R.drawable.addpro));
        data.add(new ModeloOpcionesProducto("Agregar Combo",  R.drawable.combo));
        data.add(new ModeloOpcionesProducto("Gestionar Promociones",  R.drawable.descuento2));
        data.add(new ModeloOpcionesProducto("Ver Productos",  R.drawable.productos));
        return data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_optionsgestionproductos, container, false);
        this.back = root.findViewById(R.id.back);
        this.back.setOnClickListener(this);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Gestión de Productos");


        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaOpcionesProductoAdapter(dataSet(), this);
        rvMusicas.setAdapter(adapter);
        return root;
    }

    @Override
    public void onClick(View view) {

        if (view == this.back){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else {
            CardView cv = (CardView) view;
            TextView tv = (TextView) view.findViewById(R.id.accion);


            if (tv.getText().toString().compareTo("Ver Productos")==0){
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                db.collection("producto")
                        .whereEqualTo("comerciante", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Producto p = document.toObject(Producto.class);
                                        ModeloVistaPunto mo = new ModeloVistaPunto();
                                        //ModeloVistaPunto mo = new ModeloVistaPunto(p.getNombre(),p.getCategoria(),p.getFoto(),p.getPrecio());
                                        data.add(mo);
                                    }

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("productos", data);

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

            if (tv.getText().toString().compareTo("Agregar Producto")==0){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new AgregarPuntoFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }

    }
}