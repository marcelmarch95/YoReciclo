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

public class ModeloVistaReporteAdapter extends RecyclerView.Adapter<ModeloVistaReporteAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaReporte> data;
    private View.OnClickListener listener;


    public ModeloVistaReporteAdapter(ArrayList<ModeloVistaReporte> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloreporte, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaReporte reporte = data.get(position);

        //holder.imgMusica.setImageDrawable(LoadImageFromWebOperations(musica.getFoto()));
        holder.direccion.setText(reporte.getPunto().getDireccion());

        holder.fecha.setText("Reporte creado el " + reporte.getReporte().getFecha() + " a las " + reporte.getReporte().getHora());
        holder.ver.setText(reporte.getReporte().getIdReporte().toString());

        if (reporte.getReporte().getMotivo().compareTo("inexistente")==0) {
            holder.motivo.setText("Punto Inexistente");
            holder.imgmotivo.setImageResource(R.drawable.inexistente);
        }
        if (reporte.getReporte().getMotivo().compareTo("lleno")==0) {
            holder.motivo.setText("Punto Lleno");
            holder.imgmotivo.setImageResource(R.drawable.full);
        }
        if (reporte.getReporte().getMotivo().compareTo("otro")==0) {
            holder.motivo.setText("Otro Motivo");
            holder.imgmotivo.setImageResource(R.drawable.otro);
        }
        if (reporte.getReporte().getMotivo().compareTo("deteriorado")==0) {
            holder.motivo.setText("Punto Deteriorado");
            holder.imgmotivo.setImageResource(R.drawable.roto);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgmotivo;
        Button ver;
        TextView direccion;
        TextView motivo;
        TextView fecha;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            imgmotivo = (ImageView) itemView.findViewById(R.id.imgmotivo);
            ver = (Button) itemView.findViewById(R.id.vereporte);
            ver.setOnClickListener(lis);
            ver.setTextColor(0xFF0000);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            motivo = (TextView) itemView.findViewById(R.id.motivo);
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