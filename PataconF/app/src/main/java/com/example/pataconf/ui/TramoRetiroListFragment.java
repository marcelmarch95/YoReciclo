package com.example.pataconf.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pataconf.ModeloVistaPuntoAdapter;
import com.example.pataconf.ModeloVistaTramoAdapter;
import com.example.pataconf.PerfilComerciante;
import com.example.pataconf.R;
import com.example.pataconf.ui.editarpunto.EditarPuntoFragment;
import com.example.pataconf.ui.eliminarpunto.EliminarPuntoFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListFragment;
import com.example.pataconf.ui.optionproducts.OptionsPuntosListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import Modelo.Cobertura;
import Modelo.ModeloVistaPunto;
import Modelo.ModeloVistaTramo;


public class TramoRetiroListFragment extends Fragment implements View.OnClickListener {

    private Spinner scategoria;
    private OptionsPuntosListViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaTramoAdapter adapter;
    private Button back;
    private PerfilComerciante padre;
    private Spinner categoria;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaTramo> tramos;
    private Button volver;
    private Button nuevo;
    private FirebaseAuth mAuth;
    private TextView tvtitulo;
    private LinearLayout layo;
    String dia, mes, ano;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listramoretiro, container, false);

        padre = (PerfilComerciante) getActivity();
        //padre.getSupportActionBar().hide();
        padre.getSupportActionBar().setTitle("Listado Tramos de Retiro");

        nuevo = root.findViewById(R.id.nuevo);
        nuevo.setOnClickListener(this);

        tvtitulo = root.findViewById(R.id.tvtitulo);

        dia = getArguments().getSerializable("dia").toString();
        mes = getArguments().getSerializable("mes").toString();
        ano = getArguments().getSerializable("ano").toString();

        if (dia.length()<2)
            dia = "0"+dia;
        String[] meses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        Integer mess = Integer.valueOf(mes);
        String me = meses[mess];

        tvtitulo.setText("Día " + dia + " de " + me + " del " + ano);

        ArrayList<ModeloVistaTramo> data = (ArrayList<ModeloVistaTramo>) getArguments().getSerializable("tramos");
        Collections.sort(data);

        this.tramos = data;

        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaTramoAdapter(data, this);
        rvMusicas.setAdapter(adapter);

        this.volver = root.findViewById(R.id.volver);
        this.volver.setOnClickListener(this);

        setHasOptionsMenu(true);

        if (data.isEmpty()){
            layo = root.findViewById(R.id.layo);
            ImageView image = new ImageView(getContext());
            image.setImageResource(R.drawable.empty);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200, 200);
            layoutParams.gravity= Gravity.CENTER;
            layoutParams.topMargin = 30;
            image.setLayoutParams(layoutParams);
            image.requestLayout();
            TextView tv = new TextView(getContext());
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setText("NO HAY TRAMOS REGISTRADOS EN LA FECHA");
            layo.addView(tv);
            layo.addView(image);
        }

        return root;
    }

    @Override
    public void onClick(View view) {

        if (view==this.volver){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new MiCalendarioFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else if (view==this.nuevo){
            Bundle bundle = new Bundle();
            bundle.putSerializable("dia", dia);
            bundle.putSerializable("mes", mes);
            bundle.putSerializable("ano", ano);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            ArrayList<Cobertura> coberturas = new ArrayList<>();
            db = FirebaseFirestore.getInstance();

            db.collection("cobertura")
                    .whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document2 : task.getResult()) {
                                    Cobertura cb = document2.toObject(Cobertura.class);
                                    cb.setId(document2.getId());
                                    coberturas.add(cb);
                                }

                                bundle.putSerializable("coberturas", coberturas);

                                Fragment lp = new AgregarFechaCalendar();
                                lp.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();


                            } else {

                            }
                        }
                    });


        }

        else if (view == this.back){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment lp = new OptionsPuntosListFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }

        else {
            Button bt = (Button) view;
            System.out.println("Click en el punto con id: " + bt.getText());

            List<String> salida = new ArrayList<>();

            StringTokenizer arr = new StringTokenizer(bt.getText().toString(), "\n");
            while(arr.hasMoreTokens()){
                salida.add(arr.nextToken());
            }

            String tipo = salida.get(1);
            String idpunto = salida.get(0);

            System.out.println("tipo: " + tipo);
            System.out.println("idpunto: " + idpunto);

            ModeloVistaTramo puntoe = new ModeloVistaTramo();

            if (tipo.compareTo("E")==0){

                for (ModeloVistaTramo p: tramos){
                    if (p.getTramoRetiro().getId().compareTo(idpunto)==0){
                        puntoe = p;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("punto", puntoe);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new EliminarPuntoFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }

            if (tipo.compareTo("ED")==0){

                for (ModeloVistaTramo p: tramos){
                    if (p.getTramoRetiro().getId().compareTo(idpunto)==0){
                        puntoe = p;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("punto", puntoe);

                System.out.println("Punto antes de entrar a editar ." + puntoe.getTramoRetiro().getId());

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment lp = new EditarPuntoFragment();
                lp.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                fragmentTransaction.commit();
            }
        }

        /*else {
            CardView cv = (CardView) view;
            TextView tv = (TextView) view.findViewById(R.id.accion);
            if (tv.getText().toString().compareTo("Ver Puntos")==0){

            }
        }*/





    }



}