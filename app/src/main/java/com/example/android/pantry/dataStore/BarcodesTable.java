package com.example.android.pantry.dataStore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.pantry.model.Barcode;
import com.example.android.pantry.model.Product;

import com.example.android.pantry.dataStore.PantryContract;

/**
 * Created by dewong4 on 5/14/17.
 */

public class BarcodesTable {
    private static final String TAG = BarcodesTable.class.getSimpleName();

    public static Cursor getAllBarcodes(SQLiteDatabase db, String value) {
        String selection =
                PantryContract.BarcodesEntry.COLUMN_VALUE + " = ?";
        String[] selectionArgs = { value };

        Cursor cursor = db.query(
                PantryContract.BarcodesEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.BarcodesEntry.COLUMN_BARCODE_ID
        );

        return cursor;
    }

    public static Barcode getBarcodeByValue(SQLiteDatabase db, String value) {
        String selection =
                PantryContract.BarcodesEntry.COLUMN_VALUE + " = ?";
        String[] selectionArgs = { value };

        Cursor cursor = db.query(
                PantryContract.BarcodesEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.BarcodesEntry.COLUMN_BARCODE_ID
        );

        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();

        // use productId to query the product
        long productId = cursor.getLong(cursor.getColumnIndex(PantryContract.BarcodesEntry.COLUMN_PRODUCT_ID));
        Product product = ProductsTable.getProduct(db, productId);
        if (product == null) return null;

        long barcodeId = cursor.getInt(cursor.getColumnIndex(PantryContract.BarcodesEntry.COLUMN_BARCODE_ID));
        String type = cursor.getString(cursor.getColumnIndex(PantryContract.BarcodesEntry.COLUMN_TYPE));

        return new Barcode(barcodeId, type, value, product);
    }

    public static long getBarcodeIdByValue(SQLiteDatabase db, String value) {
        String[] projection = { PantryContract.BarcodesEntry.COLUMN_BARCODE_ID };
        String selection =
                PantryContract.BarcodesEntry.COLUMN_VALUE + " = ?";
        String[] selectionArgs = { value };

        Cursor cursor = db.query(
                PantryContract.BarcodesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.BarcodesEntry.COLUMN_BARCODE_ID
        );

        if (cursor.getCount() == 0) return 0;

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(PantryContract.BarcodesEntry.COLUMN_BARCODE_ID));
    }

    public static long saveToDb(SQLiteDatabase db, String value, String type, long productId) {

        ContentValues cv = new ContentValues();
        cv.put(PantryContract.BarcodesEntry.COLUMN_VALUE, value);
        cv.put(PantryContract.BarcodesEntry.COLUMN_TYPE, type);
        cv.put(PantryContract.BarcodesEntry.COLUMN_PRODUCT_ID, productId);

        long barcodeId = getBarcodeIdByValue(db, value);
        if (barcodeId != 0) {
            // update the barcode in db
            String selection =
                    PantryContract.BarcodesEntry.COLUMN_VALUE + " = ? ";
            String[] selectionArgs = { value };

            Log.i(TAG + " saveProductToDb() ", ", updated barcode: " + value);

            int count;
            count = db.update(
                    PantryContract.BarcodesEntry.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);


        } else {
            // insert new product into the db
            barcodeId = db.insert(PantryContract.BarcodesEntry.TABLE_NAME, null, cv);
        }

        return barcodeId;
    }


}
