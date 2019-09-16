package com.example.android.pantry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pantry.dataStore.BarcodesTable;
import com.example.android.pantry.dataStore.InventoryTable;
import com.example.android.pantry.dataStore.LocationsTable;
import com.example.android.pantry.dataStore.ProductsTable;
import com.example.android.pantry.dataStore.PantryDbHelper;
import com.example.android.pantry.dataStore.PantryContract;
import com.example.android.pantry.model.InventoryItem;
import com.example.android.pantry.model.Product;

/**
 * Created by dewong4 on 5/16/17.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryItemViewHolder> {

    private static final String TAG = InventoryAdapter.class.getSimpleName();

    private int mNumberItems;

    private final InventoryAdapterOnClickHandler mClickHandler;

    private SQLiteDatabase mDb;


    public interface InventoryAdapterOnClickHandler {
        void onClick(String productDetail);
    }

    public InventoryAdapter(Context context, InventoryAdapterOnClickHandler clickHandler) {
        mNumberItems = 0;
        mClickHandler = clickHandler;

        // set up DB to use products table
        PantryDbHelper dbHelper = new PantryDbHelper(context);
        mDb = dbHelper.getWritableDatabase();
/*
        //TODO: begin move this block to test code
        long productId = 0;

        productId = ProductsTable.saveToDb(mDb, "Kong Yen", "rice vinegar", 20.2, "FL OZ", "vinegar", "grocery");
        InventoryTable.saveToDb(mDb, productId, "pantry", 6, "2018-Dec-31");
        BarcodesTable.saveToDb(mDb, "673367140049", "UPC_A", productId);

        productId = ProductsTable.saveToDb(mDb, "Kraft", "A1 sauce", 10.0, "OZ", "a1 sauce", "grocery");
        InventoryTable.saveToDb(mDb, productId, "fridge", 1, "2017-Aug-31");
        BarcodesTable.saveToDb(mDb, "054400000054", "UPC_A", productId);

        productId = ProductsTable.saveToDb(mDb, "Advil", "Ibuprofen", 360.0, "tablet", "ibuprofen", "drug");
        InventoryTable.saveToDb(mDb, productId, "medicine cabinet", 1, "2019-May-31");
        BarcodesTable.saveToDb(mDb, "305730154604", "UPC_A", productId);
    // TODO: end move this block to test code
    */
        LocationsTable.saveToDb(mDb, "pantry");
        LocationsTable.saveToDb(mDb, "fridge");
        LocationsTable.saveToDb(mDb, "medicine cabinet");

        Cursor cursor = InventoryTable.getInventory(mDb);
        int count = cursor.getCount();
        mNumberItems = count;
        Log.i(this.toString(), "Found " + count + " inventoryTable items.");

        for (int i=0; i < count; i++) {
            if (!cursor.moveToPosition(i)) continue;
            long inventoryId = cursor.getLong(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_INVENTORY_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_QUANTITY));
            String location = cursor.getString(cursor.getColumnIndex(PantryContract.InventoryEntry.COLUMN_LOCATION));
            Log.i(TAG, i + ", id: " + inventoryId + ", location "+ location + ", quantity: " + quantity);
        }


    }

    @Override
    public InventoryItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.inventory_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        InventoryItemViewHolder viewHolder = new InventoryItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InventoryItemViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class InventoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView quantityView;
        TextView itemInventoryDescriptionView;

        public InventoryItemViewHolder(View itemView) {
            super(itemView);

            quantityView = (TextView) itemView.findViewById(R.id.tv_item_quantity);
            itemInventoryDescriptionView = (TextView) itemView.findViewById(R.id.tv_item_inventory_desc);
            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex) {
            InventoryItem item = InventoryTable.getInventoryItemByInventoryId(mDb, listIndex + 1);
            if (item == null) {
                quantityView.setText(String.valueOf(0));
                itemInventoryDescriptionView.setText(String.valueOf(listIndex));
                return;
            }

            Product product = item.getProductInfo();
            String brand = product.getBrand();
            String name = product.getName();

            quantityView.setText(String.valueOf(item.getQuantity()));
            itemInventoryDescriptionView.setText(brand + " " + name);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            InventoryItem item = InventoryTable.getInventoryItemByInventoryId(mDb, adapterPosition + 1);
            if (item == null) {
                // don't call on click callback
                return;
            }

            Product product = item.getProductInfo();
            String brand = product.getBrand();
            String name = product.getName();
            String inventoryItemSummary = "Qty: " + item.getQuantity() + ", brand: " + brand + ", name: " + name;
            mClickHandler.onClick(inventoryItemSummary);
        }
    }
}