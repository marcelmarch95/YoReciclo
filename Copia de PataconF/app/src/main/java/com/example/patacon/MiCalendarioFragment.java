package com.example.patacon;

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
import android.widget.CalendarView;

import com.example.patacon.ui.TramoRetiroListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Modelo.Cobertura;
import Modelo.ModeloVistaTramo;
import Modelo.TramoRetiro;

public class MiCalendarioFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    CalendarView simpleCalendarView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<ModeloVistaTramo> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendario, container, false);
        simpleCalendarView = (CalendarView) root.findViewById(R.id.calender); // get the reference of CalendarView
        long selectedDate = simpleCalendarView.getDate(); // get selected date in milliseconds


        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                //Toast.makeText(getContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();


                System.out.println("agregar fecha calendario");
                FragmentManager fragmentManager = getFragmentManager();
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                db.collection("tramoretiro")
                        .whereEqualTo("uid", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        TramoRetiro p = document.toObject(TramoRetiro.class);
                                        ModeloVistaTramo pu = new ModeloVistaTramo();
                                        pu.setTramoRetiro(p);
                                        data.add(pu);
                                    }

                                    String f = String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                                    System.out.println("FFFFFFF: + " + f);

                                    ArrayList<ModeloVistaTramo> fin = new ArrayList<>();
                                    for (ModeloVistaTramo mv: data){
                                        if (mv.getTramoRetiro().getFecha().compareTo(f)==0){
                                            fin.add(mv);
                                        }
                                    }

                                    ArrayList<Cobertura> coberturas = new ArrayList<>();


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

                                                        for (ModeloVistaTramo p: fin){
                                                            for (Cobertura r: coberturas){
                                                                if (p.getTramoRetiro().getIdCobertura().compareTo(r.getId())==0){
                                                                    p.setCobertura(r);
                                                                }
                                                            }
                                                        }
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable("dia", String.valueOf(dayOfMonth));
                                                        bundle.putSerializable("mes", String.valueOf(month));
                                                        bundle.putSerializable("ano", String.valueOf(year));
                                                        bundle.putSerializable("tramos", fin);


                                                        Fragment lp = new TramoRetiroListFragment();
                                                        lp.setArguments(bundle);
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                                        fragmentTransaction.commit();

                                                    } else {

                                                    }
                                                }
                                            });




                                } else {

                                }
                            }
                        });


            }
        });

        return root;
    }


    @Override
    public void onClick(View view) {

    }


}