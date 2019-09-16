package com.example.android.pantry;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.ProductsAdapterOnClickHandler {

    private ProductsAdapter mAdapter;
    private RecyclerView mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

         // set up recycler view
        mProductList = (RecyclerView) findViewById(R.id.rv_product_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mProductList.setLayoutManager(layoutManager);
        mProductList.setHasFixedSize(true);

        mAdapter = new ProductsAdapter(this, this);
        mProductList.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        // TODO: find new place to close DB
        // mDb.close();
        super.onDestroy();
    }

    @Override
    public void onClick(String productDetail) {
        Context context = this;
        Toast.makeText(context, productDetail, Toast.LENGTH_SHORT)
                .show();

    }
}
