package com.example.workshop8.ui.packages;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Timestamp;

public class Package extends ViewModel {

    private int PackageId;
    private String PkgName;
    private Date PkgStartDate;
    private Date PkgEndDate;
    private String PkgDesc;
    private double PkgBasePrice;
    private double PkgAgencyCommission;

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return PkgName;
    }
}