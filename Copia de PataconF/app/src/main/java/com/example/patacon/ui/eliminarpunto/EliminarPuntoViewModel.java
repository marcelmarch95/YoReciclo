package com.example.patacon.ui.eliminarpunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EliminarPuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EliminarPuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}