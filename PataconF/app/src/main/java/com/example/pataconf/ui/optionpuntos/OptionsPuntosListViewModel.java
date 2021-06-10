package com.example.pataconf.ui.optionpuntos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class OptionsPuntosListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OptionsPuntosListViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}