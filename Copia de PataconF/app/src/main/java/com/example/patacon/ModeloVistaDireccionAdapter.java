package com.example.patacon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import Modelo.ModeloVistaDireccion;
import Modelo.ModeloVistaReporte;

public class ModeloVistaDireccionAdapter extends RecyclerView.Adapter<ModeloVistaDireccionAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaDireccion> data;
    private View.OnClickListener listener;


    public ModeloVistaDireccionAdapter(ArrayList<ModeloVistaDireccion> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaDireccionAdapter(){
    }

    public ModeloVistaDireccionAdapter(ArrayList<ModeloVistaDireccion> data) {
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
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelodireccion, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaDireccion direccion = data.get(position);

        holder.direccion.setText(direccion.getDireccion().getCalle()+ " #" + direccion.getDireccion().getNumero());
        holder.imgeditar.setText("ED;"+direccion.getDireccion().getId());
        holder.imgeliminar.setText("EL;"+direccion.getDireccion().getId());

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