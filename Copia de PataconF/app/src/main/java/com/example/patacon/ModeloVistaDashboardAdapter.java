package com.example.patacon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modelo.ModeloVistaDashboard;

public class ModeloVistaDashboardAdapter extends RecyclerView.Adapter<ModeloVistaDashboardAdapter.MusicaViewHolder>{

    private ArrayList<ModeloVistaDashboard> data;

    public ModeloVistaDashboardAdapter(ArrayList<ModeloVistaDashboard> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelovista, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaDashboard musica = data.get(position);
        holder.imgMusica.setImageResource(musica.getImagen());
        holder.tvNombre.setText(musica.getNombre());
        holder.tvArtista.setText(musica.getArtista());
        holder.number.setText(String.valueOf(musica.getNumero()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMusica;
        TextView tvNombre;
        TextView tvArtista;
        TextView number;

        public MusicaViewHolder(View itemView) {
            super(itemView);
            imgMusica = (ImageView) itemView.findViewById(R.id.img_musica);
            tvNombre = (TextView) itemView.findViewById(R.id.direccion);
            tvArtista = (TextView) itemView.findViewById(R.id.tv_artista);
            number = (TextView) itemView.findViewById(R.id.number);
        }
    }
}