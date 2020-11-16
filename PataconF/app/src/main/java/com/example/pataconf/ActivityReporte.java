package com.example.pataconf;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActivityReporte extends Fragment implements View.OnClickListener {

    public View principal;
    private FragmentManager fragmentManager;


    public ActivityReporte() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.principal = view;
        LinearLayout opc1 = (LinearLayout) view.findViewById(R.id.op1);
        LinearLayout opc2 = (LinearLayout) view.findViewById(R.id.op2);
        LinearLayout opc3 = (LinearLayout) view.findViewById(R.id.op3);

        opc1.setOnClickListener(this);
        opc2.setOnClickListener(this);
        opc3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.op3){
            String dial = "tel:+56956844862";
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }

        if (v.getId()==R.id.op1){
            LayoutHelp fragment = new LayoutHelp();
            fragment.setFragmentManager(fragmentManager);
            final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
            transaction4.replace(R.id.main_container, fragment).commit();
        }

    }


    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}