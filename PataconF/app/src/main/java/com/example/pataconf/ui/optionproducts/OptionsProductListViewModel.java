package com.example.pataconf.ui.optionproducts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class OptionsProductListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OptionsProductListViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}