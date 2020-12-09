package com.example.patacon.ui.cargando;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.patacon.R;
import com.google.firebase.storage.OnProgressListener;

public class CargandoFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView porcentaje;
    private TextView texto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cargando, container, false);
        this.progressBar = root.findViewById(R.id.progressBar2);
        this.porcentaje = root.findViewById(R.id.porcentaje);
        this.porcentaje.setVisibility(View.INVISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.texto = root.findViewById(R.id.texto);
        return root;
    }

    public void actualizarCarga(int carga){
        this.progressBar.setVisibility(View.VISIBLE);
        this.porcentaje.setVisibility(View.VISIBLE);
        this.porcentaje.setText(String.valueOf(carga)+"%");
        this.texto.setText("Enviando Reporte");
        this.progressBar.setProgress(carga);
        this.progressBar.requestLayout();
    }
}