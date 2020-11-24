package com.example.pataconf.ui.products;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ProductListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProductListViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}