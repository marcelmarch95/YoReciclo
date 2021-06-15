package com.example.patacon.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import Modelo.Direccion;
import Modelo.ModeloVistaPunto;
import Modelo.Punto;
import Modelo.Reporte;
import Modelo.TramoRetiro;

import static android.app.Activity.RESULT_OK;

public class AgregarFotoFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Punto> data2 = new ArrayList<>();
    private String hora;
    private EditText comentarios;

    private Button volver;
    private boolean stateMap;
    private Button selectfoto;
    private Button guardar;
    private Button eliminarfoto;
    private Button continuar;
    private Button exit;
    private ImageView foto;
    private FirebaseAuth mAuth;
    private boolean fotocargada = false;

    private FirebaseFirestore db;
    private Bitmap img;
    private View root;
    private static final int PICK_IMAGE = 100;

    private TramoRetiro tramoseleccionado;
    private int totplastico = 0;
    private int totlatas = 0;
    private int totvidrio = 0;
    private int totcarton = 0;
    private Direccion direccion;

    private TextView fecha;
    private TextView tramo;
    private TextView tdireccion;
    private TextView tciudad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        root = inflater.inflate(R.layout.fragment_agregarfoto, container, false);

        tramoseleccionado = (TramoRetiro) getArguments().getSerializable("tramo");
        direccion = (Direccion) getArguments().getSerializable("direccion");
        totlatas = (int) getArguments().getSerializable("lata");
        totvidrio = (int) getArguments().getSerializable("vidrio");
        totcarton = (int) getArguments().getSerializable("carton");
        totplastico = (int) getArguments().getSerializable("plastico");

        comentarios = (EditText) root.findViewById(R.id.comentarios);

        fecha = root.findViewById(R.id.fecha2);
        tramo = root.findViewById(R.id.tramo2);

        tdireccion = root.findViewById(R.id.direccion);
        tciudad = root.findViewById(R.id.ciudad);

        tdireccion.setText(direccion.getCalle() + " #" + direccion.getNumero());
        tciudad.setText(direccion.getCiudad() + ", " + direccion.getRegion());

        fecha.setText(tramoseleccionado.getFecha());
        tramo.setText("Tramo " + tramoseleccionado.getHorainicio() + " - " + tramoseleccionado.getHorafinal());

        this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

        this.foto = (ImageView) root.findViewById(R.id.imageView4);
        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);
        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openGallery();
                abrirCamara();
            }
        });

        this.continuar = root.findViewById(R.id.continuar);
        this.continuar.setOnClickListener(this);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Agregar foto de residuos");

        return root;
    }

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getContext().getPackageManager()) != null){
            startActivityForResult(intent, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            img = (Bitmap) extras.get("data");
            foto.setImageBitmap(img);
            fotocargada = true;
            eliminarfoto.setEnabled(true);
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto.setImageURI(imageUri);
            eliminarfoto.setEnabled(true);
        }
    }*/


    @Override
    public void onClick(View view) {
        if (view == this.eliminarfoto){
            this.eliminarfoto.setEnabled(false);
        }

        if (view==this.continuar){
            if (!fotocargada){
                Toast.makeText(getContext(), "Error, debes tomar una foto a los residuos...", Toast.LENGTH_SHORT).show();
                return;
            }

            Fragment lp = new ConfirmacionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("tramo",tramoseleccionado);
            bundle.putSerializable("direccion", direccion);
            bundle.putSerializable("lata",totlatas);
            bundle.putSerializable("plastico",totplastico);
            bundle.putSerializable("vidrio", totvidrio);
            bundle.putSerializable("carton",totcarton);
            bundle.putSerializable("comentarios", comentarios.getText().toString());


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bundle.putByteArray("img", byteArray);

            lp.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();

            lp.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        if (view == this.volver){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    data2.add(document.toObject(Punto.class));
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
    }



}