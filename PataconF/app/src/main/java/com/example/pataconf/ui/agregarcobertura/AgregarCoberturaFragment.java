package com.example.pataconf.ui.agregarcobertura;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.SelectorDireccionMapaCobertura;

import Modelo.Cobertura;

public class AgregarCoberturaFragment extends Fragment implements View.OnClickListener {

    private Button btnsiguiente;
    private Spinner region;
    private Spinner ciudad;
    private EditText ecalle;
    private EditText enumero;
    private View root;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_agregarcobertura, container, false);

        ecalle = (EditText) root.findViewById(R.id.ecalle);
        enumero = (EditText) root.findViewById(R.id.enumero);

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



        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Agregar Cobertura");

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

        if (error)
            return;

        Cobertura d = new Cobertura();
        d.setNombre(ecalle.getText().toString());
        d.setDescripcion(enumero.getText().toString());
        d.setRegion(region.getSelectedItem().toString());
        d.setCiudad(ciudad.getSelectedItem().toString());

        Intent intent = new Intent(getActivity(), SelectorDireccionMapaCobertura.class);
        intent.putExtra("editar", false);
        intent.putExtra("dire",d);
        startActivity(intent);
    }

}