package com.example.patacon.ui;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.patacon.Informacion;
import com.example.patacon.R;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Modelo.Cobertura;
import Modelo.TramoRetiro;

public class AgregarFechaCalendar extends Fragment implements View.OnClickListener {

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    CalendarView simpleCalendarView;
    private Button[] btn = new Button[6];
    private Button fecinicio;
    private Button fecfinal;
    private int[] btn_id = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6};
    private String dia;
    private String mes = "1";
    private String ano;
    private String hora1;
    private String minuto1;
    private String hora2;
    private String minuto2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private TextView hor1;
    private TextView hor2;
    private TextView tvfecha;

    private Button btn_unfocus;
    private Button agregar;
    private int cupos = 0;
    private ArrayList<Cobertura> coberturas = new ArrayList<>();
    private Spinner spinner;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_agregarfecha, container, false);

        fecinicio = root.findViewById(R.id.fecinicio);
        fecfinal = root.findViewById(R.id.fecfinal);

        fecinicio.setOnClickListener(this);
        fecfinal.setOnClickListener(this);

        agregar = root.findViewById(R.id.agrefecha);
        agregar.setOnClickListener(this);

        hor1 = root.findViewById(R.id.hor1);
        hor2 = root.findViewById(R.id.hor2);

        tvfecha = root.findViewById(R.id.tvfecha);

        dia = getArguments().getSerializable("dia").toString();
        mes = getArguments().getSerializable("mes").toString();
        ano = getArguments().getSerializable("ano").toString();
        coberturas = (ArrayList<Cobertura>) getArguments().getSerializable("coberturas");
        List<String> array = new ArrayList<String>();

        for (int i=0; i<coberturas.size(); i++){
            array.add(coberturas.get(i).getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, array);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) root.findViewById(R.id.cobertura);
        spinner.setAdapter(adapter);

        String[] meses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        Integer mess = Integer.valueOf(mes);
        String me = meses[mess];


        String di = dia;
        if (di.length()<2)
            di = "0"+dia;


        tvfecha.setText(di + " de " + me + " del " + ano);

        for(int i = 0; i < btn.length; i++){
            System.out.println("error en btn id : " + btn_id[i]);
            btn[i] = (Button) root.findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.WHITE);
            btn[i].setOnClickListener(this);
        }

        btn_unfocus = btn[1];

        return root;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.agrefecha:
                validarCampos();
                break;

            case R.id.fecinicio:
                obtenerHora(true);
                break;

            case R.id.fecfinal:
                obtenerHora(false);
                break;

            case R.id.btn1 :
                setFocus(btn_unfocus, btn[0]);
                cupos = 1;
                break;

            case R.id.btn2 :
                setFocus(btn_unfocus, btn[1]);
                cupos = 2;
                break;

            case R.id.btn3 :
                setFocus(btn_unfocus, btn[2]);
                cupos = 3;
                break;

            case R.id.btn4 :
                setFocus(btn_unfocus, btn[3]);
                cupos = 4;
                break;

            case R.id.btn5 :
                setFocus(btn_unfocus, btn[4]);
                cupos = 5;
                break;

            case R.id.btn6 :
                setFocus(btn_unfocus, btn[5]);
                cupos = 6;
                break;
        }
    }

    private void validarCampos() {
        if (hora1 == null || hora2 == null || minuto1 == null || minuto2 == null || cupos==0){
            Toast.makeText(getContext(),"Error, completar información",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth = FirebaseAuth.getInstance();

            if (dia.charAt(0) == '0')
                dia = String.valueOf(dia.charAt(1));

            String fec = dia+"/"+(Integer.valueOf(mes)+1)+"/"+ano;
            String hoi = hora1+":"+minuto1;
            String hof = hora2+":"+minuto2;

            System.out.println("fec agrgar nueva: " + fec);

            TramoRetiro tr = new TramoRetiro();
            tr.setHorainicio(hoi);
            tr.setHorafinal(hof);
            tr.setFecha(fec);
            tr.setCupos(cupos);
            tr.setCuposdisponibles(cupos);
            tr.setUid(mAuth.getUid());

            String idc = coberturas.get(spinner.getSelectedItemPosition()).getId();
            tr.setIdCobertura(idc);

            db = FirebaseFirestore.getInstance();
            db.collection("tramoretiro")
                    .add(tr)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Fragment lp = new InformacionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("estado",true);
                            bundle.putSerializable("mensaje","Tramo creado correctamente");
                            bundle.putSerializable("dia",dia);
                            bundle.putSerializable("mes",mes);
                            bundle.putSerializable("ano", ano);
                            lp.setArguments(bundle);

                            FragmentManager fragmentManager = getFragmentManager();

                            Fragment fr = new InformacionFragment();
                            fr.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fr);
                            fragmentTransaction.commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent i = new Intent(getContext(), Informacion.class);
                            i.putExtra("result", false);
                            i.putExtra("mensaje", "Error al crear tramo");
                            startActivity(i);
                        }
                    });
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.WHITE);
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }

    private void obtenerHora(boolean inicio){
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                if (inicio) {
                    hor1.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                    hora1 = horaFormateada;
                    minuto1 = minutoFormateado;
                }
                else {
                    if (Integer.valueOf(horaFormateada)<Integer.valueOf(hora1)){
                        Toast.makeText(getContext(),"Hora debe ser mayor a la inicial",Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.valueOf(horaFormateada)==Integer.valueOf(hora1)){
                        if (Integer.valueOf(minutoFormateado)<=Integer.valueOf(minuto1)){
                            Toast.makeText(getContext(),"Hora debe ser mayor a la inicial",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            hor2.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                            hora2 = horaFormateada;
                            minuto2 = minutoFormateado;
                        }
                    }
                    else {
                        hor2.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        hora2 = horaFormateada;
                        minuto2 = minutoFormateado;
                    }

                }
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, true);

        recogerHora.show();
    }


}