package com.example.patacon.ui.puntos;

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

import com.example.patacon.ModeloVistaPuntoAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.editarpunto.EditarPuntoFragment;
import com.example.patacon.ui.verpunto.VerPuntoFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Modelo.ModeloVistaPunto;


public class PuntosListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaPuntoAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaPunto> puntos;
    private Button volver;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listpuntos, container, false);

        padre = (PerfilComerciante) getActivity();

        ArrayList<ModeloVistaPunto> data = (ArrayList<ModeloVistaPunto>) getArguments().getSerializable("puntos");
        this.puntos = data;

        padre.getSupportActionBar().setTitle("Listado Puntos");

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaPuntoAdapter(data, this);
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

            List<String> salida = new ArrayList<>();

            StringTokenizer arr = new StringTokenizer(bt.getText().toString(), "\n");
            while(arr.hasMoreTokens()){
                salida.add(arr.nextToken());
            }

            String tipo = salida.get(1);
            String idpunto = salida.get(0);

            System.out.println("tipo: " + tipo);
            System.out.println("idpunto: " + idpunto);

            ModeloVistaPunto puntoe = new ModeloVistaPunto();

            if (tipo.compareTo("E")==0){

                for (ModeloVistaPunto p: puntos){
                    if (p.getId().compareTo(idpunto)==0){
                        puntoe = p;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("punto", puntoe);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new VerPuntoFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }

            if (tipo.compareTo("ED")==0){

                for (ModeloVistaPunto p: puntos){
                    if (p.getId().compareTo(idpunto)==0){
                        puntoe = p;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("punto", puntoe);

                System.out.println("Punto antes de entrar a editar ." + puntoe.getPid());

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new EditarPuntoFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }

        /*else {
            CardView cv = (CardView) view;
            TextView tv = (TextView) view.findViewById(R.id.accion);
            if (tv.getText().toString().compareTo("Ver Puntos")==0){

            }
        }*/





    }



}