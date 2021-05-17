package com.example.pataconf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pataconf.ui.informacion.InformacionFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import Modelo.ModeloVistaTramo;

public class ModeloVistaTramoAdapter extends RecyclerView.Adapter<ModeloVistaTramoAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaTramo> data;
    private View.OnClickListener listener;


    public ModeloVistaTramoAdapter(ArrayList<ModeloVistaTramo> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelotramo, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaTramo musica = data.get(position);

        System.out.println(musica.toString());

        holder.sector.setText(musica.getCobertura().getNombre());
        holder.horai.setText(musica.getTramoRetiro().getHorainicio());
        holder.horaf.setText(musica.getTramoRetiro().getHorafinal());
        holder.cupostotales.setText(String.valueOf(musica.getTramoRetiro().getCupos()) + " cupos");
        holder.cuposdisponibles.setText(String.valueOf(musica.getTramoRetiro().getCuposdisponibles()) + " cupos disponibles");
        holder.estado.setChecked(musica.getTramoRetiro().isEstado());



        holder.estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                musica.getTramoRetiro().setEstado(isChecked);
                System.out.println("tramoretiro: " + musica.getTramoRetiro().toString());
                db.collection("tramoretiro").document(musica.getTramoRetiro().getId())
                        .set(musica.getTramoRetiro())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("correcto");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("error");
                            }
                        });

                System.out.println(isChecked + " is ding checked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView horai;
        TextView horaf;
        TextView sector;
        TextView cupostotales;
        TextView cuposdisponibles;
        Switch estado;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            sector = (TextView) itemView.findViewById(R.id.sector);
            horai = (TextView) itemView.findViewById(R.id.horai);
            horaf = (TextView) itemView.findViewById(R.id.horaf);
            cupostotales = (TextView) itemView.findViewById(R.id.cupostotales);
            cuposdisponibles = (TextView) itemView.findViewById(R.id.cuposdisponibles);
            estado = (Switch) itemView.findViewById(R.id.switch1);
        }

    }







}