package com.example.workshop8.ui.suppliers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class Supplier extends ViewModel {

    private int SupplierId;
    private String SupName;

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return SupName;
    }
}