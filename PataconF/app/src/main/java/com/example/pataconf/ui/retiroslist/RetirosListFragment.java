package com.example.pataconf.ui.retiroslist;

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

import com.example.pataconf.ModeloVistaReporteAdapter;
import com.example.pataconf.ModeloVistaRetiroAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.optionpuntos.OptionsPuntosListFragment;
import com.example.pataconf.ui.optionpuntos.OptionsPuntosListViewModel;
import com.example.pataconf.ui.optionreports.OptionsReportesListFragment;
import com.example.pataconf.ui.optionretiros.OptionsRetirosListFragment;
import com.example.pataconf.ui.vistareportepunto.VistaReportePuntoFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Modelo.ModeloVistaReporte;
import Modelo.ModeloVistaRetiro;


public class RetirosListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaRetiroAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaRetiro> reportes;
    private Button volver;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listretiro, container, false);

        padre = (PerfilComerciante) getActivity();
        //padre.getSupportActionBar().hide();
        padre.getSupportActionBar().setTitle("Listado Retiros");

        ArrayList<ModeloVistaRetiro> data = (ArrayList<ModeloVistaRetiro>) getArguments().getSerializable("retiros");
        this.reportes = data;

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaRetiroAdapter(data, this);
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

            Fragment lp = new OptionsRetirosListFragment();
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
            System.out.println("Click en el reporte con id: " + bt.getText());

            for (ModeloVistaRetiro mvr: this.reportes) {
                if (mvr.getRetiro().getId().compareTo(bt.getText().toString())==0){
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mvr", mvr);

                    Fragment lp = new VistaReportePuntoFragment();
                    lp.setArguments(bundle);
                    fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                    fragmentTransaction.commit();
                }
            }
        }





    }



}