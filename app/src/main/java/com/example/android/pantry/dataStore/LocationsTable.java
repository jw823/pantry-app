package com.example.android.pantry.dataStore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by dewong4 on 5/29/17.
 */

public class LocationsTable {
    private static final String TAG = LocationsTable.class.getSimpleName();

    public static String [] getAllLocations(SQLiteDatabase db) {
        Cursor cursor = db.query(
                PantryContract.LocationsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PantryContract.LocationsEntry.COLUMN_LOCATION_ID
        );

        String locations[] = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            locations[i] = cursor.getString(cursor.getColumnIndex(PantryContract.LocationsEntry.COLUMN_DESCRIPTION));
        }

        return locations;
    }

    public static int getLocationIdByDescription(SQLiteDatabase db, String description) {
        String[] projection = { PantryContract.LocationsEntry.COLUMN_LOCATION_ID };
        String selection =
                PantryContract.LocationsEntry.COLUMN_DESCRIPTION + " = ?";
        String[] selectionArgs = { description };

        Cursor cursor = db.query(
                PantryContract.LocationsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                PantryContract.LocationsEntry.COLUMN_LOCATION_ID
        );

        if (cursor.getCount() == 0) return 0;

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(PantryContract.LocationsEntry.COLUMN_LOCATION_ID));
    }

    public static long saveToDb(SQLiteDatabase db, String description) {

        ContentValues cv = new ContentValues();
        cv.put(PantryContract.LocationsEntry.COLUMN_DESCRIPTION, description);

        long locationId = getLocationIdByDescription(db, description);
        if (locationId == 0) {
            // insert new location into the db
            locationId = db.insert(PantryContract.LocationsEntry.TABLE_NAME, null, cv);
        }

        return locationId;
    }
}
