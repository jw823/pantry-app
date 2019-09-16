package com.example.android.pantry.model;

/**
 * Created by dewong4 on 5/16/17.
 */

public class Product {
    private int    mProductId;    // set to 0 if id unknown
    private String mBrand;
    private String mName;
    private double mAmount;
    private String mUnit;
    private String mIngredient;
    private String mCategory;

    public Product(int pId, String brand, String name,
                   double amount, String unit,
                   String ingredient, String category) {
        setProductId(pId);
        setBrand(brand);
        setName(name);
        setAmount(amount);
        setUnit(unit);
        setIngredient(ingredient);
        setCategory(category);
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int mProductId) {
        this.mProductId = mProductId;
    }

    public boolean isKnownProduct() { return (mProductId != 0); }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String mBrand) {
        this.mBrand = mBrand;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }
}
