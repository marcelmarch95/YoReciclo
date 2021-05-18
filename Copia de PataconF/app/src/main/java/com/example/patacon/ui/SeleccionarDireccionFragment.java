package com.example.patacon.ui;

import android.graphics.Color;
import android.location.Location;
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

import com.example.patacon.ModeloVistaDireccionAdapter;
import com.example.patacon.ModeloVistaSeleccionarDireccionAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.agregardireccion.AgregarDireccionFragment;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.editardireccion.EditarDireccionFragment;
import com.example.patacon.ui.puntos.PuntosListFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.StringTokenizer;

import Modelo.Cobertura;
import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaRecicladora;
import Modelo.Recicladora;

public class SeleccionarDireccionFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaSeleccionarDireccionAdapter adapter;
    private ArrayList<ModeloVistaDireccion> data = new ArrayList<>();
    private ArrayList<Cobertura> coberturas = new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private ModeloVistaDireccion d;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listseleccionardireccion, container, false);


        adapter = new ModeloVistaSeleccionarDireccionAdapter();
        adapter.setListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        data = (ArrayList<ModeloVistaDireccion>) getArguments().getSerializable("direcciones");

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Seleccionar Dirección");
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

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lp = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
        fragmentTransaction.commit();

        Button b = (Button) view;
        String texto = b.getText().toString();


        for (ModeloVistaDireccion mvd : this.data) {
            if (mvd.getDireccion().getId().compareTo(texto) == 0)
                d = mvd;
        }

        if (d == null) {
            System.out.println("DIRECCION NO ENCONTRADA");
            return;
        }

        else {


            db.collection("cobertura")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cobertura co = document.toObject(Cobertura.class);
                            co.setId(document.getId());
                            coberturas.add(co);
                        }

                        ArrayList<Cobertura> compatibles = new ArrayList<>();

                        for (Cobertura c: coberturas){
                            Double lat = Double.valueOf(c.getLat());
                            Double lng = Double.valueOf(c.getLng());
                            Integer radio = Integer.valueOf(c.getRadio());

                            float[] distance = new float[2];

                            Location.distanceBetween( Double.valueOf(d.getDireccion().getLat()),Double.valueOf(d.getDireccion().getLng()),
                                    lat, lng, distance);

                            if( distance[0] > radio  ){
                                System.out.println("OUTSIDE");
                            } else {
                                compatibles.add(c);
                                System.out.println("INSIDE");
                            }
                        }


                        ArrayList<String> cobcom = new ArrayList<>();

                        for (Cobertura c: compatibles){
                            if (!cobcom.contains(c.getUid()))
                                cobcom.add(c.getUid());
                        }

                        for (String s: cobcom){
                            System.out.println("ENCONTRADA EMPRESA CON ID " + s + " CON COBERTURA");
                        }

                        if (cobcom.size()==0){
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            Fragment lp = new SinCoberturaFragment();
                            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                            fragmentTransaction.commit();
                            return;
                        }

                        db.collection("recicladora")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<ModeloVistaRecicladora> compa = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Recicladora re = document.toObject(Recicladora.class);
                                        re.setId(document.getId());
                                        ModeloVistaRecicladora mvr = new ModeloVistaRecicladora();
                                        mvr.setRecicladora(re);
                                        compa.add(mvr);
                                    }

                                    ArrayList<ModeloVistaRecicladora> finales = new ArrayList<>();

                                    for (String id: cobcom){
                                        for (ModeloVistaRecicladora mv: compa){
                                            if (mv.getRecicladora().getId().compareTo(id)==0)
                                                if (!finales.contains(mv))
                                                    finales.add(mv);
                                        }
                                    }

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("recicladoras", finales);
                                    bundle.putSerializable("direccion",d.getDireccion());

                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    Fragment lp = new SeleccionarRecicladoraFragment();
                                    lp.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.nav_host_fragment, lp);
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
}