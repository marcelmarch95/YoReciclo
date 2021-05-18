package com.example.patacon.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.patacon.ModeloVistaReporteAdapter;
import com.example.patacon.ModeloVistaRetiroAdapter;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.vistareportepunto.VistaReportePuntoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import Modelo.Direccion;
import Modelo.ModeloVistaReporte;
import Modelo.ModeloVistaRetiro;
import Modelo.Retiro;
import Modelo.TramoRetiro;


public class RetirosListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaRetiroAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaRetiro> retiros = new ArrayList<>();
    private Button volver;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listretiros, container, false);

        padre = (PerfilComerciante) getActivity();
        padre.getSupportActionBar().setTitle("Retiros");

        adapter = new ModeloVistaRetiroAdapter();
        adapter.setListener(this);

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);

        boolean tiene = false;
        try {
            tiene = getArguments().getBoolean("tiene");
        }
        catch (Exception e){
            tiene = false;
        }


        if (!tiene){

            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("retiro")
            .whereEqualTo("uid", user.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Retiro r = document.toObject(Retiro.class);
                            r.setId(document.getId());
                            ModeloVistaRetiro pu = new ModeloVistaRetiro();
                            pu.setRetiro(r);
                            retiros.add(pu);
                        }

                        ArrayList<Direccion> direcciones = new ArrayList<>();

                        db.collection("direccion")
                                .whereEqualTo("pid", user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Direccion r = document.toObject(Direccion.class);
                                                r.setId(document.getId());
                                                direcciones.add(r);
                                            }

                                            for (ModeloVistaRetiro r: retiros){
                                                for (Direccion d: direcciones){
                                                    if (r.getRetiro().getIdDireccion().compareTo(d.getId())==0)
                                                        r.setDireccion(d);
                                                }
                                            }


                                            db.collection("tramoretiro")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    TramoRetiro tr = (TramoRetiro) document.toObject(TramoRetiro.class);
                                                                    tr.setId(document.getId());
                                                                    for (ModeloVistaRetiro mr: retiros){
                                                                        if (mr.getRetiro().getIdTramo().compareTo(tr.getId())==0)
                                                                            mr.setTramo(tr);
                                                                    }
                                                                }

                                                                glm = new GridLayoutManager(getActivity(), 1);
                                                                rvMusicas.setLayoutManager(glm);
                                                                adapter.setData(retiros);
                                                                rvMusicas.setAdapter(adapter);

                                                                setHasOptionsMenu(true);



                                                            } else {

                                                            }
                                                        }
                                                    });


                                        } else {

                                        }
                                    }
                                });

                    } else {

                    }
                }
            });
        }

        else {
            ArrayList<ModeloVistaRetiro> data = (ArrayList<ModeloVistaRetiro>) getArguments().getSerializable("retiros");
            this.retiros = data;
            setHasOptionsMenu(true);

            rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
            glm = new GridLayoutManager(getActivity(), 1);
            rvMusicas.setLayoutManager(glm);
            adapter = new ModeloVistaRetiroAdapter(retiros, this);
            rvMusicas.setAdapter(adapter);

        }



        return root;
    }

    @Override
    public void onClick(View view) {

        if (view == this.volver) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        } else if (view == this.back) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        } else {
            Button bt = (Button) view;
            System.out.println("Click en el retiro con id: " + bt.getText());

            ModeloVistaRetiro seleccionado = null;

            for (ModeloVistaRetiro m : this.retiros) {
                if (m.getRetiro().getId().compareTo((String) bt.getText()) == 0)
                    seleccionado = m;
            }

            if (seleccionado == null)
                return;


            Bitmap im = getBitmapFromURL(seleccionado.getRetiro().getFoto1());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            im.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Bundle bundle = new Bundle();
            bundle.putSerializable("retiro", seleccionado);
            bundle.putByteArray("img", byteArray);
            Fragment lp = new VeretiroFragment();
            lp.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();


        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }




}