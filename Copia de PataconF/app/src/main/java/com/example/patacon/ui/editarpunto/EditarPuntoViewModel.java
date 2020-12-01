package com.example.patacon.ui.editarpunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EditarPuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditarPuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}