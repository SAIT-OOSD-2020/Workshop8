package com.example.workshop8.ui.customers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Customer extends ViewModel {

    private int CustomerId;
    private String CustFirstName;
    private String CustLastName;
    private String CustAddress;
    private String CustCity;
    private String CustProv;
    private String CustPostal;
    private String CustCountry;
    private String CustHomePhone;
    private String CustBusPhone;
    private String CustEmail;
    private Integer AgentId;

    private MutableLiveData<String> mText;

    public Customer() {
        mText = new MutableLiveData<>();
        mText.setValue("This is customer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}