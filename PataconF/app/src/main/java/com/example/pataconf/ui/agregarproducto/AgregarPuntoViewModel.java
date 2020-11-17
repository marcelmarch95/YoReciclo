package com.example.pataconf.ui.agregarproducto;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AgregarProductoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AgregarProductoViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}