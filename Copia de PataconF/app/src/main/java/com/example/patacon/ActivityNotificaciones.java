package com.example.patacon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Modelo.Notificacion;

public class ActivityNotificaciones extends Fragment implements View.OnClickListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    public ActivityNotificaciones() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notificaciones, container, false);
        return inflater.inflate(R.layout.activity_notificaciones, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        List items = new ArrayList();
        Bundle args = getArguments();
        ArrayList<Notificacion> nots = (ArrayList<Notificacion>) args.getSerializable("nots");

        for (Notificacion n: nots){
            items.add(n);
            System.out.println("noti recibidia: " + n.toString());
        }

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this.getActivity());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new NotificacionAdapter(items, this);
        recycler.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        System.out.println("Click: " + v.getId());
    }
}