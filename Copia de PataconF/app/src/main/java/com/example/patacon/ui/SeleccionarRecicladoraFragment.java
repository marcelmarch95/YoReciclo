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
import android.widget.Toast;

import com.example.patacon.ModeloVistaSeleccionarDireccionAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Modelo.Cobertura;
import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaRecicladora;
import Modelo.ModeloVistaTramo;
import Modelo.Recicladora;
import Modelo.TramoRetiro;

public class SeleccionarRecicladoraFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaSeleccionarRecicladoraAdapter adapter;
    private ArrayList<ModeloVistaRecicladora> data = new ArrayList<>();
    private ArrayList<TramoRetiro> tramos = new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private ModeloVistaRecicladora d;
    private ModeloVistaDireccion mvd;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listseleccionarrecicladora, container, false);


        adapter = new ModeloVistaSeleccionarRecicladoraAdapter();
        adapter.setListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        data = (ArrayList<ModeloVistaRecicladora>) getArguments().getSerializable("recicladoras");


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Seleccionar Recicladora");
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


        for (ModeloVistaRecicladora mvd : this.data) {
            if (mvd.getRecicladora().getId().compareTo(texto) == 0)
                d = mvd;
        }

        if (d == null) {
            System.out.println("DIRECCION NO ENCONTRADA");
            return;
        }

        else {

            Toast.makeText(getContext(),"Seleccionada recicladora id " + texto,Toast.LENGTH_SHORT).show();


            Date date = new Date(System.currentTimeMillis());

            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            System.out.println("usuario : " + user.getUid());

            db.collection("tramoretiro")
                    .whereEqualTo("uid", texto)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    TramoRetiro p = document.toObject(TramoRetiro.class);
                                    p.setId(document.getId());

                                    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    Date d = null;
                                    try {
                                        d = sourceFormat.parse(p.getFecha());

                                        if (d.compareTo(date)>=0){
                                            if (p.isEstado()){
                                                tramos.add(p);
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("tramos", tramos);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new SeleccionarFechaFragment();
                                lp.setArguments(bundle);
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();

                            } else {

                            }
                        }
                    });
        }


    }
}