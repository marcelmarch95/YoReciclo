package com.example.pataconf.ui.agregarpunto;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.SelectorDireccionMapaPunto;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import Modelo.Punto;

import static android.app.Activity.RESULT_OK;

public class AgregarPuntoFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private Button selectfoto;
    private Button volver;
    private Button eliminarfoto;
    private Button agregar;
    private Spinner sector;
    private EditText edireccion;
    private EditText eobservacion;
    private CheckBox vidrio;
    private CheckBox lata;
    private CheckBox plastico;
    private TextView title;
    private RadioGroup recintog;
    private RadioGroup areag;

    private FirebaseFirestore db;
    private ImageView foto;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_agregarpunto, container, false);

        edireccion = (EditText) root.findViewById(R.id.edireccion);
        sector = (Spinner) root.findViewById(R.id.sector);
        eobservacion = (EditText) root.findViewById(R.id.observacion);

        title = (TextView) root.findViewById(R.id.title);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.macrosector, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sector.setAdapter(adapter2);



        this.vidrio = (CheckBox) root.findViewById(R.id.vidrio);
        this.lata = (CheckBox) root.findViewById(R.id.lata);
        this.plastico = (CheckBox) root.findViewById(R.id.plastico);
        this.recintog = (RadioGroup) root.findViewById(R.id.recintog);
        this.areag = (RadioGroup) root.findViewById(R.id.areag);
        this.volver = (Button) root.findViewById(R.id.volver);
        this.volver.setOnClickListener(this);

        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);
        this.agregar = (Button) root.findViewById(R.id.agregar);

        foto = (ImageView) root.findViewById(R.id.imageView4);
        this.agregar.setOnClickListener(this);

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

        if (view==this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
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

        int idrecinto = recintog.getCheckedRadioButtonId();
        int idarea = areag.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton recinto = (RadioButton) root.findViewById(idrecinto);
        RadioButton area = (RadioButton) root.findViewById(idarea);

        Punto p = new Punto();
        p.setSector(this.sector.getSelectedItem().toString());
        p.setDireccion(this.edireccion.getText().toString());
        p.setObservacion(this.eobservacion.getText().toString());
        p.setFoto("notlink");
        p.setRecinto(recinto.getText().toString());
        p.setArea(area.getText().toString());
        p.setIslatas(this.lata.isChecked());
        p.setIsplastico(this.plastico.isChecked());
        p.setIsvidrio(this.vidrio.isChecked());

        System.out.println("Uri: " + imageUri);

        Intent intent = new Intent(getActivity(), SelectorDireccionMapaPunto.class);
        intent.putExtra("punto", p);
        intent.putExtra("uri", imageUri);
        intent.putExtra("editar", false);
        startActivity(intent);


    }

}