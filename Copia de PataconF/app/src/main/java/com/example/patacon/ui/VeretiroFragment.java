package com.example.patacon.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.retiroslist.RetirosListFragment;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import Modelo.Direccion;
import Modelo.ModeloVistaRetiro;
import Modelo.TramoRetiro;

public class VeretiroFragment extends Fragment implements View.OnClickListener {


    private TramoRetiro tramoseleccionado;
    private TextView fecha;
    private TextView tramo;

    private TextView ticarton;
    private TextView tilata;
    private TextView tividrio;
    private TextView tiplastico;

    private TextView tdireccion;
    private TextView tciudad;

    private ImageView foto;
    private Button btncancelar;
    private Button btnresolver;

    private FirebaseFirestore db;

    private int totplastico = 0;
    private int totlatas = 0;
    private int totvidrio = 0;
    private int totcarton = 0;
    private Button botonvolver;
    private Button botonsiguiente;
    private Direccion direccion;
    private View root;
    private Bitmap img;
    private String comentarios;
    private ModeloVistaRetiro retiro;
    private TextView estado;
    private LinearLayout layoup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_veretiro, container, false);

        btncancelar = root.findViewById(R.id.btncancelar);
        btnresolver = root.findViewById(R.id.btnresolver);

        btnresolver.setOnClickListener(this);
        btnresolver.setVisibility(View.INVISIBLE);
        btncancelar.setOnClickListener(this);

        retiro = (ModeloVistaRetiro) getArguments().getSerializable("retiro");
        tramoseleccionado = retiro.getTramo();
        direccion = retiro.getDireccion();
        totlatas = Integer.valueOf(retiro.getRetiro().getTotlata());
        totvidrio = Integer.valueOf(retiro.getRetiro().getTotvidrio());
        totcarton = Integer.valueOf(retiro.getRetiro().getTotcarton());
        totplastico = Integer.valueOf(retiro.getRetiro().getTotplastico());
        comentarios = retiro.getRetiro().getComentarios();

        layoup = (LinearLayout) root.findViewById(R.id.layoup);

        if (retiro.getRetiro().getEstado().compareTo("reparo")==0){
            btnresolver.setVisibility(View.VISIBLE);
        }
        if (retiro.getRetiro().getEstado().compareTo("cancelado")==0){
            btncancelar.setVisibility(View.INVISIBLE);
        }
        else {
            if (retiro.getRetiro().getEstado().compareTo("reparo")!=0) {
                layoup.removeAllViews();
                layoup.addView(btncancelar);
                layoup.requestLayout();
            }
        }

        foto = (ImageView) root.findViewById(R.id.imageView10);

        byte[] byteArray = getArguments().getByteArray("img");
        img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        foto.setImageBitmap(img);


        fecha = root.findViewById(R.id.fecha);
        tramo = root.findViewById(R.id.tramo);

        tdireccion = root.findViewById(R.id.direccion);
        tciudad = root.findViewById(R.id.ciudad);

        tdireccion.setText(direccion.getCalle() + " #" + direccion.getNumero());
        tciudad.setText(direccion.getCiudad() + ", " + direccion.getRegion());

        ticarton = root.findViewById(R.id.totcarton);
        tilata = root.findViewById(R.id.totlata);
        tiplastico = root.findViewById(R.id.totplastico);
        tividrio = root.findViewById(R.id.totvidrio);
        estado = root.findViewById(R.id.estado);

        ticarton.setText(String.valueOf(totcarton) + " unidades");
        tividrio.setText(String.valueOf(totvidrio) + " unidades");
        tilata.setText(String.valueOf(totlatas)  + " unidades");
        tiplastico.setText(String.valueOf(totplastico)  + " unidades");

        botonvolver = root.findViewById(R.id.btnvolver);


        estado.setText("Estado: " + retiro.getRetiro().getEstado().toUpperCase());
        botonvolver.setOnClickListener(this);

        ImageView img = new ImageView(getContext());


        fecha.setText(tramoseleccionado.getFecha());
        tramo.setText("Tramo " + tramoseleccionado.getHorainicio() + " - " + tramoseleccionado.getHorafinal());

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Detalle Retiro");



        return root;
    }






    @Override
    public void onClick(View view) {

        if (view == this.btncancelar){
            if (retiro.getRetiro().getEstado().compareTo("finalizado")!=0){

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp1 = new CargandoFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp1);
                fragmentTransaction.commit();

                db = FirebaseFirestore.getInstance();

                retiro.getRetiro().setEstado("cancelado");
                System.out.println("id del retiro a actualizar: " + retiro.getRetiro().getId());
                db.collection("retiro").document(retiro.getRetiro().getId())
                        .set(retiro.getRetiro())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Retiro actualizado correctamente");
                                //Toast.makeText(getContext(),"Reporte creado correctamente con ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();


                                InformacionFragment fi = new InformacionFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("mensaje", "Retiro actualizado correctamente");
                                bundle.putBoolean("estado", true);
                                fi.setArguments(bundle);

                                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                                fragmentTransaction2.commit();
                                //Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        }

        if (view==this.botonsiguiente){

        }
        else if (view == this.botonvolver){
            RetirosListFragment fi = new RetirosListFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.nav_host_fragment, fi);
            fragmentTransaction.commit();
        }


    }



}