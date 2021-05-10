package com.example.workshop8.ui.products;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class Product extends ViewModel {

    private int ProductId;
    private String ProdName;

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return ProdName;
    }
}