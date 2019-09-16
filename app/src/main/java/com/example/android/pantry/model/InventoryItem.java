package com.example.android.pantry.model;

import com.example.android.pantry.model.Product;

/**
 * Created by dewong4 on 5/16/17.
 */

public class InventoryItem {
    private long mInventoryId;  // set to 0 if id unknown
    private String mLocation;
    private int mQuantity;
    private long mExpirationDate;
    private long mPurchaseDate;
    private long mPurchasePrice;
    private Product mProductInfo;

    public InventoryItem(long inventoryId, String location, int quantity,
                         long expirationDate, long purchaseDate,
                         long purchasePrice, Product productInfo) {
        mInventoryId = inventoryId;
        mLocation = location;
        mQuantity = quantity;
        mExpirationDate = expirationDate;
        mPurchaseDate = purchaseDate;
        mPurchasePrice = purchasePrice;
        mProductInfo = productInfo;
    }

    public long getInventoryId() {
        return mInventoryId;
    }

    public void setInventoryId(long inventoryId) {
        mInventoryId = inventoryId;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocationId(String location) {
        mLocation = location;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public long getExpirationDate() {
        return mExpirationDate*1000; //convert to Java date in milliseconds
    }

    public void setExpirationDate(long expirationDate) {
        mExpirationDate = expirationDate/1000; //concert to unix datae
    }

    public long getPurchaseDate() {
        return mPurchaseDate*1000; //convert to java date in milliseconds
    }

    public void setPurchaseDate(long purchaseDate) {
        mPurchaseDate = purchaseDate/1000;  //convert to unix date
    }

    public long getPurchasePrice() {
        return mPurchasePrice;
    }

    public void setPurchasePrice(long purchasePrice) {
        mPurchasePrice = purchasePrice;
    }

    public Product getProductInfo() {
        return mProductInfo;
    }

    public void setProductInfo(Product productInfo) {
        mProductInfo = productInfo;
    }
}
