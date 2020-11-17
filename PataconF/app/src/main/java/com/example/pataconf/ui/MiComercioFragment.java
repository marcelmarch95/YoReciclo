package com.example.pataconf.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pataconf.ActivityCargando;
import com.example.pataconf.ActivityPrincipal;
import com.example.pataconf.Informacion;
import com.example.pataconf.MainActivity;
import com.example.pataconf.ModeloVistaOpcionesProductoAdapter;
import com.example.pataconf.ModeloVistaProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.example.pataconf.ui.optionproducts.OptionsProductListViewModel;
import com.example.pataconf.ui.products.ProductListFragment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import Modelo.Comerciante;
import Modelo.ModeloOpcionesProducto;
import Modelo.ModeloVistaProducto;
import Modelo.Producto;
import Modelo.Recicladora;

import static android.app.Activity.RESULT_OK;

public class MiComercioFragment extends Fragment implements View.OnClickListener {


    private Button selectfoto;
    private Button guardar;
    private Button eliminarfoto;
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


        DocumentReference docRef = db.collection("comerciante").document(mAuth.getUid().toString());
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

                        try {
                            new DownloadImageTask(foto).execute(c.getFoto());
                        }
                        catch (Exception e){
                            foto.setImageResource(R.drawable.recicon);
                            System.out.println("Error al cargar foto: " + c.getFoto());
                        }

                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);

        this.exit = (Button) root.findViewById(R.id.exit);
        this.exit.setOnClickListener(this);

        this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Mi Recicladora");

        return root;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void saveFoto(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child("images/"+mAuth.getUid()+"/" + imageUri.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(imageUri);

        this.progressBar.setVisibility(View.VISIBLE);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("error al subir : " + exception.toString());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {
                        comerciante.setFoto(downloadUrl.toString());
                        System.out.println("subida correctamente link: " + comerciante.getFoto());

                        db.collection("comerciante").document(comerciante.getUid()).set(comerciante).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getContext(),"Actualizado correctamente!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                guardar.setEnabled(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getContext(),"Error al actualizar!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                guardar.setEnabled(true);

                            }
                        });
                    }
                });

            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto.setImageURI(imageUri);
            eliminarfoto.setEnabled(true);
        }
    }

    public void updateViewPerfil(){

    }


    @Override
    public void onClick(View view) {

        if(view == this.guardar){
            this.guardar.setEnabled(false);
            this.progressBar.setVisibility(View.VISIBLE);
            saveFoto();
            updateViewPerfil();
        }


        if (view == this.eliminarfoto){
            this.foto.setImageResource(R.drawable.imagen2);
            this.eliminarfoto.setEnabled(false);
        }

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