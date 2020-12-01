package com.example.patacon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patacon.R;


public class ActivityCargando extends Fragment {




    public ActivityCargando() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_cargando, container, false);
        return inflater.inflate(R.layout.activity_cargando, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}