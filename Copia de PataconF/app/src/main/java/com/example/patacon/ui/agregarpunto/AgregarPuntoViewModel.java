package com.example.patacon.ui.agregarpunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AgregarPuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AgregarPuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}