package com.example.workshop8.ui.products;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class Product extends ViewModel {

    private int ProductId;
    private String ProdName;

    public Product(int productId, String prodName) {
        ProductId = productId;
        ProdName = prodName;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return ProdName;
    }
}