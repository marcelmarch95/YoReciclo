package com.example.pataconf;

import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Modelo.ModeloVistaCobertura;

public class ModeloVistaCoberturaAdapter extends RecyclerView.Adapter<ModeloVistaCoberturaAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaCobertura> data;
    private View.OnClickListener listener;


    public ModeloVistaCoberturaAdapter(ArrayList<ModeloVistaCobertura> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaCoberturaAdapter(){
    }

    public ModeloVistaCoberturaAdapter(ArrayList<ModeloVistaCobertura> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener ls) {
        this.listener = ls;
    }

    public void setdata(ArrayList<ModeloVistaCobertura> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelocobertura, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaCobertura direccion = data.get(position);

        holder.direccion.setText(direccion.getCobertura().getNombre() + " " + direccion.getCobertura().getCiudad());
        holder.imgeditar.setText("ED;"+direccion.getCobertura().getId());
        holder.imgeliminar.setText("EL;"+direccion.getCobertura().getId());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView direccion;
        Button imgeditar;
        Button imgeliminar;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            imgeliminar = (Button) itemView.findViewById(R.id.imgeliminar);
            imgeditar = (Button) itemView.findViewById(R.id.imgeditar);

            imgeliminar.setOnClickListener(lis);
            imgeditar.setOnClickListener(lis);

            imgeliminar.setTextColor(0xFF0000);
            imgeditar.setTextColor(0xFF0000);
        }

    }


}