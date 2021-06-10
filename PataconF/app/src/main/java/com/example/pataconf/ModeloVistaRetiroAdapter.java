package com.example.pataconf;

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
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import Modelo.ModeloVistaReporte;
import Modelo.ModeloVistaRetiro;

public class ModeloVistaRetiroAdapter extends RecyclerView.Adapter<ModeloVistaRetiroAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaRetiro> data;
    private View.OnClickListener listener;


    public ModeloVistaRetiroAdapter(ArrayList<ModeloVistaRetiro> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloretiro, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaRetiro retiro = data.get(position);

        //holder.imgMusica.setImageDrawable(LoadImageFromWebOperations(musica.getFoto()));
        holder.direccion.setText(retiro.getDireccion().getCalle() + " - " + retiro.getDireccion().getCiudad() );

        System.out.println("retiro tramo: " + retiro.getTramo().toString());
        holder.fecha.setText(retiro.getTramo().getFecha() + "  " + retiro.getTramo().getHorainicio() + " - " +  retiro.getTramo().getHorafinal());
        holder.generador.setText(retiro.getGenerador().getNombre() +  " " + retiro.getGenerador().getApellido());
        holder.ver.setText(retiro.getRetiro().getId());

        holder.imgmotivo.setImageResource(R.drawable.inexistente);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgmotivo;
        Button ver;
        TextView direccion;
        TextView generador;
        TextView fecha;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            imgmotivo = (ImageView) itemView.findViewById(R.id.imgmotivo);
            ver = (Button) itemView.findViewById(R.id.vereporte);
            ver.setOnClickListener(lis);
            ver.setTextColor(0xFF0000);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            generador = (TextView) itemView.findViewById(R.id.generador);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
        }

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }





    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                System.out.println("NO PUDE DESCARGAR LA IMG");
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            System.out.println("RESULTTTTTT: " + result);
            if (result == null){
                bmImage.setImageResource(R.drawable.pto);
            }
            else {
                bmImage.setImageBitmap(result);
            }

        }
    }
}