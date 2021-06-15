package com.example.patacon.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.puntos.PuntosListFragment;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaRetiro;
import Modelo.Punto;
import Modelo.Retiro;
import Modelo.TramoRetiro;

public class SolicitarRetiroFragment extends Fragment implements View.OnClickListener {



    private View root;
    private ImageView imagen;
    private Bitmap img;

    private TramoRetiro tramoseleccionado;
    private int totplastico = 0;
    private int totlatas = 0;
    private int totvidrio = 0;
    private int totcarton = 0;
    private Button botonvolver;
    private Button botonsiguiente;
    private Direccion direccion;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String comentarios;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_solicitaretiro, container, false);

        imagen = (ImageView) root.findViewById(R.id.imageView11);

        tramoseleccionado = (TramoRetiro) getArguments().getSerializable("tramo");
        direccion = (Direccion) getArguments().getSerializable("direccion");
        totlatas = (int) getArguments().getSerializable("lata");
        totvidrio = (int) getArguments().getSerializable("vidrio");
        totcarton = (int) getArguments().getSerializable("carton");
        totplastico = (int) getArguments().getSerializable("plastico");
        comentarios = (String) getArguments().getSerializable("comentarios");

        byte[] byteArray = getArguments().getByteArray("img");

        if (byteArray==null){
            img = null;
        }
        else {
            img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }



        Glide.with(this).load(R.drawable.carg).into(imagen);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Enviando solicitud");

        enviarDatos();


        return root;
    }

    private void enviarDatos() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        StorageReference riversRef = storageRef.child("images/"+mAuth.getUid()+"/"+tramoseleccionado.getId()+"/1");

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("11111111 error");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("11111111 success");

                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {

                        Retiro retiro = new Retiro();
                        retiro.setIdTramo(tramoseleccionado.getId());
                        retiro.setIdDireccion(direccion.getId());
                        retiro.setUid(mAuth.getUid());
                        retiro.setFoto1(downloadUrl.toString());
                        retiro.setTotcarton(String.valueOf(totcarton));
                        retiro.setTotlata(String.valueOf(totlatas));
                        retiro.setTotvidrio(String.valueOf(totvidrio));
                        retiro.setTotplastico(String.valueOf(totplastico));
                        retiro.setIdrecicladora(tramoseleccionado.getUid());
                        retiro.setComentarios(comentarios);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        db.collection("retiro")
                        .add(retiro)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                System.out.println("Retiro creado correctamente con ID: " + documentReference.getId());
                                //Toast.makeText(getContext(),"Reporte creado correctamente con ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();

                                db = FirebaseFirestore.getInstance();

                                int nuevo = Integer.valueOf(tramoseleccionado.getCuposdisponibles())-1;
                                tramoseleccionado.setCuposdisponibles(nuevo);

                                db.collection("tramoretiro").document(tramoseleccionado.getId())
                                .set(tramoseleccionado)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("Retiro creado correctamente");
                                        //Toast.makeText(getContext(),"Reporte creado correctamente con ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();


                                        InformacionFragment fi = new InformacionFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("mensaje", "Retiro creado correctamente");
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
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                System.out.println("Error al crear el retiro");
                                //Toast.makeText(getContext(),"Error al crear reporte!",Toast.LENGTH_SHORT).show();


                                InformacionFragment fi = new InformacionFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("mensaje", "Error al crear retiro");
                                bundle.putBoolean("estado", false);
                                fi.setArguments(bundle);

                                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                                fragmentTransaction2.commit();
                            }
                        });
                    }
                });



            }



        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int intValue = (int) progress;
                //cf.actualizarCarga(intValue);
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });
    }


    @Override
    public void onClick(View view) {



    }



}