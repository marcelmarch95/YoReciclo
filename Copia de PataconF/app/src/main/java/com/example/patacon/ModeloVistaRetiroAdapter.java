package com.example.patacon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

    public ModeloVistaRetiroAdapter() {
    }

    public ModeloVistaRetiroAdapter(ArrayList<ModeloVistaRetiro> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public ArrayList<ModeloVistaRetiro> getData() {
        return data;
    }

    public void setData(ArrayList<ModeloVistaRetiro> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloretiro, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaRetiro retiro = data.get(position);

        System.out.println("retiro direccion: " + retiro.getDireccion().toString()) ;

        if (retiro.getRetiro().getEstado().compareTo("solicitado")==0)
            holder.imgestado.setImageResource(R.drawable.pendi);
        else if (retiro.getRetiro().getEstado().compareTo("reparo")==0)
            holder.imgestado.setImageResource(R.drawable.rechazado);
        else if (retiro.getRetiro().getEstado().compareTo("rechazado")==0)
            holder.imgestado.setImageResource(R.drawable.rechazadotot);
        else if (retiro.getRetiro().getEstado().compareTo("cancelado")==0)
            holder.imgestado.setImageResource(R.drawable.dele);
        else if (retiro.getRetiro().getEstado().compareTo("aprobado")==0)
            holder.imgestado.setImageResource(R.drawable.aceptaod);
        else if (retiro.getRetiro().getEstado().compareTo("finalizado")==0)
            holder.imgestado.setImageResource(R.drawable.terminado);

        holder.veretiro.setText(retiro.getRetiro().getId());


        holder.empresa.setText(retiro.getDireccion().getCalle() + " #" + retiro.getDireccion().getNumero() + " " + retiro.getDireccion().getRegion());
        holder.fecha.setText("Retiro para el " + retiro.getTramo().getFecha());
        holder.hora.setText("Tramo " + retiro.getTramo().getHorainicio() + " - " + retiro.getTramo().getHorafinal());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView fecha;
        TextView hora;
        TextView empresa;
        ImageView imgestado;
        Button veretiro;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            hora = (TextView) itemView.findViewById(R.id.hora);
            empresa = (TextView) itemView.findViewById(R.id.empresa);
            imgestado = (ImageView) itemView.findViewById(R.id.imgestado);
            veretiro = (Button) itemView.findViewById(R.id.veretiro);
            veretiro.setOnClickListener(lis);
            veretiro.setTextColor(0xFF0000);
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