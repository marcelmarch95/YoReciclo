package com.example.patacon.ui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.example.patacon.ModeloVistaDashboardAdapter;
import com.example.patacon.R;
import com.example.patacon.ui.escanearpunto.EscanearPuntoFragment;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Generador;
import Modelo.ModeloVistaDashboard;
import Modelo.Punto;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaDashboardAdapter adapter;
    private FirebaseAuth mAuth;
    private Generador generador;
    private TextView name;
    private Button mapa;
    private FirebaseFirestore db;
    private Button agendar;
    private Button reportar;
    private ArrayList<Punto> data2 = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        name = (TextView) root.findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mapa = (Button) root.findViewById(R.id.mapa);
        agendar = (Button) root.findViewById(R.id.agendar);
        reportar = (Button) root.findViewById(R.id.reportar);

        mapa.setOnClickListener(this);
        agendar.setOnClickListener(this);
        reportar.setOnClickListener(this);

        //Generador prueba = getArguments().getSerializable("generador");
        //System.out.println("Prueba: " + prueba.toString());

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("generador").document(currentUser.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        generador = (Generador) document.toObject(Generador.class);
                        name.setText(generador.getNombre());
                    } else {
                        System.out.println("error1");
                    }
                } else {
                    System.out.println("error2");
                }
            }
        });

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        getActivity().setTitle("f title");



        /*rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaDashboardAdapter(dataSet());
        rvMusicas.setAdapter(adapter);*/
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view==this.mapa){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Punto p = document.toObject(Punto.class);
                                    p.setId(document.getId());
                                    data2.add(p);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("puntos", data2);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new PuntosMapaFragment();
                                lp.setArguments(bundle);
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();
                            } else {
                                System.out.println("Error al conectarse ");
                            }
                        }
                    });

        }

        if (view==this.reportar){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new EscanearPuntoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
    }
}