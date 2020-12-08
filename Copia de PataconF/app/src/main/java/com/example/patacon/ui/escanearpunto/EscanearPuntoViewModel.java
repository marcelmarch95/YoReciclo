package com.example.patacon.ui.escanearpunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EscanearPuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EscanearPuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}