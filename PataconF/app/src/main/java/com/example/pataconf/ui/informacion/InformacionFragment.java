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
import com.example.pataconf.ui.products.ProductListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.ModeloVistaProducto;
import Modelo.Producto;

public class InformacionFragment extends Fragment implements View.OnClickListener {

    private Button finalizar;
    private boolean productos;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaProducto> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_informacion, container, false);
        TextView texto = root.findViewById(R.id.text1);
        ImageView imageView = root.findViewById(R.id.imageView);
        finalizar = (Button) root.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);

        String msj =  getArguments().getString("mensaje");
        productos = getArguments().getBoolean("productos");
        boolean result =  getArguments().getBoolean("estado");

        if (!result){
            imageView.setImageResource(R.drawable.error);
        }

        texto.setText(msj);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == this.finalizar && productos){

            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("producto")
                    .whereEqualTo("comerciante", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Producto p = document.toObject(Producto.class);
                                    ModeloVistaProducto mo = new ModeloVistaProducto(p.getNombre(),p.getCategoria(),p.getFoto(),p.getPrecio());
                                    data.add(mo);
                                }

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("productos", data);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new ProductListFragment();
                                lp.setArguments(bundle);
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();
                            } else {

                            }
                        }
                    });
        }
        else {

        }
    }
}