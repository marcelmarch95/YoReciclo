package com.example.pataconf.ui.vistareportepunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class VistaReportePuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VistaReportePuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}