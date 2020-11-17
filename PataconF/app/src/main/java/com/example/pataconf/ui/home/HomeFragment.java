package com.example.pataconf.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toolbar;

import com.example.pataconf.ModeloVistaDashboardAdapter;
import com.example.pataconf.R;

import java.util.ArrayList;

import Modelo.ModeloVistaDashboard;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private ModeloVistaDashboardAdapter adapter;

    private ArrayList<ModeloVistaDashboard> dataSet() {
        ArrayList<ModeloVistaDashboard> data = new ArrayList<>();
        data.add(new ModeloVistaDashboard("Retiros", "Pendientes", R.drawable.pending, 7));
        data.add(new ModeloVistaDashboard("Retiros", "En curso", R.drawable.preparing, 1));
        data.add(new ModeloVistaDashboard("Retiros", "Finalizados", R.drawable.delivered, 12));
        //data.add(new ModeloVistaDashboard("Pedidos", "Finalizados", R.drawable.orders, 5));
        return data;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        getActivity().setTitle("Your title");



        rvMusicas = (RecyclerView) root.findViewById(R.id.rv_musicas);
        glm = new GridLayoutManager(getActivity(), 1);
        rvMusicas.setLayoutManager(glm);
        adapter = new ModeloVistaDashboardAdapter(dataSet());
        rvMusicas.setAdapter(adapter);
        return root;
    }
}