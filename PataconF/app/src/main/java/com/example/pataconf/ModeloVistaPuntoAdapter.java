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

import Modelo.ModeloVistaPunto;

public class ModeloVistaPuntoAdapter extends RecyclerView.Adapter<ModeloVistaPuntoAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaPunto> data;
    private View.OnClickListener listener;


    public ModeloVistaPuntoAdapter(ArrayList<ModeloVistaPunto> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelopunto, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaPunto musica = data.get(position);

        //holder.imgMusica.setImageDrawable(LoadImageFromWebOperations(musica.getFoto()));
        holder.direccion.setText(musica.getDireccion());
        holder.sector.setText(musica.getSector());
        holder.eliminar.setText(musica.getId() + "\n" + "E");
        holder.editar.setText(musica.getId() + "\n" + "ED");

        if (musica.isIsplastico()==false) {
            holder.imgp.setVisibility(View.INVISIBLE);
            holder.imgp.getLayoutParams().height = 0;
            holder.imgp.getLayoutParams().width = 0;
            holder.imgp.requestLayout();
        }
        if (musica.isIslatas()==false){
            holder.imgl.setVisibility(View.INVISIBLE);
            holder.imgl.getLayoutParams().height = 0;
            holder.imgl.getLayoutParams().width = 0;
            holder.imgl.requestLayout();
        }
        if (musica.isIsvidrio()==false){
            holder.imgv.setVisibility(View.INVISIBLE);
            holder.imgv.getLayoutParams().height = 0;
            holder.imgv.getLayoutParams().width = 0;
            holder.imgv.requestLayout();
        }


        // show The Image in a ImageView
        //new DownloadImageTask(holder.imgMusica).execute(musica.getFoto());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgp;
        ImageView imgl;
        ImageView imgv;
        Button eliminar;
        Button editar;
        TextView direccion;
        TextView sector;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            imgp = (ImageView) itemView.findViewById(R.id.imgp);
            imgl = (ImageView) itemView.findViewById(R.id.imgl);
            imgv = (ImageView) itemView.findViewById(R.id.imgv);
            eliminar = (Button) itemView.findViewById(R.id.eliminar);
            editar = (Button) itemView.findViewById(R.id.editar);
            eliminar.setOnClickListener(lis);
            editar.setOnClickListener(lis);
            eliminar.setTextColor(0xFF0000);
            editar.setTextColor(0xFF0000);
            direccion = (TextView) itemView.findViewById(R.id.direccion);
            sector = (TextView) itemView.findViewById(R.id.sector);
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
                InputStream in = new java.net.URL(urldisplay).openStream();
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