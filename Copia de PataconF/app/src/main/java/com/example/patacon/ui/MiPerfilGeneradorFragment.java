package com.example.patacon.ui;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.MainActivity;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import Modelo.Generador;
import Modelo.Recicladora;

import static android.app.Activity.RESULT_OK;

public class MiPerfilGeneradorFragment extends Fragment implements View.OnClickListener {


    //private Button selectfoto;
    private Button guardar;
    //private Button eliminarfoto;
    private Button exit;
    private FirebaseFirestore db;
    //private ImageView foto;
    private FirebaseAuth mAuth;
    //private Uri imageUri;
    private static final int PICK_IMAGE = 100;
    private Generador generador;

    private TextView nombreApellido;
    private ProgressBar progressBar;

    private EditText numeroTelefono;
    private EditText correo;
    private EditText contraseña;

    private TextView estadocorreo;
    private ImageView imgestadocorreo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_miperfilgenerador, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        this.nombreApellido = (TextView) root.findViewById(R.id.nombreApellido);
        this.correo = (EditText) root.findViewById(R.id.correo);
        this.numeroTelefono = (EditText) root.findViewById(R.id.numeroTelefono);
        this.contraseña = (EditText) root.findViewById(R.id.contraseña);
        //this.foto = (ImageView) root.findViewById(R.id.imageView4);

        this.progressBar = root.findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);

        this.estadocorreo = root.findViewById(R.id.estadocorreo);
        this.imgestadocorreo = root.findViewById(R.id.imgestadocorreo);

        if (mAuth.getCurrentUser().isEmailVerified()){
            this.estadocorreo.setText("Correo verificado");
            this.imgestadocorreo.setImageResource(R.drawable.correok);
        }
        else {
            this.estadocorreo.setText("Correo no verificado");
            this.imgestadocorreo.setImageResource(R.drawable.correno);
        }

        this.guardar = (Button) root.findViewById(R.id.guardar);
        guardar.setOnClickListener(this);


        DocumentReference docRef = db.collection("generador").document(mAuth.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("documento encontrado: " + document.getData());
                        Generador g =  document.toObject(Generador.class);
                        nombreApellido.setText(g.getNombre()+ " " + g.getApellido());
                        correo.setText(g.getCorreo());
                        numeroTelefono.setText(g.getNumeroTelefono());
                        contraseña.setText(g.getContraseña());
                        generador = g;

                        /*try {
                            new DownloadImageTask(foto).execute(g.getFoto());
                        }
                        catch (Exception e){
                            foto.setImageResource(R.drawable.recicon);
                            System.out.println("Error al cargar foto: " + c.getFoto());
                        }*/

                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        //this.selectfoto = (Button) root.findViewById(R.id.selectfoto);

        this.exit = (Button) root.findViewById(R.id.exit);
        this.exit.setOnClickListener(this);

        /*this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });*/

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mi Perfil");

        return root;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }





    public void updateViewPerfil(){

    }


    @Override
    public void onClick(View view) {

        if(view == this.guardar){
            this.guardar.setEnabled(false);
            this.progressBar.setVisibility(View.VISIBLE);
            //saveFoto();
            updateViewPerfil();
        }


        /*if (view == this.eliminarfoto){
            this.foto.setImageResource(R.drawable.imagen2);
            this.eliminarfoto.setEnabled(false);
        }*/

        if (view == this.exit){
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result==null){
                bmImage.setImageResource(R.drawable.market);
            }
            else {
                bmImage.setImageBitmap(result);
            }
        }
    }



}