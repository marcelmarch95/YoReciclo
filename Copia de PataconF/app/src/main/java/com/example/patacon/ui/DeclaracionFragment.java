package com.example.patacon.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;

import java.util.ArrayList;

import Modelo.TramoRetiro;

public class DeclaracionFragment extends Fragment implements View.OnClickListener {


    private TramoRetiro tramoseleccionado;
    private TextView fecha;
    private TextView tramo;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_declaracion, container, false);

        tramoseleccionado = (TramoRetiro) getArguments().getSerializable("tramo");

        fecha = root.findViewById(R.id.fecha);
        tramo = root.findViewById(R.id.tramo);

        fecha.setText(tramoseleccionado.getFecha());
        tramo.setText("Tramo " + tramoseleccionado.getHorainicio() + " - " + tramoseleccionado.getHorafinal());

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Declaración Residuos");



        return root;
    }




    @Override
    public void onClick(View view) {
    }



}