package com.example.android.pantry.utilities;

import android.util.Log;

import com.example.android.pantry.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dewong4 on 5/24/17.
 */

public class PantryJsonUtils {
    private static final String TAG = PantryJsonUtils.class.getSimpleName();
    private static final String PRODUCT_ID = "productid";

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("\\", "");
        return data_json;
    }

    public static Product getProductFromJson(String rawProductJsonStr) {
        if (rawProductJsonStr == null) return null;

        String productJsonStr = convertStandardJSONString(rawProductJsonStr);

        Log.v(TAG, "productJsonStr = " + productJsonStr);

        try {
            JSONObject productJson = new JSONObject(productJsonStr);
            Product product = new Product(
                    productJson.getInt("id"),
                    productJson.getString("brand"),
                    productJson.getString("name"),
                    productJson.getDouble("amount"),
                    productJson.getString("unit"),
                    productJson.getString("ingredient"),
                    productJson.getString("category"));
            return product;

        } catch (Exception e) {
            Log.e(TAG, "Error parsing data [" + e.getMessage() + "]");
            return null;
        }

    }

    public static long getProductIdFromBarcodeJson(String rawBarcodeJsonStr) {
        if (rawBarcodeJsonStr == null) return 0;

        String barcodeJsonStr = convertStandardJSONString(rawBarcodeJsonStr);
        long productId = 0;

        Log.v(TAG, "barcodeJsonStr = " + barcodeJsonStr);

        try {
            JSONObject barcodeJson = new JSONObject(barcodeJsonStr);

            // TODO: how to check for valid JSON ?
            // if (barcodeJson == null) return 0;

            productId = barcodeJson.getLong(PRODUCT_ID);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing data [" + e.getMessage() + "]");
        }

        // NOTE: this is productid in remote DB, could be different from local one
        Log.v(TAG, "productId = " + productId);
        return productId;
    }
}
