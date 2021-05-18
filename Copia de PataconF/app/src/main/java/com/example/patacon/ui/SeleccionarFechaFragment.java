package com.example.patacon.ui;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.vistareportepunto.ModeloVistaSeleccionarFechaRetiroAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaFechasRetiro;
import Modelo.ModeloVistaRecicladora;
import Modelo.TramoRetiro;

public class SeleccionarFechaFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaSeleccionarFechaRetiroAdapter adapter;
    private ArrayList<TramoRetiro> data = new ArrayList<>();
    private ArrayList<ModeloVistaFechasRetiro> dataf= new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private ModeloVistaFechasRetiro seleccionada;
    private Direccion direccion;
    private ModeloVistaDireccion mvd;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listseleccionarfecharetiro, container, false);


        adapter = new ModeloVistaSeleccionarFechaRetiroAdapter();
        adapter.setListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        data = (ArrayList<TramoRetiro>) getArguments().getSerializable("tramos");
        direccion = (Direccion) getArguments().getSerializable("direccion");

        System.out.println("total tramos encontrados: " + data.size());
        for (TramoRetiro t: data){
            System.out.println(t.toString());
        }

        ArrayList<String> fechasp = new ArrayList<>();

        for (TramoRetiro tr: data){
            if (!fechasp.contains(tr.getFecha()))
                fechasp.add(tr.getFecha());
        }


        for (String fecha: fechasp){
            ModeloVistaFechasRetiro mvfr = new ModeloVistaFechasRetiro();
            mvfr.setFecha(fecha);
            for (TramoRetiro trr: data){
                if (trr.getFecha().compareTo(fecha)==0)
                    mvfr.getTramos().add(trr);
            }
            dataf.add(mvfr);
        }

        Collections.sort(dataf);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Seleccionar Fecha");
        setHasOptionsMenu(true);

        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter.setdata(dataf);
        rvMusicas.setAdapter(adapter);


        return root;
    }




    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action).setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        /*switch (item.getItemId()){
            case R.id.action:
                if (item.getTitle().toString().compareTo("Listo")==0){
                    item.setTitle("Editar");
                    editar = false;
                    for (ModeloVistaDireccion m: data){
                        m.setEliminar(false);
                        m.setEliminar2(false);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    item.setTitle("Listo");
                    editar = true;
                    for (ModeloVistaDireccion m: data){
                        m.setEliminar(true);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        System.out.println("clicckckckckckc");

        Button b = (Button) view;
        String texto = b.getText().toString();


        for (ModeloVistaFechasRetiro mvd : this.dataf) {
            if (mvd.getFecha().compareTo(texto) == 0)
                seleccionada = mvd;
        }

        if (seleccionada == null) {
            System.out.println("FECHA NO ENCONTRADA");
            return;
        }

        else {


            Bundle bundle = new Bundle();
            bundle.putSerializable("tramos", seleccionada.getTramos());
            bundle.putSerializable("direccion", direccion);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new SeleccionarTramoFragment();
            lp.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }


    }
}