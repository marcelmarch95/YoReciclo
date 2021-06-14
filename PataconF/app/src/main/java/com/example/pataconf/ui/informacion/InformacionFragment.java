package com.example.pataconf.ui.informacion;

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

import com.example.pataconf.R;
import com.example.pataconf.ui.TramoRetiroListFragment;
import com.example.pataconf.ui.optionreports.OptionsReportesListFragment;
import com.example.pataconf.ui.optionretiros.OptionsRetirosListFragment;
import com.example.pataconf.ui.puntos.PuntosListFragment;
import com.example.pataconf.ui.reportes.ReportesListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Cobertura;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaTramo;
import Modelo.Punto;
import Modelo.TramoRetiro;

public class InformacionFragment extends Fragment implements View.OnClickListener {

    private Button finalizar;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private boolean isreporte = false;
    private String mensa;
    private ArrayList<ModeloVistaTramo> data2 = new ArrayList<>();
    private String dia, mes, ano;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_informacion, container, false);
        TextView texto = root.findViewById(R.id.text1);
        ImageView imageView = root.findViewById(R.id.imageView);
        finalizar = (Button) root.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);

        String msj =  getArguments().getString("mensaje");
        mensa = msj;

        if (this.mensa.compareTo("Tramo creado correctamente")==0){
            dia = getArguments().getString("dia");
            mes = getArguments().getString("mes");
            ano = getArguments().getString("ano");
        }

        boolean result =  getArguments().getBoolean("estado");

        if (msj.compareTo("Reporte aprobado")==0){
            System.out.println();
            isreporte = true;
        }
        if (msj.compareTo("Reporte rechazado")==0){
            isreporte = true;
        }

        if (!result){
            imageView.setImageResource(R.drawable.error);
        }
        texto.setText(msj);

        return root;
    }


    @Override
    public void onClick(View view) {

        if (this.mensa.compareTo("Retiro aprobado")==0 || this.mensa.compareTo("Retiro rechazado")==0 || this.mensa.compareTo("Retiro finalizado")==0){

            FragmentManager fragmentManager = getFragmentManager();
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Fragment lp = new OptionsRetirosListFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();

            return;
        }

        if (this.mensa.compareTo("Tramo creado correctamente")==0){

            FragmentManager fragmentManager = getFragmentManager();
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("tramoretiro")
                    .whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    TramoRetiro p = document.toObject(TramoRetiro.class);
                                    ModeloVistaTramo pu = new ModeloVistaTramo();
                                    pu.setTramoRetiro(p);
                                    data2.add(pu);
                                }

                                String f = String.valueOf(dia)+"/"+String.valueOf(Integer.valueOf(mes)+1)+"/"+String.valueOf(ano);
                                System.out.println("ff: " + f);

                                ArrayList<ModeloVistaTramo> fin = new ArrayList<>();
                                for (ModeloVistaTramo mv: data2){
                                    if (mv.getTramoRetiro().getFecha().compareTo(f)==0){
                                        fin.add(mv);
                                    }
                                }

                                ArrayList<Cobertura> coberturas = new ArrayList<>();


                                db.collection("cobertura")
                                        .whereEqualTo("uid", user.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        Cobertura cb = document2.toObject(Cobertura.class);
                                                        cb.setId(document2.getId());
                                                        coberturas.add(cb);
                                                    }

                                                    for (ModeloVistaTramo p: fin){
                                                        for (Cobertura r: coberturas){
                                                            if (p.getTramoRetiro().getIdCobertura().compareTo(r.getId())==0){
                                                                p.setCobertura(r);
                                                            }
                                                        }
                                                    }
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("dia", String.valueOf(dia));
                                                    bundle.putSerializable("mes", String.valueOf(mes));
                                                    bundle.putSerializable("ano", String.valueOf(ano));
                                                    bundle.putSerializable("tramos", fin);


                                                    Fragment lp = new TramoRetiroListFragment();
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
            return;
        }

        if (this.isreporte){
            System.out.println("if 1 ");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsReportesListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
            return;
        }

        if (view == this.finalizar && this.isreporte==false) {
            System.out.println("if 3");
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