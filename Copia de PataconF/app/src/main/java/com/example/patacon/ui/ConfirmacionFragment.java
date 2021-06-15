package com.example.patacon.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;

import java.io.ByteArrayOutputStream;

import Modelo.Direccion;
import Modelo.TramoRetiro;

public class ConfirmacionFragment extends Fragment implements View.OnClickListener {


    private TramoRetiro tramoseleccionado;
    private TextView fecha;
    private TextView tramo;

    private TextView ticarton;
    private TextView tilata;
    private TextView tividrio;
    private TextView tiplastico;

    private TextView tdireccion;
    private TextView tciudad;

    private ImageView foto;

    private int totplastico = 0;
    private int totlatas = 0;
    private int totvidrio = 0;
    private int totcarton = 0;
    private Button botonvolver;
    private Button botonsiguiente;
    private Direccion direccion;
    private View root;
    private Bitmap img;
    private String comentarios;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_confirmacion, container, false);

        tramoseleccionado = (TramoRetiro) getArguments().getSerializable("tramo");
        direccion = (Direccion) getArguments().getSerializable("direccion");
        totlatas = (int) getArguments().getSerializable("lata");
        totvidrio = (int) getArguments().getSerializable("vidrio");
        totcarton = (int) getArguments().getSerializable("carton");
        totplastico = (int) getArguments().getSerializable("plastico");
        comentarios = (String) getArguments().getSerializable("comentarios");

        foto = (ImageView) root.findViewById(R.id.imageView10);

        byte[] byteArray = getArguments().getByteArray("img");

        if (byteArray==null){
            foto.setImageResource(R.drawable.nofoto);
        }
        else {
            img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            foto.setImageBitmap(img);
        }


        fecha = root.findViewById(R.id.fecha);
        tramo = root.findViewById(R.id.tramo);

        tdireccion = root.findViewById(R.id.direccion);
        tciudad = root.findViewById(R.id.ciudad);

        tdireccion.setText(direccion.getCalle() + " #" + direccion.getNumero());
        tciudad.setText(direccion.getCiudad() + ", " + direccion.getRegion());

        ticarton = root.findViewById(R.id.totcarton);
        tilata = root.findViewById(R.id.totlata);
        tiplastico = root.findViewById(R.id.totplastico);
        tividrio = root.findViewById(R.id.totvidrio);

        ticarton.setText(String.valueOf(totcarton) + " unidades");
        tividrio.setText(String.valueOf(totvidrio) + " unidades");
        tilata.setText(String.valueOf(totlatas)  + " unidades");
        tiplastico.setText(String.valueOf(totplastico)  + " unidades");

        botonsiguiente = root.findViewById(R.id.btnsiguiente);
        botonvolver = root.findViewById(R.id.btnvolver);

        botonsiguiente.setOnClickListener(this);
        botonvolver.setOnClickListener(this);



        fecha.setText(tramoseleccionado.getFecha());
        tramo.setText("Tramo " + tramoseleccionado.getHorainicio() + " - " + tramoseleccionado.getHorafinal());

        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Confirmar Solicitud Retiro");



        return root;
    }




    @Override
    public void onClick(View view) {

        if (view==this.botonsiguiente){
            Bundle bundle = new Bundle();
            bundle.putSerializable("tramo",tramoseleccionado);
            bundle.putSerializable("direccion", direccion);
            bundle.putSerializable("lata",totlatas);
            bundle.putSerializable("plastico",totplastico);
            bundle.putSerializable("vidrio", totvidrio);
            bundle.putSerializable("carton",totcarton);
            bundle.putSerializable("comentarios",comentarios);

            Fragment lp = new SolicitarRetiroFragment();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            bundle.putByteArray("img", byteArray);
            lp.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
        else if (view == this.botonvolver){

        }


    }



}