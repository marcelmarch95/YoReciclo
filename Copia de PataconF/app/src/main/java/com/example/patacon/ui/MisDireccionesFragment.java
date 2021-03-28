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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.Generador;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaReporte;

public class MisDireccionesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaDireccionAdapter adapter;
    private ArrayList<ModeloVistaDireccion> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listdirecciones, container, false);

        Direccion d = new Direccion();
        d.setCalle("Pje 3");
        d.setNumero("105");
        ModeloVistaDireccion mvd = new ModeloVistaDireccion();
        mvd.setDireccion(d);

        Direccion d2 = new Direccion();
        d2.setCalle("Avenida Camilo Henriquez");
        d2.setNumero("105");
        ModeloVistaDireccion mvd2 = new ModeloVistaDireccion();
        mvd2.setDireccion(d2);

        data = new ArrayList<>();
        data.add(mvd);
        data.add(mvd2);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mis Direcciones");
        setHasOptionsMenu(true);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaDireccionAdapter(data, this);
        rvMusicas.setAdapter(adapter);

        return root;
    }




    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action).setVisible(true);
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        switch (item.getItemId()){
            case R.id.action:
                if (item.getTitle().toString().compareTo("Listo")==0){
                    item.setTitle("Editar");
                    for (ModeloVistaDireccion m: data){
                        m.setEliminar(false);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
                else {
                    item.setTitle("Listo");
                    for (ModeloVistaDireccion m: data){
                        m.setEliminar(true);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

    }



}