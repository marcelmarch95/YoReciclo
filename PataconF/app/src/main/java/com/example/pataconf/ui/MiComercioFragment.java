package com.example.pataconf.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pataconf.MainActivity;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.URL;

import Modelo.Recicladora;

import static android.app.Activity.RESULT_OK;

public class MiComercioFragment extends Fragment implements View.OnClickListener {


    private Button guardar;
    private Button exit;
    private FirebaseFirestore db;
    private ImageView foto;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private static final int PICK_IMAGE = 100;
    private Recicladora comerciante;

    private TextView nombreComercio;
    private TextView correo;
    private ProgressBar progressBar;

    private EditText direccion;
    private EditText localidad;
    private EditText nombreEncargado;
    private EditText telefono;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_micomercio, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        this.nombreComercio = (TextView) root.findViewById(R.id.nombreComercio);
        this.correo = (TextView) root.findViewById(R.id.correo);

        this.direccion = (EditText) root.findViewById(R.id.direccion);
        this.localidad = (EditText) root.findViewById(R.id.localidad);
        this.nombreEncargado = (EditText) root.findViewById(R.id.nombreDeEncargado);
        this.telefono = (EditText) root.findViewById(R.id.telefono);
        this.foto = (ImageView) root.findViewById(R.id.imageView4);

        this.progressBar = root.findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);


        this.guardar = (Button) root.findViewById(R.id.guardar);
        guardar.setOnClickListener(this);


        DocumentReference docRef = db.collection("recicladora").document(mAuth.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("documento encontrado: " + document.getData());
                        Recicladora c =  document.toObject(Recicladora.class);
                        nombreComercio.setText(c.getNombreEmpresa());
                        correo.setText(c.getCorreo());
                        direccion.setText(c.getDireccion());
                        localidad.setText(c.getLocalidad());
                        nombreEncargado.setText(c.getNombreEncargado());
                        telefono.setText(c.getNumeroTelefono());
                        comerciante = c;
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        this.exit = (Button) root.findViewById(R.id.exit);
        this.exit.setOnClickListener(this);

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mi Recicladora");

        return root;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }




    public void updateViewPerfil(){
        this.progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        comerciante.setDireccion(direccion.getText().toString());
        comerciante.setNombreEncargado(nombreEncargado.getText().toString());
        comerciante.setNumeroTelefono(telefono.getText().toString());

        db = FirebaseFirestore.getInstance();
        db.collection("recicladora").document(currentUser.getUid())
        .set(comerciante)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Actualizado correctamente!",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                guardar.setEnabled(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al actualizar",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                guardar.setEnabled(true);
            }
        });


    }


    @Override
    public void onClick(View view) {

        if(view == this.guardar){
            this.guardar.setEnabled(false);
            this.progressBar.setVisibility(View.VISIBLE);
            updateViewPerfil();
        }




        if (view == this.exit){
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
        }
    }






}