package com.example.pataconf;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import Modelo.Notificacion;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {
    private List<Notificacion> items;
    private View.OnClickListener listener;

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView title;
        public TextView stitle;
        public String latitud, longitud;
        private Button mapButton;
        private TextView textHora;
        private TextView textFecha;
        private Button detalleButton;
        private View.OnClickListener listener;
        private LinearLayout layoutFecha;
        private LinearLayout layoutHora;
        private LinearLayout limagen;

        public NotificacionViewHolder(View v, View.OnClickListener lis) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.img);
            title = (TextView) v.findViewById(R.id.keyy);
            stitle = (TextView) v.findViewById(R.id.stitle);
            mapButton = (Button) v.findViewById(R.id.mapButton);
            detalleButton = (Button) v.findViewById(R.id.detalleButton);
            limagen = (LinearLayout) v.findViewById(R.id.limagen);

            textFecha = (TextView) v.findViewById(R.id.textFecha);
            textHora = (TextView) v.findViewById(R.id.textHora);

            layoutFecha = (LinearLayout) v.findViewById(R.id.layoutFecha);
            layoutHora = (LinearLayout) v.findViewById(R.id.layoutHora);

            mapButton.setTextColor(0xFF0000);
            detalleButton.setTextColor(0xFF0000);

            mapButton.setOnClickListener(lis);
            detalleButton.setOnClickListener(lis);
        }
    }

    public NotificacionAdapter(List<Notificacion> items, View.OnClickListener lis) {
        this.listener = lis;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_car_layout, viewGroup, false);
        return new NotificacionViewHolder(v, this.listener);
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder viewHolder, int i) {

        viewHolder.imagen.setImageResource(items.get(i).getImagen());
        System.out.println("iTEM ACTUAL is not: " + items.get(i).isNot());
        System.out.println("iTEM ACTUAL is planned: " + items.get(i).getPlanned());

        if (items.get(i).isNot()==false){
            if (items.get(i).getPlanned()==0){
                ViewGroup.LayoutParams params = viewHolder.layoutHora.getLayoutParams();
                params.height = 0;
                params.width = 0;

                viewHolder.layoutHora.setLayoutParams(params);
                viewHolder.layoutFecha.setLayoutParams(params);
                viewHolder.layoutHora.setVisibility(View.INVISIBLE);
                viewHolder.layoutFecha.setVisibility(View.INVISIBLE);
            }
            else {
                ViewGroup.LayoutParams params = viewHolder.limagen.getLayoutParams();
                params.width = 10;
                viewHolder.limagen.setLayoutParams(params);

                viewHolder.mapButton.setVisibility(View.INVISIBLE);

                StringTokenizer st = new StringTokenizer(items.get(i).getHora(), ":");
                String horaNueva = "";
                int contador = 0;

                while (st.hasMoreTokens()){
                    String t = st.nextToken().toString();
                    if (t.length()==1){
                        t = "0" + t;
                    }
                    if (contador==0){
                        horaNueva = horaNueva+t+":";
                        contador++;
                    }
                    else {
                        horaNueva = horaNueva+t;
                    }
                }

                Calendar now2 = Calendar.getInstance();

                String mes2 = String.valueOf((now2.get(Calendar.MONTH)+1));
                System.out.println("mes antes: " + mes2);

                if (Integer.valueOf(mes2)<10){
                    mes2 = "0"+mes2;
                }

                String fech2 = now2.get(Calendar.DAY_OF_MONTH) + "/" + mes2 + "/" + now2.get(Calendar.YEAR);
                now2.roll(Calendar.DAY_OF_MONTH, 1);
                String mañana2 = (now2.get(Calendar.DAY_OF_MONTH)) + "/" + mes2 + "/" + now2.get(Calendar.YEAR);

                now2.roll(Calendar.DAY_OF_MONTH, -2);
                String ayer = (now2.get(Calendar.DAY_OF_MONTH)) + "/" + mes2 + "/" + now2.get(Calendar.YEAR);

                viewHolder.textHora.setText(horaNueva);
                Calendar now = Calendar.getInstance();

                String mes = String.valueOf((now.get(Calendar.MONTH)+1));
                System.out.println("mes antes: " + mes);

                if (Integer.valueOf(mes)<10){
                    mes = "0"+mes;
                }

                String fech = now.get(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + now.get(Calendar.YEAR);
                now.roll(Calendar.DAY_OF_MONTH, 1);
                String mañana = (now.get(Calendar.DAY_OF_MONTH)) + "/" + mes + "/" + now.get(Calendar.YEAR);

                System.out.println("Hoy: " + fech);
                System.out.println("Mañana: " + mañana);

                if (ayer.compareTo(items.get(i).getFecha())==0){
                    viewHolder.textFecha.setText("Ayer");
                }
                else if (fech.compareTo(items.get(i).getFecha())==0){
                    viewHolder.textFecha.setText("Hoy");
                }
                else if (mañana.compareTo(items.get(i).getFecha())==0){
                    viewHolder.textFecha.setText("Mañana");
                }
                else {
                    viewHolder.textFecha.setText(items.get(i).getFecha());
                }
                viewHolder.imagen.setVisibility(View.INVISIBLE);
            }
        }

        if (items.get(i).getDestinoTexto() == null){
            viewHolder.title.setText(items.get(i).getTitulo());
            viewHolder.stitle.setText(items.get(i).getDetalle());
            viewHolder.mapButton.setVisibility(View.INVISIBLE);
            viewHolder.detalleButton.setVisibility(View.INVISIBLE);

            ViewGroup.LayoutParams params = viewHolder.limagen.getLayoutParams();
            params.width = 10;
            viewHolder.limagen.setLayoutParams(params);

            viewHolder.mapButton.setVisibility(View.INVISIBLE);

            StringTokenizer st = new StringTokenizer(items.get(i).getHora(), ":");
            String horaNueva = "";
            int contador = 0;

            while (st.hasMoreTokens()){
                String t = st.nextToken().toString();
                if (t.length()==1){
                    t = "0" + t;
                }
                if (contador==0){
                    horaNueva = horaNueva+t+":";
                    contador++;
                }
                else {
                    horaNueva = horaNueva+t;
                }
            }

            viewHolder.textHora.setText(horaNueva);

            Calendar now = Calendar.getInstance();

            String mes = String.valueOf((now.get(Calendar.MONTH)+1));
            System.out.println("mes antes: " + mes);

            if (Integer.valueOf(mes)<10){
                mes = "0"+mes;
            }

            String fech = now.get(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + now.get(Calendar.YEAR);
            now.roll(Calendar.DAY_OF_MONTH, 1);
            String mañana = (now.get(Calendar.DAY_OF_MONTH)) + "/" + mes + "/" + now.get(Calendar.YEAR);

            now.roll(Calendar.DAY_OF_MONTH, -2);
            String ayer = (now.get(Calendar.DAY_OF_MONTH)) + "/" + mes + "/" + now.get(Calendar.YEAR);

            System.out.println("Hoy: " + fech);
            System.out.println("Mañana: " + mañana);
            System.out.println("Ayer: " + ayer);

            if (fech.compareTo(items.get(i).getFecha())==0){
                viewHolder.textFecha.setText("Hoy");
            }
            else if (mañana.compareTo(items.get(i).getFecha())==0){
                viewHolder.textFecha.setText("Mañana");
            }
            else if (ayer.compareTo(items.get(i).getFecha())==0){
                viewHolder.textFecha.setText("Ayer");
            }
            else {
                viewHolder.textFecha.setText(items.get(i).getFecha());
            }
            viewHolder.imagen.setVisibility(View.INVISIBLE);

        }
        else {
            String id = items.get(i).getId();
            viewHolder.mapButton.setText(id + "\n" + "M" + "\n" + items.get(i).getUbicacionActual());
            viewHolder.detalleButton.setText(id +  "\n" + "D");
            viewHolder.title.setText("Destino: " + items.get(i).getDestinoTexto());
            viewHolder.stitle.setText("Patente: " +String.valueOf(items.get(i).getPatente()));
        }

    }


}
