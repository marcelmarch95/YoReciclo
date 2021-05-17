package com.example.patacon.ui;

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

import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.home.HomeFragment;
import com.example.patacon.ui.puntos.PuntosListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class SinCoberturaFragment extends Fragment implements View.OnClickListener {

    private Button finalizar;
    private FirebaseFirestore db;
    private String mensaje;
    private ArrayList<ModeloVistaDireccion> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sincobertura, container, false);
        TextView texto = root.findViewById(R.id.text1);
        ImageView imageView = root.findViewById(R.id.imageView);
        finalizar = (Button) root.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);




        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == this.finalizar) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new CargandoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();

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

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("direcciones", data);

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment lp = new SeleccionarDireccionFragment();
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