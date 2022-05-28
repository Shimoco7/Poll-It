package com.example.appproject.model.reward;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Order {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    String id;

    String rewardId;
    String supplierImage;
    String title;
    Long purchaseDate;
    Long expirationDate;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public Long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSupplierImage() {
        return supplierImage;
    }

    public void setSupplierImage(String supplierImage) {
        this.supplierImage = supplierImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
