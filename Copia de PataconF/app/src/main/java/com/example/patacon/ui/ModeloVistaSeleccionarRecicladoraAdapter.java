package com.example.patacon.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.patacon.R;

import java.util.ArrayList;

import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaRecicladora;

public class ModeloVistaSeleccionarRecicladoraAdapter extends RecyclerView.Adapter<ModeloVistaSeleccionarRecicladoraAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaRecicladora> data;
    private View.OnClickListener listener;


    public ModeloVistaSeleccionarRecicladoraAdapter(ArrayList<ModeloVistaRecicladora> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaSeleccionarRecicladoraAdapter(){
    }

    public ModeloVistaSeleccionarRecicladoraAdapter(ArrayList<ModeloVistaRecicladora> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener ls) {
        this.listener = ls;
    }

    public void setdata(ArrayList<ModeloVistaRecicladora> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloseleccionarrecicladora, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaRecicladora recicladora = data.get(position);

        holder.nombreempresa.setText(recicladora.getRecicladora().getNombreEmpresa());
        holder.localidad.setText(recicladora.getRecicladora().getDireccion());
        holder.seleccionar.setText(recicladora.getRecicladora().getId());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView nombreempresa;
        TextView localidad;
        Button seleccionar;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            nombreempresa = (TextView) itemView.findViewById(R.id.nombreempresa);
            localidad = (TextView) itemView.findViewById(R.id.localidad);
            seleccionar = (Button) itemView.findViewById(R.id.seleccionar);

            seleccionar.setOnClickListener(lis);
            seleccionar.setTextColor(0xFF0000);
        }

    }


}