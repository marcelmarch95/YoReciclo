package com.example.patacon.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.informacion.InformacionFragment;

import java.util.ArrayList;

import Modelo.Direccion;
import Modelo.TramoRetiro;

public class DeclaracionFragment extends Fragment implements View.OnClickListener {


    private TramoRetiro tramoseleccionado;
    private TextView fecha;
    private TextView tramo;
    private Button masplastico;
    private Button menosplastico;
    private Button maslatas;
    private Button menoslatas;
    private Button masvidrio;
    private Button menosvidrio;
    private Button mascarton;
    private Button menoscarton;
    private TextView contadorplastico;
    private TextView contadorlatas;
    private TextView contadorvidrio;
    private TextView contadorcarton;
    private int totplastico = 0;
    private int totlatas = 0;
    private int totvidrio = 0;
    private int totcarton = 0;
    private Button botonvolver;
    private Button botonsiguiente;
    private Direccion direccion;

    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_declaracion, container, false);

        tramoseleccionado = (TramoRetiro) getArguments().getSerializable("tramo");
        direccion = (Direccion) getArguments().getSerializable("direccion");

        fecha = root.findViewById(R.id.fecha);
        tramo = root.findViewById(R.id.tramo);

        masplastico = root.findViewById(R.id.masplastico);
        menosplastico = root.findViewById(R.id.menosplastico);
        maslatas = root.findViewById(R.id.maslata);
        menoslatas = root.findViewById(R.id.menoslata);
        masvidrio = root.findViewById(R.id.masvidrio);
        menosvidrio = root.findViewById(R.id.menosvidrio);
        mascarton = root.findViewById(R.id.mascarton);
        menoscarton = root.findViewById(R.id.menoscarton);

        contadorplastico = root.findViewById(R.id.contadorplastico);
        contadorlatas = root.findViewById(R.id.contadorlata);
        contadorvidrio = root.findViewById(R.id.contadorvidrio);
        contadorcarton = root.findViewById(R.id.contadorcarton);

        botonsiguiente = root.findViewById(R.id.btnsiguiente);
        botonvolver = root.findViewById(R.id.btnvolver);

        botonsiguiente.setOnClickListener(this);
        botonvolver.setOnClickListener(this);

        masplastico.setOnClickListener(this);
        menosplastico.setOnClickListener(this);
        maslatas.setOnClickListener(this);
        menoslatas.setOnClickListener(this);
        masvidrio.setOnClickListener(this);
        menosvidrio.setOnClickListener(this);
        mascarton.setOnClickListener(this);
        menoscarton.setOnClickListener(this);

        fecha.setText(tramoseleccionado.getFecha());
        tramo.setText("Tramo " + tramoseleccionado.getHorainicio() + " - " + tramoseleccionado.getHorafinal());

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Declaración de Residuos");



        return root;
    }




    @Override
    public void onClick(View view) {

        if (view==this.botonsiguiente){
            if (totplastico==0 && totvidrio==0 && totcarton==0 && totlatas==0){
                return;
            }
            else {
                Fragment lp = new AgregarFotoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("tramo",tramoseleccionado);
                bundle.putSerializable("direccion", direccion);
                bundle.putSerializable("lata",totlatas);
                bundle.putSerializable("plastico",totplastico);
                bundle.putSerializable("vidrio", totvidrio);
                bundle.putSerializable("carton",totcarton);
                lp.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();

                lp.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }
        else if (view == this.botonvolver){

        }

        else if (view == this.masplastico){
            totplastico++;
            contadorplastico.setText(String.valueOf(totplastico));
        }
        else if (view == this.menosplastico && totplastico>0){
            totplastico--;
            contadorplastico.setText(String.valueOf(totplastico));

        }
        else if (view == this.mascarton){
            totcarton++;
            contadorcarton.setText(String.valueOf(totcarton));
        }
        else if (view == this.menoscarton && totcarton>0){
            totcarton--;
            contadorcarton.setText(String.valueOf(totcarton));
        }
        else if (view == this.masvidrio){
            totvidrio++;
            contadorvidrio.setText(String.valueOf(totvidrio));
        }
        else if (view == this.menosvidrio && totvidrio>0){
            totvidrio--;
            contadorvidrio.setText(String.valueOf(totvidrio));
        }
        else if (view == this.maslatas){
            totlatas++;
            contadorlatas.setText(String.valueOf(totlatas));
        }
        else if (view == this.menoslatas && totlatas>0){
            totlatas--;
            contadorlatas.setText(String.valueOf(totlatas));
        }
    }



}