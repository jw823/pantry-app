package com.example.android.pantry;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity implements InventoryAdapter.InventoryAdapterOnClickHandler {

    private InventoryAdapter mAdapter;
    private RecyclerView mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // set up recycler view
        mProductList = (RecyclerView) findViewById(R.id.rv_inventory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mProductList.setLayoutManager(layoutManager);
        mProductList.setHasFixedSize(true);

        mAdapter = new InventoryAdapter(this, this);
        mProductList.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        // TODO: find new place to close DB
        // mDb.close();
        super.onDestroy();
    }

    @Override
    public void onClick(String inventoryItemInfo) {
        Context context = this;
        Toast.makeText(context, inventoryItemInfo, Toast.LENGTH_SHORT)
                .show();
    }
}
