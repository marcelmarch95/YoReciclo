package com.example.patacon.ui.puntosmapa;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PuntosMapaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PuntosMapaViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}