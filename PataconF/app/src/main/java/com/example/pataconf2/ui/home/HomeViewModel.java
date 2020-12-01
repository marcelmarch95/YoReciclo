package com.example.pataconf.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pataconf.ModeloVistaDashboardAdapter;
import com.example.pataconf.R;

import java.util.ArrayList;

import Modelo.ModeloVistaDashboard;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public HomeViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}