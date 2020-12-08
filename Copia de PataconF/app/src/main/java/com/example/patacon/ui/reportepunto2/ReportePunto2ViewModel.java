package com.example.patacon.ui.reportepunto2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ReportePunto2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportePunto2ViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}