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

import com.example.android.pantry.dataStore.PantryDbHelper;
import com.example.android.pantry.dataStore.ProductsTable;
import com.example.android.pantry.model.Product;

/**
 * Created by dewong4 on 5/15/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private static final String TAG = ProductsAdapter.class.getSimpleName();

    private int mNumberItems;

    private final ProductsAdapterOnClickHandler mClickHandler;

    private SQLiteDatabase mDb;



    public interface ProductsAdapterOnClickHandler {
        void onClick(String productDetail);
    }

    public ProductsAdapter(Context context, ProductsAdapterOnClickHandler clickHandler) {
        mNumberItems = 0;
        mClickHandler = clickHandler;

        // set up DB to use products table
        PantryDbHelper dbHelper = new PantryDbHelper(context);
        mDb = dbHelper.getWritableDatabase();

        // get all products, populate the recycler view

        // ProductsTable.saveToDb(mDb, "Kong Yen", "rice vinegar", 20.2, "FL OZ", "vinegar", "grocery");
        // ProductsTable.saveToDb(mDb, "Kraft", "A1 sauce", 15.0, "OZ", "a1 sauce", "grocery");
        // ProductsTable.saveToDb(mDb, "Advil", "Ibuprofen", 360.0, "tablet", "ibuprofen", "drug");

        Cursor cursor = ProductsTable.getAllProducts(mDb);
        int count = cursor.getCount();
        mNumberItems = count;
        Log.i(this.toString(), "Found " + count + " products.");
/*
        for (int i=0; i < count; i++) {
            if (!cursor.moveToPosition(i)) continue;
            String brand = cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_BRAND));
            String name = cursor.getString(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_NAME));
            float amount = cursor.getFloat(cursor.getColumnIndex(PantryContract.ProductsEntry.COLUMN_AMOUNT));
            Log.i(this.toString(), "Prod brand: " + brand + ", name: " + name);
        }
*/
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.product_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ProductViewHolder viewHolder = new ProductViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ProductViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemBrandView;
        TextView itemNameView;
        TextView itemAmountView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            itemBrandView = (TextView) itemView.findViewById(R.id.tv_item_brand_name);
            itemAmountView = (TextView) itemView.findViewById(R.id.tv_item_amount_unit);
            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex) {
            Product product = ProductsTable.getProduct(mDb, listIndex + 1);
            if (product == null) {
                itemBrandView.setText(String.valueOf(listIndex));
                return;
            }

            String brand = product.getBrand();
            String name = product.getName();
            double amount = product.getAmount();
            String unit = product.getUnit();

            itemBrandView.setText(brand + " " + name);
            itemAmountView.setText(String.valueOf(amount) + " " + unit);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Product product = ProductsTable.getProduct(mDb, adapterPosition + 1);
            if (product == null) {
                // don't call onClick() callback
                return;
            }

            String brand = product.getBrand();
            String name = product.getName();
            String productSummary = "Brand: " + brand + ", Name: " + name;
            mClickHandler.onClick(productSummary);
        }
    }
}
