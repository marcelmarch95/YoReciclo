package com.example.pataconf.ui.products;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.ModeloVistaProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.optionproducts.OptionsProductListFragment;
import com.example.pataconf.ui.optionproducts.OptionsProductListViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Modelo.ModeloVistaProducto;

public class ProductListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsProductListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaProductoAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsProductListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listproduct, container, false);

        padre = (PerfilComerciante) getActivity();
        //padre.getSupportActionBar().hide();
        padre.getSupportActionBar().setTitle("Listado Productos");

        this.back = root.findViewById(R.id.back);
        this.back.setOnClickListener(this);

        categoria = (Spinner) root.findViewById(R.id.propiedad);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.categoriasproductos, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter2);

        ArrayList<ModeloVistaProducto> data = (ArrayList<ModeloVistaProducto>) getArguments().getSerializable("productos");

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaProductoAdapter(data, this);
        rvMusicas.setAdapter(adapter);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onClick(View view) {

        if (view == this.back){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsProductListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else {
            CardView cv = (CardView) view;
            TextView tv = (TextView) view.findViewById(R.id.accion);
            if (tv.getText().toString().compareTo("Ver Productos")==0){

            }
        }





    }



}