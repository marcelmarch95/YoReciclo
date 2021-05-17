package com.example.patacon.ui.editardireccion;

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

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.SelectorDireccionMapaDireccion;
import com.example.patacon.SelectorDireccionMapaPunto;
import com.example.patacon.ui.MisDireccionesFragment;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntos.PuntosListFragment;
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

import Modelo.Direccion;
import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaPunto;
import Modelo.Punto;

public class EditarDireccionFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private Button cancelar;
    private Button editar1;
    private Button editar2;
    private Spinner sector;
    private Spinner region;
    private Spinner ciudad;
    private EditText ecalle;
    private EditText enumero;
    private TextView eindicaciones;
    private ArrayList<ModeloVistaDireccion> data = new ArrayList<>();
    private Direccion direccion;

    private FirebaseFirestore db;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_editardireccion, container, false);


        this.direccion = (Direccion) getArguments().getSerializable("direccion");

        ecalle = (EditText) root.findViewById(R.id.ecalle);
        enumero = (EditText) root.findViewById(R.id.enumero);
        eindicaciones = (EditText) root.findViewById(R.id.eindicaciones);

        region = (Spinner) root.findViewById(R.id.region);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.region, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapter2);

        ciudad = (Spinner) root.findViewById(R.id.ciudad);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.ciudad, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ciudad.setAdapter(adapter3);

        sector = (Spinner) root.findViewById(R.id.sector);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getContext(), R.array.macrosector, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sector.setAdapter(adapter4);


        this.cancelar = root.findViewById(R.id.cancelar);
        this.editar1 = root.findViewById(R.id.editar1);
        this.editar2 = root.findViewById(R.id.editar2);

        this.cancelar.setOnClickListener(this);
        this.editar1.setOnClickListener(this);
        this.editar2.setOnClickListener(this);

        ecalle.setText(direccion.getCalle());
        enumero.setText(direccion.getNumero());
        eindicaciones.setText(direccion.getIndicaciones());


        for (int i = 0; i < sector.getCount(); i++) {
            if (sector.getItemAtPosition(i).equals(direccion.getSector())) {
                sector.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < region.getCount(); i++) {
            if (region.getItemAtPosition(i).equals(direccion.getRegion())) {
                region.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < ciudad.getCount(); i++) {
            if (ciudad.getItemAtPosition(i).equals(direccion.getCiudad())) {
                ciudad.setSelection(i);
                break;
            }
        }

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Editar Dirección");

        return root;
    }



    @Override
    public void onClick(View view) {
        if (view == this.cancelar){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("direccion")
                    .whereEqualTo("pid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Direccion d = document.toObject(Direccion.class);
                                    ModeloVistaDireccion mvd = new ModeloVistaDireccion();
                                    d.setId(document.getId());
                                    mvd.setDireccion(d);
                                    data.add(mvd);
                                }

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("direcciones", data);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new MisDireccionesFragment();
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

        if (ecalle.getText().toString().isEmpty()){
            this.ecalle.requestFocus();
            this.ecalle.setError("Ingrese nombre de calle");
            error = true;
        }
        if (enumero.getText().toString().isEmpty()){
            this.enumero.requestFocus();
            this.enumero.setError("Ingrese numero de calle");
            error = true;
        }
        if (eindicaciones.getText().toString().isEmpty()){
            this.eindicaciones.requestFocus();
            this.eindicaciones.setError("Ingrese indicaciones");
            error = true;
        }

        if (error)
            return;

        direccion.setCalle(ecalle.getText().toString());
        direccion.setNumero(enumero.getText().toString());
        direccion.setIndicaciones(eindicaciones.getText().toString());
        direccion.setRegion(region.getSelectedItem().toString());
        direccion.setCiudad(ciudad.getSelectedItem().toString());
        direccion.setSector(sector.getSelectedItem().toString());


        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (editarubicacion){
            Intent intent = new Intent(getActivity(), SelectorDireccionMapaDireccion.class);
            intent.putExtra("editar", false);
            intent.putExtra("dire",direccion);
            startActivity(intent);
        }

        else {

            Fragment lp = new CargandoFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();

            db = FirebaseFirestore.getInstance();
            db.collection("direccion").document(direccion.getId())
                    .set(direccion)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Direccion editada correctamente");
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
                            bundle.putSerializable("mensaje", "Error al editar dirección");
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