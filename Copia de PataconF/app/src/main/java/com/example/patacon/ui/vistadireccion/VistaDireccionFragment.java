package com.example.patacon.ui.vistadireccion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.optionretiros.OptionsRetirosListFragment;

import Modelo.Direccion;

public class VistaDireccionFragment extends Fragment implements View.OnClickListener {

    private Button volver;
    private EditText esector;
    private EditText eregion;
    private EditText eciudad;
    private EditText ecalle;
    private EditText enumero;
    private TextView eindicaciones;
    private View root;
    private Direccion direccion;
    private static final int PICK_IMAGE = 100;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vistadireccion, container, false);

        direccion =  (Direccion) getArguments().get("direccion");

        ecalle = (EditText) root.findViewById(R.id.ecalle);
        enumero = (EditText) root.findViewById(R.id.enumero);
        eindicaciones = (EditText) root.findViewById(R.id.eindicaciones);
        eregion = (EditText) root.findViewById(R.id.eregion);
        eciudad = (EditText) root.findViewById(R.id.eciudad);
        esector = (EditText) root.findViewById(R.id.esector);

        ecalle.setText(direccion.getCalle());
        enumero.setText(direccion.getNumero());
        eindicaciones.setText(direccion.getIndicaciones());
        eregion.setText(direccion.getRegion());
        eciudad.setText(direccion.getCiudad());
        esector.setText(direccion.getSector());

        volver = (Button) root.findViewById(R.id.volver);
        volver.setOnClickListener(this);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Vista dirección");

        return root;
    }




    @Override
    public void onClick(View view) {
        if (view == this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsRetirosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
    }



}