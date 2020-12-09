package com.example.pataconf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;

import Modelo.Viaje;

public class ActivityDetalleViaje extends Fragment implements View.OnClickListener {

    private TextView destino;
    private TextView fecha;
    private TextView hora;
    private TextView envase;
    private TextView kg;
    private TextView chofer;
    private TextView rut;
    private TextView patente;
    private TextView carro;

    private ProgressBar progressBar;
    private TextView estado;
    private static boolean isTimerRunning;
    private static Timer timer;
    private static int elapsedTime = 0;
    private String numero;
    private TextView cron;
    private LinearLayout op2;
    private TextView viaje;
    private ActivityPrincipal papa;
    private Button volver;
    private String origenn;

    public ActivityDetalleViaje() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_detalleviaje, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        Bundle args = getArguments();
        Viaje viaje = (Viaje) args.getSerializable("viaje");

        this.destino = view.findViewById(R.id.tdestino);
        this.fecha = view.findViewById(R.id.tfecha);
        this.hora = view.findViewById(R.id.thora);

        this.chofer = view.findViewById(R.id.tchofer);
        this.rut = view.findViewById(R.id.trut);
        this.patente = view.findViewById(R.id.tpatente);
        this.carro = view.findViewById(R.id.tcarro);
        this.op2 = view.findViewById(R.id.op2);
        this.envase = view.findViewById(R.id.tenvase);
        this.kg = view.findViewById(R.id.tkg);

        this.destino.setText(viaje.getDestinoTexto());
        this.fecha.setText(viaje.getFecha());
        this.hora.setText(viaje.getHora());
        this.carro.setText(viaje.getCarro());
        this.patente.setText(viaje.getPatente());
        this.envase.setText(viaje.getEnvase());
        this.kg.setText(viaje.getKg());

        this.volver = view.findViewById(R.id.buttonVolver);
        this.volver.setOnClickListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("chofer").document(viaje.getIdChofer());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        chofer.setText(document.get("nombre").toString() + " " + document.get("apellido").toString());
                        numero = document.get("numero").toString();
                        System.out.println("numeroi: " + numero);
                    } else {

                    }
                } else {

                }
            }
        });

        rut.setText(viaje.getIdChofer());
        this.cron = view.findViewById(R.id.cron);
        this.estado = view.findViewById(R.id.testado);

        this.estado.setText("Despacho " + viaje.getEstado());
        LinearLayout opc2 = (LinearLayout) view.findViewById(R.id.op2);
        opc2.setOnClickListener(this);
        cron.setText("");

        if (viaje.getEstado().compareTo("Finalizado")==0){
            opc2.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.op2){
            try {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+numero)));
            }
            catch (Exception e){
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+56956844862")));
            }

        }
        else if (v == this.volver){
            if (this.origenn.compareTo("curso")==0)
                this.papa.actualizarFragmentCurso();
            else if (this.origenn.compareTo("finalizado")==0){
                this.papa.actualizarFragmentFinalizado();
            }
            else {
                this.papa.actualizarFragmentPlanificado();
            }
        }
    }


    public void setPapa(ActivityPrincipal a){
        this.papa = a;
    }

    public void setOrigen(String origen){
        this.origenn = origen;
    }

}