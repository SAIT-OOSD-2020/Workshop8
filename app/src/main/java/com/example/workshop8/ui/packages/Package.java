package com.example.workshop8.ui.packages;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.sql.Date;

public class Package extends ViewModel {

    private int PackageId;
    private String PkgName;
    private Date PkgStartDate;
    private Date PkgEndDate;
    private String PkgDesc;
    private BigInteger PkgBasePrice;
    private BigInteger PkgAgencyCommission;

    public Package(int packageId, String pkgName, Date pkgStartDate, Date pkgEndDate,
                   String pkgDesc, BigInteger pkgBasePrice, BigInteger pkgAgencyCommission) {
        PackageId = packageId;
        PkgName = pkgName;
        PkgStartDate = pkgStartDate;
        PkgEndDate = pkgEndDate;
        PkgDesc = pkgDesc;
        PkgBasePrice = pkgBasePrice;
        PkgAgencyCommission = pkgAgencyCommission;
    }

    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public String getPkgName() {
        return PkgName;
    }

    public void setPkgName(String pkgName) {
        PkgName = pkgName;
    }

    public Date getPkgStartDate() {
        return PkgStartDate;
    }

    public void setPkgStartDate(Date pkgStartDate) {
        PkgStartDate = pkgStartDate;
    }

    public Date getPkgEndDate() {
        return PkgEndDate;
    }

    public void setPkgEndDate(Date pkgEndDate) {
        PkgEndDate = pkgEndDate;
    }

    public String getPkgDesc() {
        return PkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        PkgDesc = pkgDesc;
    }

    public BigInteger getPkgBasePrice() {
        return PkgBasePrice;
    }

    public void setPkgBasePrice(BigInteger pkgBasePrice) {
        PkgBasePrice = pkgBasePrice;
    }

    public BigInteger getPkgAgencyCommission() {
        return PkgAgencyCommission;
    }

    public void setPkgAgencyCommission(BigInteger pkgAgencyCommission) {
        PkgAgencyCommission = pkgAgencyCommission;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return PkgName;
    }
}