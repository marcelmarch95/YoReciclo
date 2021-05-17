package com.example.patacon.ui.vistareportepunto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.patacon.R;
import com.google.common.collect.ArrayTable;

import java.util.ArrayList;
import java.util.StringTokenizer;

import Modelo.ModeloVistaFechasRetiro;
import Modelo.ModeloVistaRecicladora;
import Modelo.TramoRetiro;

public class ModeloVistaSeleccionarFechaRetiroAdapter extends RecyclerView.Adapter<ModeloVistaSeleccionarFechaRetiroAdapter.MusicaViewHolder>  {

    private ArrayList<ModeloVistaFechasRetiro> data;
    private View.OnClickListener listener;


    public ModeloVistaSeleccionarFechaRetiroAdapter(ArrayList<ModeloVistaFechasRetiro> data, View.OnClickListener ls) {
        this.data = data;
        this.listener = ls;
    }

    public ModeloVistaSeleccionarFechaRetiroAdapter(){
    }

    public ModeloVistaSeleccionarFechaRetiroAdapter(ArrayList<ModeloVistaFechasRetiro> data) {
        this.data = data;
    }

    public void setListener(View.OnClickListener ls) {
        this.listener = ls;
    }

    public void setdata(ArrayList<ModeloVistaFechasRetiro> data) {
        this.data = data;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modeloseleccionarfecharetiro, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        ModeloVistaFechasRetiro fechas = data.get(position);

        int total = 0;

        for (TramoRetiro tr: fechas.getTramos()){
            total+=tr.getCuposdisponibles();
        }

        String fecha = fechas.getFecha();

        StringTokenizer stringTokenizer = new StringTokenizer(fecha,"/");

        ArrayList<String> partes = new ArrayList<>();
        while(stringTokenizer.hasMoreElements()){
            partes.add(stringTokenizer.nextToken());
        }

        String[] meses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        Integer mes = Integer.valueOf(partes.get(1));


        holder.dia.setText(partes.get(0));
        holder.mes.setText(meses[mes-1]);
        holder.disponibles.setText(total +" cupos disponibles");
        holder.seleccionar.setText(fechas.getFecha());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder{

        TextView dia;
        TextView mes;
        TextView disponibles;
        Button seleccionar;

        public MusicaViewHolder(View itemView, View.OnClickListener lis) {
            super(itemView);
            dia = (TextView) itemView.findViewById(R.id.dia);
            mes = (TextView) itemView.findViewById(R.id.mes);
            disponibles = (TextView) itemView.findViewById(R.id.disponibles);
            seleccionar = (Button) itemView.findViewById(R.id.seleccionar);

            seleccionar.setOnClickListener(lis);
            seleccionar.setTextColor(0xFF0000);
        }

    }


}