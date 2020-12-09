

package com.example.pataconf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modelo.ModeloOpcionesProducto;

public class ModeloVistaOpcionesProductoAdapter extends RecyclerView.Adapter<ModeloVistaOpcionesProductoAdapter.MusicaViewHolder> {

    private ArrayList<ModeloOpcionesProducto> data;
    private View.OnClickListener clickListener;


    public ModeloVistaOpcionesProductoAdapter(ArrayList<ModeloOpcionesProducto> data, View.OnClickListener lis) {
        this.clickListener = lis;
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opcionesproducto, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloOpcionesProducto musica = data.get(position);
        holder.imgMusica.setImageResource(musica.getFoto());
        holder.accion.setText(musica.getAccion());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMusica;
        TextView accion;


        public MusicaViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clickListener);
            imgMusica = (ImageView) itemView.findViewById(R.id.img_musica);
            accion = (TextView) itemView.findViewById(R.id.accion);
        }
    }
}