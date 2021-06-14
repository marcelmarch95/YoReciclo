package com.example.pataconf.ui.cargando;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pataconf.R;
import com.example.pataconf.ui.optionreports.OptionsReportesListFragment;
import com.example.pataconf.ui.optionretiros.OptionsRetirosListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SinDocumentoFragment extends Fragment implements View.OnClickListener {

    public Button volver;
    public String mensa;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sindo, container, false);

        String msj =  getArguments().getString("mensaje");
        mensa = msj;

        volver = root.findViewById(R.id.volver);
        volver.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == this.volver){
            if (this.mensa.compareTo("retiro")==0){
                FragmentManager fragmentManager = getFragmentManager();

                Fragment lp = new OptionsRetirosListFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
            else if (this.mensa.compareTo("reporte")==0){
                FragmentManager fragmentManager = getFragmentManager();

                Fragment lp = new OptionsReportesListFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }
    }
}