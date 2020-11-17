package com.example.pataconf.ui.agregarproducto;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.SelectorDireccionMapa;
import com.example.pataconf.SelectorDireccionMapaPunto;
import com.example.pataconf.ui.cargando.CargandoFragment;
import com.example.pataconf.ui.informacion.InformacionFragment;
import com.example.pataconf.ui.optionproducts.OptionsProductListViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.Serializable;

import Modelo.Producto;
import Modelo.Punto;

import static android.app.Activity.RESULT_OK;

public class AgregarPuntoFragment extends Fragment implements View.OnClickListener {

    private OptionsProductListViewModel homeViewModel;
    private Button selectfoto;
    private Button eliminarfoto;
    private Button agregar;
    private Spinner area;
    private Spinner propiedad;
    private Spinner recinto;
    private EditText edireccion;
    private EditText eobservacion;
    private CheckBox vidrio;
    private CheckBox lata;
    private CheckBox plastico;
    private TextView title;

    private FirebaseFirestore db;
    private ImageView foto;
    private Uri imageUri;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsProductListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agregarpunto, container, false);

        edireccion = (EditText) root.findViewById(R.id.edireccion);
        area = (Spinner) root.findViewById(R.id.area);
        propiedad = (Spinner) root.findViewById(R.id.propiedad);
        recinto = (Spinner) root.findViewById(R.id.recinto);
        eobservacion = (EditText) root.findViewById(R.id.observacion);

        title = (TextView) root.findViewById(R.id.title);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.area, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.macrosector, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propiedad.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.recinto, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recinto.setAdapter(adapter3);

        this.vidrio = (CheckBox) root.findViewById(R.id.vidrio);
        this.lata = (CheckBox) root.findViewById(R.id.lata);
        this.plastico = (CheckBox) root.findViewById(R.id.plastico);


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

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Agregar Punto");

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

        if (edireccion.getText().toString().isEmpty()) {
            this.edireccion.requestFocus();
            this.edireccion.setError("Ingrese dirección del punto");
            error = true;
        }

        int total = 0;
        if (this.vidrio.isChecked())
            total++;
        if (this.lata.isChecked())
            total++;
        if(this.plastico.isChecked())
            total++;

        if (total==0){
            this.title.requestFocus();
            this.title.setError("* Obligatorio");
            error = true;
        }


        if (error)
            return;


        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //this.categoria.getSelectedItem().toString()

        Punto p = new Punto();
        p.setArea(this.area.getSelectedItem().toString());
        p.setPropiedad(this.propiedad.getSelectedItem().toString());
        p.setRecinto(this.recinto.getSelectedItem().toString());
        p.setDireccion(this.edireccion.getText().toString());
        p.setObservacion(this.eobservacion.getText().toString());
        p.setFoto("notlink");

        System.out.println("Uri: " + imageUri);

        Intent intent = new Intent(getActivity(), SelectorDireccionMapaPunto.class);
        intent.putExtra("punto", p);
        intent.putExtra("uri", imageUri);
        startActivity(intent);


    }

}