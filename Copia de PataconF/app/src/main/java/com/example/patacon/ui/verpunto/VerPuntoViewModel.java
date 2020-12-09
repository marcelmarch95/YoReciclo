package com.example.patacon.ui.verpunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class VerPuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerPuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}