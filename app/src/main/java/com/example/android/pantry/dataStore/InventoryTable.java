package com.example.android.pantry.dataStore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.pantry.model.InventoryItem;
import com.example.android.pantry.model.Product;

/**
 * Created by dewong4 on 5/15/17.
 */

public class InventoryTable {

    private static final String TAG = InventoryTable.class.getSimpleName();

    public static Cursor getInventory(SQLiteDatabase db) {
        return db.query(
                PantryContract.InventoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID
        );
    }

    public static InventoryItem getInventoryItemByInventoryId(SQLiteDatabase db, long inventoryId) {
        String selection =
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID + " = ?";
        String[] selectionArgs = { Long.toString(inventoryId) };

        Cursor cursor = db.query(
                PantryContract.InventoryEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID
        );

        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();

        // use productId to query the product
        long productId = cursor.getLong(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_PRODUCT_ID));
        Product product = ProductsTable.getProduct(db, productId);
        if (product == null) return null;

        String location = cursor.getString(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_LOCATION));
        int quantity = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_QUANTITY));
        long expirationDate = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_EXPIRATION_DATE));
        long purchaseDate = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_PURCHASE_DATE));
        long purchasePrice = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_PURCHASE_PRICE));

        return new InventoryItem(inventoryId, location, quantity, expirationDate, purchaseDate, purchasePrice, product);
    }

    public static InventoryItem getInventoryItemByProductId(SQLiteDatabase db, long productId) {
        String selection =
                PantryContract.InventoryEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { Long.toString(productId) };

        Cursor cursor = db.query(
                PantryContract.InventoryEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID
        );

        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();

        // use productId to query the product
        long inventoryId = cursor.getLong(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_INVENTORY_ID));
        Product product = ProductsTable.getProduct(db, productId);
        if (product == null) return null;

        String location = cursor.getString(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_LOCATION));
        int quantity = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_QUANTITY));
        long expirationDate = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_EXPIRATION_DATE));
        long purchaseDate = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_PURCHASE_DATE));
        long purchasePrice = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_PURCHASE_PRICE));

        return new InventoryItem(inventoryId, location, quantity, expirationDate, purchaseDate, purchasePrice, product);
    }

    private static long getInventoryIdByProductId(SQLiteDatabase db, long productId) {
        String[] projection = {
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID
        };
        String selection = PantryContract.InventoryEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { Long.toString(productId) };

        Cursor cursor = db.query(
                PantryContract.InventoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.InventoryEntry.COLUMN_PRODUCT_ID
        );

        if (cursor.getCount() == 0) return 0;
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_INVENTORY_ID));
    }

    // TODO: make locationId into string, query location table
    public static long saveToDb(SQLiteDatabase db, long productId, String location,
                                int quantity, long expirationDate,
                                long purchaseDate, long purchasePrice) {

        ContentValues cv = new ContentValues();
        cv.put(PantryContract.InventoryEntry.COLUMN_PRODUCT_ID, productId);
        cv.put(PantryContract.InventoryEntry.COLUMN_LOCATION, location);
        cv.put(PantryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
        cv.put(PantryContract.InventoryEntry.COLUMN_EXPIRATION_DATE, expirationDate);
        cv.put(PantryContract.InventoryEntry.COLUMN_PURCHASE_DATE, purchaseDate);
        cv.put(PantryContract.InventoryEntry.COLUMN_PURCHASE_PRICE, purchasePrice);

        long inventoryId = getInventoryIdByProductId(db, productId);
        if (inventoryId != 0) {
            // update the product in db
            String selection =
                    PantryContract.InventoryEntry.COLUMN_PRODUCT_ID + " = ? ";
            String[] selectionArgs = { Long.toString(productId) };

            Log.i(TAG + " saveProductToDb() ", ", updated product: " + productId);

            int count;
            count = db.update(
                    PantryContract.InventoryEntry.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);


        } else {
            // insert new product into the db
            inventoryId = db.insert(PantryContract.InventoryEntry.TABLE_NAME, null, cv);
        }

        return inventoryId;
    }

}
