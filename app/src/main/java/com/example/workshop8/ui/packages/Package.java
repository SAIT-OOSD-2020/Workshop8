package com.example.workshop8.ui.packages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Timestamp;

public class Package extends ViewModel {

    private int PackageId;
    private String PkgName;
    private Timestamp PkgStartDate;
    private Timestamp PkgEndDate;
    private String PkgDesc;
    private double PkgBasePrice;
    private double PkgAgencyCommission;

    private MutableLiveData<String> mText;

    public Package() {
        mText = new MutableLiveData<>();
        mText.setValue("This is packages fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}