package com.example.patacon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


public class LayoutHelp extends Fragment {


    private FragmentManager fragmentManager;
    private ProgressBar cargando;
    private Button volver;


    public LayoutHelp() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.layout_help, container, false);
        return inflater.inflate(R.layout.layout_help, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.volver = view.findViewById(R.id.buttonVolver);
        this.cargando = (ProgressBar) view.findViewById(R.id.progressBar);
        cargando.setVisibility(View.INVISIBLE);


        this.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityReporte fragmentReporte = new ActivityReporte();
                fragmentReporte.setFragmentManager(fragmentManager);
                final FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                transaction3.replace(R.id.main_container, fragmentReporte).commit();

            }

        });

    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }




}