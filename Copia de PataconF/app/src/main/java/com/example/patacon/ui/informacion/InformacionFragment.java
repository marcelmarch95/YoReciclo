package com.example.patacon.ui.informacion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patacon.ui.RetirosListFragment;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.home.HomeFragment;
import com.example.patacon.ui.puntos.PuntosListFragment;
import com.example.patacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaRetiro;
import Modelo.Punto;
import Modelo.Retiro;
import Modelo.TramoRetiro;

public class InformacionFragment extends Fragment implements View.OnClickListener {

    private Button finalizar;
    private FirebaseFirestore db;
    private String mensaje;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ArrayList<ModeloVistaRetiro> retiros = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_informacion, container, false);
        TextView texto = root.findViewById(R.id.text1);
        ImageView imageView = root.findViewById(R.id.imageView);
        finalizar = (Button) root.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);



        String msj =  getArguments().getString("mensaje");
        boolean result =  getArguments().getBoolean("estado");

        this.mensaje = msj;

        if (!result){
            imageView.setImageResource(R.drawable.error);
        }

        texto.setText(msj);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == this.finalizar) {

            if (this.mensaje.compareTo("Direccion editada correctamente")==0){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                HomeFragment lp = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
                return;
            }

            else if (this.mensaje.compareTo("Retiro creado correctamente")==0 || this.mensaje.compareTo("Retiro actualizado correctamente")==0){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new CargandoFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();

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

                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putBoolean("tiene",true);
                                                                    bundle.putSerializable("retiros", retiros);


                                                                    Fragment lp = new RetirosListFragment();
                                                                    lp.setArguments(bundle);
                                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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

                        } else {

                        }
                    }
                });
            }

            else if (this.mensaje.compareTo("Reporte enviado correctamente")==0){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                HomeFragment lp = new HomeFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }

            else {
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                db.collection("punto")
                .whereEqualTo("pid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Punto p = document.toObject(Punto.class);
                                ModeloVistaPunto pu = new ModeloVistaPunto();
                                pu.setDireccion(p.getDireccion());
                                pu.setLat(p.getLat());
                                pu.setLng(p.getLng());
                                pu.setSector(p.getSector());
                                pu.setRecinto(p.getRecinto());
                                pu.setArea(p.getArea());
                                pu.setFoto(p.getFoto());
                                pu.setIslatas(p.isIslatas());
                                pu.setIsplastico(p.isIsplastico());
                                pu.setIsvidrio(p.isIsvidrio());
                                pu.setId(document.getId());
                                pu.setObservacion(p.getObservacion());
                                data.add(pu);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("puntos", data);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            Fragment lp = new PuntosListFragment();
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
}