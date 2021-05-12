package com.example.workshop8.ui.suppliers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class Supplier extends ViewModel {

    private int SupplierId;
    private String SupName;

    public Supplier(int supplierId, String supName) {
        SupplierId = supplierId;
        SupName = supName;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return SupName;
    }
}