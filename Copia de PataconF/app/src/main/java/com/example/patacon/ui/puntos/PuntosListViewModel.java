package com.example.patacon.ui.puntos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PuntosListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PuntosListViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}