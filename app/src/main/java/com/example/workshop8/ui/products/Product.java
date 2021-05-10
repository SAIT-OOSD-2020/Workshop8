package com.example.workshop8.ui.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Product extends ViewModel {

    private int ProductId;
    private String ProdName;

    private MutableLiveData<String> mText;

    public Product() {
        mText = new MutableLiveData<>();
        mText.setValue("This is products fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}