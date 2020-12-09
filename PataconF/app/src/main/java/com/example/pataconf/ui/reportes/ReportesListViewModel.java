package com.example.pataconf.ui.reportes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ReportesListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportesListViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}