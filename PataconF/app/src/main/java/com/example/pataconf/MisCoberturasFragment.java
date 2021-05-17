package com.example.pataconf;

import android.content.Intent;
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

import com.example.pataconf.ui.agregarcobertura.AgregarCoberturaFragment;
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
import Modelo.ModeloVistaCobertura;


public class MisCoberturasFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaCoberturaAdapter adapter;
    private ArrayList<ModeloVistaCobertura> data = new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private ModeloVistaCobertura d;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listcobertura, container, false);


        adapter = new ModeloVistaCoberturaAdapter();
        adapter.setListener(this);
        add = root.findViewById(R.id.add);
        add.setOnClickListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("cobertura")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cobertura d = document.toObject(Cobertura.class);
                                ModeloVistaCobertura mvd = new ModeloVistaCobertura();
                                d.setId(document.getId());
                                mvd.setCobertura(d);
                                data.add(mvd);
                            }
                            ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mis Coberturas");
                            setHasOptionsMenu(true);


                            glm = new GridLayoutManager(getActivity(), 1);
                            rvMusicas.setLayoutManager(glm);
                            adapter.setdata(data);
                            rvMusicas.setAdapter(adapter);


                        } else {

                        }
                    }
                });



        return root;
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
        if (view==this.add){
            System.out.println("agregar dire");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new AgregarCoberturaFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
        else {
            System.out.println("clicckckckckckc");

            Button b = (Button) view;
            String texto = b.getText().toString();

            StringTokenizer st = new StringTokenizer(texto, ";");
            ArrayList<String> sts = new ArrayList<>();

            System.out.println("texto: " + texto);

            while (st.hasMoreTokens()) {
                sts.add(st.nextToken());
            }
            String accion = sts.get(0);
            String id = sts.get(1);
            d = null;

            for (ModeloVistaCobertura mvd: this.data){
                if (mvd.getCobertura().getId().compareTo(id)==0)
                    d = mvd;
            }

            if (d==null){
                System.out.println("COBERTURA NO ENCONTRADA");
                return;
            }

            if (accion.compareTo("EL") == 0) {
                System.out.println("ELIMINAR");

                db.collection("cobertura").document(d.getCobertura().getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                data.remove(d);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
            if (accion.compareTo("ED") == 0) {
                Intent intent = new Intent(getActivity(), SelectorDireccionMapaCobertura.class);
                intent.putExtra("editar", true);
                intent.putExtra("dire",d.getCobertura());
                startActivity(intent);
            }

        }
    }



}