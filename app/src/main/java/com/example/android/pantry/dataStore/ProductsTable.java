package com.example.android.pantry.dataStore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.pantry.model.Product;

/**
 * Created by dewong4 on 5/14/17.
 */

public class ProductsTable {
    private static final String TAG = ProductsTable.class.getSimpleName();

    public static Cursor getAllProducts(SQLiteDatabase db) {
        return db.query(
                PantryContract.ProductsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PantryContract.ProductsEntry.COLUMN_PRODUCT_ID
        );
    }

    public static Product getProduct(SQLiteDatabase db, long productId) {
        String selection =
                PantryContract.ProductsEntry.COLUMN_PRODUCT_ID + " = ?";
         String[] selectionArgs = { Long.toString(productId) };

        Cursor cursor = db.query(
                PantryContract.ProductsEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.ProductsEntry.COLUMN_PRODUCT_ID
        );

        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Product product = new Product(
                cursor.getInt(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_PRODUCT_ID)),
                cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_BRAND)),
                cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_NAME)),
                cursor.getDouble(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_UNIT)),
                cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_INGREDIENT)),
                cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_CATEGORY))
        );
        return product;
    }

    public static long getProductIdByName(SQLiteDatabase db, String brand, String name, double amount) {
        String[] projection = { PantryContract.ProductsEntry.COLUMN_PRODUCT_ID };
        String selection =
                PantryContract.ProductsEntry.COLUMN_BRAND + "=?" + " AND " +
                        PantryContract.ProductsEntry.COLUMN_NAME + "=?" + " AND " +
                        PantryContract.ProductsEntry.COLUMN_AMOUNT + "=?";
        String[] selectionArgs = { brand, name, Double.toString(amount) };

        Cursor cursor = db.query(
                PantryContract.ProductsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.ProductsEntry.COLUMN_PRODUCT_ID
        );

        if (cursor.getCount() == 0) return 0;

        cursor.moveToFirst();
        long productId = cursor.getInt(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_PRODUCT_ID));
        return productId;
    }

    public static long saveToDb(SQLiteDatabase db, String brand, String name,
                                double amount, String unit,
                                String ingredient, String category) {

        ContentValues cv = new ContentValues();
        cv.put(PantryContract.ProductsEntry.COLUMN_BRAND, brand);
        cv.put(PantryContract.ProductsEntry.COLUMN_NAME, name);
        cv.put(PantryContract.ProductsEntry.COLUMN_AMOUNT, amount);
        cv.put(PantryContract.ProductsEntry.COLUMN_UNIT, unit);
        cv.put(PantryContract.ProductsEntry.COLUMN_INGREDIENT, ingredient);
        cv.put(PantryContract.ProductsEntry.COLUMN_CATEGORY, category);

        long productId = getProductIdByName(db, brand, name, amount);
        if (productId != 0) {
            // update the product in db
            String selection =
                    PantryContract.ProductsEntry.COLUMN_BRAND + "=?" + " AND " +
                            PantryContract.ProductsEntry.COLUMN_NAME + "=?" + " AND " +
                            PantryContract.ProductsEntry.COLUMN_AMOUNT + "=?";
            String[] selectionArgs = { brand, name, Double.toString(amount) };

            Log.i(TAG + " saveProductToDb() ", ", updated brand: " + brand +
                    " name: " + name);

            int count;
            count = db.update(
                    PantryContract.ProductsEntry.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);


        } else {
            // insert new product into the db
            Log.i(TAG + " saveProductToDb() ", ", inserted brand: " + brand +
                    " name: " + name);

            productId =  db.insert(PantryContract.ProductsEntry.TABLE_NAME, null, cv);
        }

        return productId;
    }

}
