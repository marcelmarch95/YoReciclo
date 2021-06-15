package com.example.patacon.ui.agregardireccion;

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
import com.example.patacon.ui.optionproducts.OptionsPuntosListFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import Modelo.Direccion;
import Modelo.Punto;

import static android.app.Activity.RESULT_OK;

public class AgregarDireccionFragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private Button btnsiguiente;
    private Spinner sector;
    private Spinner region;
    private Spinner ciudad;
    private EditText ecalle;
    private EditText enumero;
    private TextView eindicaciones;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_agregardireccion, container, false);

        ecalle = (EditText) root.findViewById(R.id.ecalle);
        enumero = (EditText) root.findViewById(R.id.enumero);
        eindicaciones = (EditText) root.findViewById(R.id.eindicaciones);

        btnsiguiente = (Button) root.findViewById(R.id.btnsiguiente);
        btnsiguiente.setOnClickListener(this);


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


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Agregar Dirección");

        return root;
    }




    @Override
    public void onClick(View view) {
        if (view == this.btnsiguiente){
            validarDatos();

        }
    }

    public void validarDatos(){

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

        Direccion d = new Direccion();
        d.setDeleted(false);
        d.setCalle(ecalle.getText().toString());
        d.setNumero(enumero.getText().toString());
        d.setIndicaciones(eindicaciones.getText().toString());
        d.setRegion(region.getSelectedItem().toString());
        d.setCiudad(ciudad.getSelectedItem().toString());
        d.setSector(sector.getSelectedItem().toString());

        Intent intent = new Intent(getActivity(), SelectorDireccionMapaDireccion.class);
        intent.putExtra("editar", false);
        intent.putExtra("dire",d);
        startActivity(intent);
    }

}