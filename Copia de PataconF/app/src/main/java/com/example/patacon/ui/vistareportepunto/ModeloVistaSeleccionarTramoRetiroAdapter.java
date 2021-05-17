package com.example.patacon.ui.vistareportepunto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.patacon.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

import Modelo.ModeloVistaFechasRetiro;
import Modelo.TramoRetiro;

public class ModeloVistaSeleccionarTramoRetiroAdapter extends RecyclerView.Adapter<ModeloVistaSeleccionarTramoRetiroAdapter.MusicaViewHolder>  {

    private ArrayList<TramoRetiro> data;
    private View.OnClickListener listener;


    public ModeloVistaSeleccionarTramoRetiroAdapter(ArrayList<TramoRetiro> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaSeleccionarTramoRetiroAdapter(){
    }

    public ModeloVistaSeleccionarTramoRetiroAdapter(ArrayList<TramoRetiro> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener ls) {
        this.listener = ls;
    }

    public void setdata(ArrayList<TramoRetiro> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloseleccionartramoretiro, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        TramoRetiro tramo = data.get(position);

        holder.tramo.setText(tramo.getHorainicio() + " - " + tramo.getHorafinal());
        holder.disponibles.setText(tramo.getCuposdisponibles() +" cupos disponibles");
        holder.seleccionar.setText(tramo.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView tramo;
        TextView disponibles;
        Button seleccionar;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            tramo = (TextView) itemView.findViewById(R.id.tramo);
            disponibles = (TextView) itemView.findViewById(R.id.disponibles);
            seleccionar = (Button) itemView.findViewById(R.id.seleccionar);

            seleccionar.setOnClickListener(lis);
            seleccionar.setTextColor(0xFF0000);
        }

    }


}