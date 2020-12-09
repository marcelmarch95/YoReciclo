package com.example.patacon.ui.reportes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.patacon.ModeloVistaReporteAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.optionproducts.OptionsPuntosListFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Modelo.ModeloVistaReporte;


public class ReportesListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaReporteAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaReporte> reportes;
    private Button volver;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listreport, container, false);

        padre = (PerfilComerciante) getActivity();
        //padre.getSupportActionBar().hide();
        padre.getSupportActionBar().setTitle("Listado Reportes");

        ArrayList<ModeloVistaReporte> data = (ArrayList<ModeloVistaReporte>) getArguments().getSerializable("reportes");
        this.reportes = data;

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaReporteAdapter(data, this);
        rvMusicas.setAdapter(adapter);

        this.volver = root.findViewById(R.id.volver);
        this.volver.setOnClickListener(this);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onClick(View view) {

        if (view==this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else if (view == this.back){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else {
            Button bt = (Button) view;
            System.out.println("Click en el punto con id: " + bt.getText());


        }

        /*else {
            CardView cv = (CardView) view;
            TextView tv = (TextView) view.findViewById(R.id.accion);
            if (tv.getText().toString().compareTo("Ver Puntos")==0){

            }
        }*/





    }



}