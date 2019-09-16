package com.example.android.pantry.model;

/**
 * Created by dewong4 on 5/16/17.
 */

public class Barcode {
    private long barcodeId;  // set to 0 if id unknown
    private String type;
    private String value;
    private Product product;

    public Barcode(long barcodeId, String type, String value, Product product) {
        this.barcodeId = barcodeId;
        this.type = type;
        this.value = value;
        this.product = product;
    }

    public long getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(int barcodeId) {
        this.barcodeId = barcodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
