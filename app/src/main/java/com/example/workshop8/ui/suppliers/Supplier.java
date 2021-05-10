package com.example.workshop8.ui.suppliers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Supplier extends ViewModel {

    private int SupplierId;
    private String SupName;
    
    private MutableLiveData<String> mText;

    public Supplier() {
        mText = new MutableLiveData<>();
        mText.setValue("This is suppliers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}