package com.example.pataconf.ui.agregarproducto;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.ActivityCargando;
import com.example.pataconf.ActivityPrincipal;
import com.example.pataconf.Informacion;
import com.example.pataconf.ModeloVistaOpcionesProductoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.example.pataconf.ui.optionproducts.OptionsProductListViewModel;
import com.example.pataconf.ui.products.ProductListFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import Modelo.ModeloOpcionesProducto;
import Modelo.Producto;

import static android.app.Activity.RESULT_OK;

public class AgregarProductoFragment extends Fragment implements View.OnClickListener {

    private OptionsProductListViewModel homeViewModel;
    private EditText eprecio;
    private Button selectfoto;
    private Button eliminarfoto;
    private Button agregar;
    private Spinner categoria;
    private EditText nombre;
    private EditText codigo;
    private EditText stock;
    private FirebaseFirestore db;
    private EditText descripcion;
    private ImageView foto;
    private Uri imageUri;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsProductListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agregarproducto, container, false);

        nombre = (EditText) root.findViewById(R.id.enombre);
        eprecio = (EditText) root.findViewById(R.id.eprecio);
        categoria = (Spinner) root.findViewById(R.id.categoria);
        codigo = (EditText) root.findViewById(R.id.ecodigo);
        stock = (EditText) root.findViewById(R.id.stock);
        descripcion = (EditText) root.findViewById(R.id.edescripcion);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categoriasproductos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        eprecio.addTextChangedListener( new TextWatcher() {
            boolean isEdiging;
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void afterTextChanged(Editable s) {
                if(isEdiging) return;
                isEdiging = true;

                String str = s.toString().replaceAll( "[^\\d]", "" );

                if (str.compareTo("")!=0) {
                    double s1 = Double.parseDouble(str);
                    NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                    ((DecimalFormat) nf2).applyPattern("$ ###,###");
                    s.replace(0, s.length(), nf2.format(s1));
                }
                isEdiging = false;
            }
        });

        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);
        this.agregar = (Button) root.findViewById(R.id.agregar);

        foto = (ImageView) root.findViewById(R.id.imageView4);
        this.agregar.setOnClickListener(this);

        this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Agregar Producto");

        return root;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto.setImageURI(imageUri);
            eliminarfoto.setEnabled(true);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == this.agregar){
            validarDatos();
        }

        if (view == this.eliminarfoto){
            this.foto.setImageResource(R.drawable.imagen2);
            this.eliminarfoto.setEnabled(false);
        }
    }

    public void validarDatos(){
        boolean error = false;

        if (nombre.getText().toString().isEmpty()) {
            this.nombre.requestFocus();
            this.nombre.setError("Ingrese nombre producto");
            error = true;
        }

        if (this.codigo.getText().toString().isEmpty()){
            this.codigo.requestFocus();
            this.codigo.setError("Ingrese código del producto");
            error = true;
        }
        if (this.stock.getText().toString().isEmpty()){
            this.stock.requestFocus();
            this.stock.setError("Ingrese stock");
            error = true;
        }
        if (this.eprecio.getText().toString().isEmpty()){
            this.eprecio.requestFocus();
            this.eprecio.setError("Ingrese precio");
            error = true;
        }

        if (error)
            return;


        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lp = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
        fragmentTransaction.commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Producto p = new Producto((this.nombre.getText().toString()), this.codigo.getText().toString(),
                this.categoria.getSelectedItem().toString(),this.descripcion.getText().toString(),"notlink",
                Integer.valueOf(this.eprecio.getText().toString()), Integer.valueOf(this.stock.getText().toString()), user.getUid());

        System.out.println("Uri: " + imageUri);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child("images/"+p.getComerciante()+"/" + imageUri.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(imageUri);

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
                        p.setFoto(downloadUrl.toString());
                        System.out.println("subida correctamente link: " + p.getFoto());

                        db.collection("producto").document(p.getCodigo()).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Bundle bundle = new Bundle();
                                bundle.putString("mensaje", "Producto agregado correctamente");
                                bundle.putBoolean("estado", true);
                                bundle.putBoolean("productos", true);

                                Fragment lp = new InformacionFragment();
                                lp.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Bundle bundle = new Bundle();
                                bundle.putString("mensaje", "Error al agregar el Producto");
                                bundle.putBoolean("estado", false);
                                bundle.putBoolean("productos", true);

                                Fragment lp = new InformacionFragment();
                                lp.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();

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

}