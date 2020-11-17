package com.example.pataconf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import Modelo.ModeloVistaProducto;

public class ModeloVistaProductoAdapter extends RecyclerView.Adapter<ModeloVistaProductoAdapter.MusicaViewHolder> {

    private ArrayList<ModeloVistaProducto> data;
    private View.OnClickListener listener;

    public ModeloVistaProductoAdapter(ArrayList<ModeloVistaProducto> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloproducto, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaProducto musica = data.get(position);

        //holder.imgMusica.setImageDrawable(LoadImageFromWebOperations(musica.getFoto()));
        holder.tvNombre.setText(musica.getNombre());
        holder.categoria.setText(musica.getCategoria());
        holder.precio.setText("$ "+ String.valueOf(musica.getPrecio()));

        // show The Image in a ImageView
        new DownloadImageTask(holder.imgMusica).execute(musica.getFoto());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMusica;
        TextView tvNombre;
        TextView categoria;
        TextView precio;

        public MusicaViewHolder(View itemView) {
            super(itemView);
            imgMusica = (ImageView) itemView.findViewById(R.id.img_musica);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            categoria = (TextView) itemView.findViewById(R.id.propiedad);
            precio = (TextView) itemView.findViewById(R.id.precio);
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
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}