package com.example.android.pantry.dataStore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dewong4 on 5/12/17.
 */

public class PantryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry.db";

    // NOTE: update version every time the db schema changes
    private static final int DATABASE_VERSION = 1;

    public PantryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.CategoriesEntry.TABLE_NAME + " (" +
                PantryContract.CategoriesEntry.COLUMN_CATEGORY_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.CategoriesEntry.COLUMN_NAME + " NVARCHAR(30) NOT NULL UNIQUE " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORIES_TABLE);

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.IngredientsEntry.TABLE_NAME + " (" +
                PantryContract.IngredientsEntry.COLUMN_INGREDIENT_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.IngredientsEntry.COLUMN_TYPE + " NVARCHAR(30) NOT NULL UNIQUE, " +
                PantryContract.IngredientsEntry.COLUMN_NAME + " NVARCHAR(30) " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);

        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.ProductsEntry.TABLE_NAME + " (" +
                PantryContract.ProductsEntry.COLUMN_PRODUCT_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.ProductsEntry.COLUMN_CATEGORY +
                    " NVARCHAR(30) NOT NULL REFERENCES Categories(name), " +
                PantryContract.ProductsEntry.COLUMN_INGREDIENT +
                    " NVARCHAR(30) NOT NULL REFERENCES Ingredients(type), " +
                PantryContract.ProductsEntry.COLUMN_BRAND + " NVARCHAR(100) NOT NULL, " +
                PantryContract.ProductsEntry.COLUMN_NAME +  " NVARCHAR(100) NOT NULL, " +
                PantryContract.ProductsEntry.COLUMN_AMOUNT + " REAL," +
                PantryContract.ProductsEntry.COLUMN_UNIT + " NVARCHAR(30), " +
                PantryContract.ProductsEntry.COLUMN_DESCRIPTION + " NVARCHAR(100), " +
                PantryContract.ProductsEntry.COLUMN_DESCRIPTION2 + " NVARCHAR(100) " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        final String SQL_CREATE_BARCODES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.BarcodesEntry.TABLE_NAME + " (" +
                PantryContract.BarcodesEntry.COLUMN_BARCODE_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.BarcodesEntry.COLUMN_PRODUCT_ID +
                    " INTEGER REFERENCES Products(product_id), " +
                PantryContract.BarcodesEntry.COLUMN_TYPE + " NVARCHAR(20), " +
                PantryContract.BarcodesEntry.COLUMN_VALUE + " NVARCHAR(30) NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_BARCODES_TABLE);

        final String SQL_CREATE_PLUS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.PLUsEntry.TABLE_NAME + " (" +
                PantryContract.PLUsEntry.COLUMN_PLU_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.PLUsEntry.COLUMN_PRODUCT_ID +
                    " INTEGER REFERENCES Products(product_id), " +
                PantryContract.PLUsEntry.COLUMN_TYPE + " NVARCHAR(20), " +
                PantryContract.PLUsEntry.COLUMN_VALUE + " NVARCHAR(30) NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_PLUS_TABLE);

        final String SQL_CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.ImagesEntry.TABLE_NAME + " (" +
                PantryContract.ImagesEntry.COLUMN_IMAGE_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.ImagesEntry.COLUMN_PRODUCT_ID +
                    " INTEGER REFERENCES Products(product_id), " +
                PantryContract.ImagesEntry.COLUMN_TYPE + " NVARCHAR(20), " +
                PantryContract.ImagesEntry.COLUMN_DATA + " BLOB " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_IMAGES_TABLE);

        final String SQL_CREATE_LOCATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.LocationsEntry.TABLE_NAME + " (" +
                PantryContract.LocationsEntry.COLUMN_LOCATION_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.LocationsEntry.COLUMN_DESCRIPTION +  " NVARCHAR(40) " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATIONS_TABLE);

        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                PantryContract.InventoryEntry.TABLE_NAME + " (" +
                PantryContract.InventoryEntry.COLUMN_INVENTORY_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PantryContract.InventoryEntry.COLUMN_PRODUCT_ID +
                    " INTEGER REFERENCES Products(product_id), " +
                PantryContract.InventoryEntry.COLUMN_LOCATION +
                    " NVARCHAR(40) REFERENCES Locations(desc), " +
                PantryContract.InventoryEntry.COLUMN_QUANTITY +  " NVARCHAR(40), " +
                PantryContract.InventoryEntry.COLUMN_EXPIRATION_DATE + " INTEGER, " +  // TIMESTAMP
                PantryContract.InventoryEntry.COLUMN_PURCHASE_DATE + " INTEGER, " +    // TIMESTAMP
                PantryContract.InventoryEntry.COLUMN_PURCHASE_PRICE + " INTEGER " +   // DOUBLE
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.CategoriesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.IngredientsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.ProductsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.BarcodesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.PLUsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.ImagesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.LocationsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PantryContract.InventoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
