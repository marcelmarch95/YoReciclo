package com.example.pataconf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Modelo.Notificacion;
import Modelo.Usuario;
import Modelo.Viaje;


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