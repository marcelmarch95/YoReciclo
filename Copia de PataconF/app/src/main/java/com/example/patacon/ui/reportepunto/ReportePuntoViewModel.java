package com.example.patacon.ui.reportepunto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ReportePuntoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportePuntoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}