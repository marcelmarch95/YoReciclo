package com.example.patacon.ui.editarpunto;

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

import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.puntos.PuntosListFragment;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.SelectorDireccionMapaPunto;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class EditarPuntoFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private Button cancelar;
    private Button editar1;
    private Button editar2;
    private Spinner sector;
    private EditText edireccion;
    private EditText eobservacion;
    private CheckBox vidrio;
    private CheckBox lata;
    private CheckBox plastico;
    private TextView title;
    private RadioGroup recintog;
    private RadioGroup areag;
    private ModeloVistaPunto punto;
    private RadioButton publico;
    private RadioButton privado;
    private RadioButton urbano;
    private RadioButton rural;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();

    private FirebaseFirestore db;
    private ImageView foto;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_editarpunto, container, false);

        edireccion = (EditText) root.findViewById(R.id.edireccion);
        sector = (Spinner) root.findViewById(R.id.sector);
        eobservacion = (EditText) root.findViewById(R.id.observacion);

        title = (TextView) root.findViewById(R.id.title);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.macrosector, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sector.setAdapter(adapter2);

        this.vidrio = (CheckBox) root.findViewById(R.id.vidrio);
        this.lata = (CheckBox) root.findViewById(R.id.lata);
        this.plastico = (CheckBox) root.findViewById(R.id.plastico);
        this.recintog = (RadioGroup) root.findViewById(R.id.recintog);
        this.areag = (RadioGroup) root.findViewById(R.id.areag);

        this.publico = (RadioButton) root.findViewById(R.id.btpublico);
        this.privado = (RadioButton) root.findViewById(R.id.btprivado);

        this.urbano = (RadioButton) root.findViewById(R.id.bturbano);
        this.rural = (RadioButton) root.findViewById(R.id.btrural);


        foto = (ImageView) root.findViewById(R.id.imageView4);

        this.cancelar = root.findViewById(R.id.cancelar);
        this.editar1 = root.findViewById(R.id.editar1);
        this.editar2 = root.findViewById(R.id.editar2);

        this.cancelar.setOnClickListener(this);
        this.editar1.setOnClickListener(this);
        this.editar2.setOnClickListener(this);

        this.vidrio.setChecked(this.punto.isIsvidrio());
        this.plastico.setChecked(this.punto.isIsplastico());
        this.lata.setChecked(this.punto.isIslatas());

        System.out.println("Recinto: " + this.punto.getRecinto());
        System.out.println("Area: " + this.punto.getArea());

        if (this.punto.getRecinto().compareTo("Público")==0)
            this.publico.setChecked(true);
        else {
            this.privado.setChecked(true);
        }

        if (this.punto.getArea().compareTo("Urbano")==0)
            this.urbano.setChecked(true);
        else {
            this.rural.setChecked(true);
        }

        for (int i = 0; i < sector.getCount(); i++) {
            if (sector.getItemAtPosition(i).equals(punto.getSector())) {
                sector.setSelection(i);
                break;
            }
        }

        this.edireccion.setText(punto.getDireccion());
        this.eobservacion.setText(punto.getObservacion());
        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Editar Punto");

        return root;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }



    @Override
    public void onClick(View view) {
        if (view == this.cancelar){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
            .whereEqualTo("pid", user.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Punto p = document.toObject(Punto.class);
                            ModeloVistaPunto pu = new ModeloVistaPunto();
                            pu.setDireccion(p.getDireccion());
                            pu.setLat(p.getLat());
                            pu.setLng(p.getLng());
                            pu.setSector(p.getSector());
                            pu.setRecinto(p.getRecinto());
                            pu.setArea(p.getArea());
                            pu.setFoto(p.getFoto());
                            pu.setIslatas(p.isIslatas());
                            pu.setIsplastico(p.isIsplastico());
                            pu.setIsvidrio(p.isIsvidrio());
                            pu.setId(document.getId());
                            pu.setObservacion(p.getObservacion());
                            data.add(pu);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("puntos", data);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment lp = new PuntosListFragment();
                        lp.setArguments(bundle);
                        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                        fragmentTransaction.commit();
                    } else {

                    }
                }
            });
        }

        else if (view == this.editar1){
            validarDatos(false);
        }
        else {
            validarDatos(true);
        }
    }

    public void validarDatos(boolean editarubicacion){
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
        p.setPid(this.punto.getPid());
        p.setLat(this.punto.getLat());
        p.setLng(this.punto.getLng());
        p.setId(this.punto.getId());

        System.out.println("Uri: " + imageUri);

        if (editarubicacion){
            Intent intent = new Intent(getActivity(), SelectorDireccionMapaPunto.class);
            intent.putExtra("punto", p);
            intent.putExtra("editar", true);
            intent.putExtra("uri", imageUri);
            startActivity(intent);
        }

        else {

            Fragment lp = new CargandoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();

            db = FirebaseFirestore.getInstance();
            db.collection("punto").document(p.getId())
                    .set(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Punto editado correctamente");
                            bundle.putSerializable("estado", true);

                            Fragment info = new InformacionFragment();
                            info.setArguments(bundle);
                            fragmentTransaction2.replace(R.id.nav_host_fragment, info);
                            fragmentTransaction2.commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Error al editar el punto");
                            bundle.putSerializable("estado", false);

                            Fragment info = new InformacionFragment();
                            info.setArguments(bundle);
                            fragmentTransaction2.replace(R.id.nav_host_fragment, info);
                            fragmentTransaction2.commit();
                        }
                    });
        }


    }

}