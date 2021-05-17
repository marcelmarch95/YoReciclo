package com.example.patacon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Modelo.ModeloVistaDireccion;

public class ModeloVistaSeleccionarDireccionAdapter extends RecyclerView.Adapter<ModeloVistaSeleccionarDireccionAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaDireccion> data;
    private View.OnClickListener listener;


    public ModeloVistaSeleccionarDireccionAdapter(ArrayList<ModeloVistaDireccion> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaSeleccionarDireccionAdapter(){
    }

    public ModeloVistaSeleccionarDireccionAdapter(ArrayList<ModeloVistaDireccion> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener ls) {
        this.listener = ls;
    }

    public void setdata(ArrayList<ModeloVistaDireccion> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloseleccionardireccion, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaDireccion direccion = data.get(position);

        holder.direccion.setText(direccion.getDireccion().getCalle()+ " #" + direccion.getDireccion().getNumero());
        holder.ciudad.setText(direccion.getDireccion().getCiudad());
        holder.seleccionar.setText(direccion.getDireccion().getId());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView direccion;
        TextView ciudad;
        Button seleccionar;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            ciudad = (TextView) itemView.findViewById(R.id.ciudad);
            seleccionar = (Button) itemView.findViewById(R.id.seleccionar);

            seleccionar.setOnClickListener(lis);
            seleccionar.setTextColor(0xFF0000);
        }

    }


}