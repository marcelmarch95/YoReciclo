package com.example.patacon.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.MainActivity;
import com.example.patacon.ModeloVistaDireccionAdapter;
import com.example.patacon.ModeloVistaReporteAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.agregardireccion.AgregarDireccionFragment;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.editardireccion.EditarDireccionFragment;
import com.example.patacon.ui.verpunto.VerPuntoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Modelo.Direccion;
import Modelo.Generador;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaReporte;

public class MisDireccionesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaDireccionAdapter adapter;
    private ArrayList<ModeloVistaDireccion> data = new ArrayList<>();
    private boolean editar = false;
    private Button add;
    private ModeloVistaDireccion d;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listdirecciones, container, false);


        adapter = new ModeloVistaDireccionAdapter();
        adapter.setListener(this);
        add = root.findViewById(R.id.add);
        add.setOnClickListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("direccion")
                .whereEqualTo("pid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Direccion d = document.toObject(Direccion.class);
                                ModeloVistaDireccion mvd = new ModeloVistaDireccion();
                                d.setId(document.getId());
                                mvd.setDireccion(d);
                                data.add(mvd);
                            }
                            ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mis Direcciones");
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
        if (view==this.add){
            System.out.println("agregar dire");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new AgregarDireccionFragment();
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

            for (ModeloVistaDireccion mvd: this.data){
                if (mvd.getDireccion().getId().compareTo(id)==0)
                    d = mvd;
            }

            if (d==null){
                System.out.println("DIRECCION NO ENCONTRADA");
                return;
            }

            if (accion.compareTo("EL") == 0) {
                System.out.println("ELIMINAR");

                db.collection("direccion").document(d.getDireccion().getId())
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
                System.out.println("EDITAR");
                Bundle bundle = new Bundle();
                bundle.putSerializable("direccion", d.getDireccion());

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new EditarDireccionFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }

        }
    }



}