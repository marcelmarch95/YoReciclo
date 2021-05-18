package com.example.patacon.ui;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.vistareportepunto.ModeloVistaSeleccionarFechaRetiroAdapter;
import com.example.patacon.ui.vistareportepunto.ModeloVistaSeleccionarTramoRetiroAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaFechasRetiro;
import Modelo.TramoRetiro;

public class SeleccionarTramoFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaSeleccionarTramoRetiroAdapter adapter;
    private ArrayList<TramoRetiro> data = new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private TramoRetiro seleccionada;
    private Direccion direccion;
    private ModeloVistaDireccion mvd;
    private TextView fecha;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listseleccionartramoretiro, container, false);


        adapter = new ModeloVistaSeleccionarTramoRetiroAdapter();
        adapter.setListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        fecha = (TextView) root.findViewById(R.id.fecha);


        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        data = (ArrayList<TramoRetiro>) getArguments().getSerializable("tramos");
        direccion = (Direccion) getArguments().getSerializable("direccion");

        System.out.println("total tramos encontrados: " + data.size());
        for (TramoRetiro t: data){
            System.out.println(t.toString());
        }
        StringTokenizer stringTokenizer = new StringTokenizer(data.get(0).getFecha(),"/");

        ArrayList<String> partes = new ArrayList<>();
        while(stringTokenizer.hasMoreTokens()){
            partes.add(stringTokenizer.nextToken());
        }

        String dia = partes.get(0);
        String mes = partes.get(1);
        String ano = partes.get(2);

        if (dia.length()<2)
            dia = "0"+dia;
        String[] meses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        Integer mess = Integer.valueOf(mes);
        String me = meses[mess-1];

        fecha.setText("Día " + dia + " de " + me + " del " + ano);


        Collections.sort(data);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Seleccionar Tramo Horario");
        setHasOptionsMenu(true);

        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter.setdata(data);
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

        System.out.println("id buscando : " + texto);


        for (TramoRetiro mvd : this.data) {
            if (mvd.getId().compareTo(texto) == 0)
                seleccionada = mvd;
        }

        if (seleccionada == null) {
            System.out.println("FECHA NO ENCONTRADA");
            return;
        }

        else {

            Bundle bundle = new Bundle();
            bundle.putSerializable("tramo", seleccionada);
            bundle.putSerializable("direccion", direccion);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new DeclaracionFragment();
            lp.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }


    }
}