package com.example.android.pantry.scanner;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.util.Log;

import com.example.android.pantry.R;
import com.example.android.pantry.dataStore.BarcodesTable;
import com.example.android.pantry.dataStore.InventoryTable;
import com.example.android.pantry.dataStore.LocationsTable;
import com.example.android.pantry.dataStore.PantryDbHelper;
import com.example.android.pantry.dataStore.ProductsTable;
import com.example.android.pantry.model.Barcode;
import com.example.android.pantry.model.InventoryItem;
import com.example.android.pantry.model.Product;
import com.example.android.pantry.utilities.NetworkUtils;
import com.example.android.pantry.utilities.PantryJsonUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * The BarcodeScannerActivity is based on the FullScannerActivity from the barcode library,
 * first iteration is based on SimpleScannerActivity
 * https://github.com/dm77/barcodescanner (License Apache 2.0)
 *
 * Uses ZXing, https://github.com/zxing/zxing (License Apache 2.0)
 *
 * NOTE: this article recommands ZXing but also mentions ZBar and Google Mobile Vision
 * https://medium.com/@bherbst/android-barcode-scanning-library-landscape-109292b81b65
 *
 * Google Mobile Vision: https://github.com/googlesamples/android-vision.git
 */
public class BarcodeScannerActivity extends BaseScannerActivity implements MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener,
        SearchDialogFragment.SearchDialogListener {
    private static final String TAG = BarcodeScannerActivity.class.getSimpleName();

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    private SQLiteDatabase mDb;
    private InventoryItem mLastInventoryItem;

    private String mLastBarcodeValue;
    private String mLastBarcodeType;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);


        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        mLastBarcodeValue = rawResult.getText();
        mLastBarcodeType = rawResult.getBarcodeFormat().toString();
        PantryDbHelper dbHelper = new PantryDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        String productInfo = "";
        String quantityString = "";
        mLastInventoryItem = null;

        Barcode barcode = BarcodesTable.getBarcodeByValue(mDb, mLastBarcodeValue);
        if (barcode == null) {
            String message = "Product not found.\nSearch the big product database?";

            showSearchDialog(message);

        } else {
            productInfo = barcode.getProduct().getBrand() + " " + barcode.getProduct().getName();
            long productId = barcode.getProduct().getProductId();

            buildInventoryItemAndShowMessageDialog(productInfo, barcode.getProduct(), productId);
        }

    }

    private void buildInventoryItemAndShowMessageDialog(String productInfo, Product product, long productId) {
        String quantityString = "";
        InventoryItem item = null;
        if(product !=null)
        {
            item = InventoryTable.getInventoryItemByProductId(mDb, productId);
            String quantity = "0";
            if (item != null) {
                quantity = String.valueOf(item.getQuantity());
            } else {
                // TODO: how to enter initial value for location and expiration date??
                // if known product, create new inventory item and fill entries
                if (product.isKnownProduct()) {

                    Log.v(TAG, "productid: " + product.getProductId() + ", brand: " + product.getBrand() +
                            ", name: " + product.getBrand());
                    item = new InventoryItem(0, "new", 0, 0, 0, 0, product);
                }               // id, location, quantity, expiration date, purchase date, price, product
            }
            quantityString = "\nYou have = " + quantity + " of it.";

            // save a copy for inventory update
            mLastInventoryItem = item;
        }

        // mDb handle kept open after network search
        // the mDb handle will be close as part of message dialog
        String locations[] = LocationsTable.getAllLocations(mDb);

        String message = "Barcode = "+mLastBarcodeValue +" ("+mLastBarcodeType+")"+
                "\nProduct = "+productInfo+" "+quantityString;
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, locations, item, this);
        fragment.show(getSupportFragmentManager(), "search_request");
    }

    public void showSearchDialog(String message) {
        DialogFragment fragment = SearchDialogFragment.newInstance("External Search", message, this);
        fragment.show(getSupportFragmentManager(), "search_request");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogAddClick(DialogFragment dialog) {
        // Update the inventory DB
        if (mLastInventoryItem != null) {
            InventoryTable.saveToDb(mDb, mLastInventoryItem.getProductInfo().getProductId(),
                    mLastInventoryItem.getLocation(), mLastInventoryItem.getQuantity() + 1,
                    mLastInventoryItem.getExpirationDate(), mLastInventoryItem.getPurchaseDate(),
                    mLastInventoryItem.getPurchasePrice());
        }
        mDb.close();
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onDialogRemoveClick(DialogFragment dialog) {
        // Update the inventory DB
        if (mLastInventoryItem != null && mLastInventoryItem.getQuantity() > 0) {
            InventoryTable.saveToDb(mDb, mLastInventoryItem.getProductInfo().getProductId(),
                    mLastInventoryItem.getLocation(), mLastInventoryItem.getQuantity() - 1,
                    mLastInventoryItem.getExpirationDate(), mLastInventoryItem.getPurchaseDate(),
                    mLastInventoryItem.getPurchasePrice());
        }
        mDb.close();
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onDialogNotSearchClick(DialogFragment dialog) {
        // Search for product on the web service
        // Nothing to do, close DB
        mDb.close();
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onDialogSearchClick(DialogFragment dialog) {
        // don't close DB, will close after search result
        new ExternalDbSearchTask().execute(mLastBarcodeValue);
        // show request progress view
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        // Nothing to do, close DB
        mDb.close();
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public class ExternalDbSearchTask extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... barcodes) {
            try {
                URL searchBarcodeUrl = NetworkUtils.buildUrlBarcodeQueryByValue(barcodes[0]);
                String jsonBarcodeData = NetworkUtils.getResponseFromHttpUrl(searchBarcodeUrl);
                long productId = PantryJsonUtils.getProductIdFromBarcodeJson(jsonBarcodeData);
                if (productId == 0) return null;

                URL searchProductUrl = NetworkUtils.buildUrlProductQueryById(String.valueOf(productId));
                String jsonProductData = NetworkUtils.getResponseFromHttpUrl(searchProductUrl);
                Product product = PantryJsonUtils.getProductFromJson(jsonProductData);
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {

            String productInfo = "";
            long productId = 0;
            // insert product in DB
            if (product != null) {
                productId = ProductsTable.saveToDb(mDb, product.getBrand(),
                        product.getName(),
                        product.getAmount(),
                        product.getUnit(),
                        product.getIngredient(),
                        product.getCategory());
                // set productid, use new value from local DB, replace value from remote DB
                product.setProductId((int)productId);
                productInfo = product.getBrand() + " " + product.getName();
                BarcodesTable.saveToDb(mDb, mLastBarcodeValue, mLastBarcodeType, productId);
            } else {
                productInfo = "No product information.";
            }

            buildInventoryItemAndShowMessageDialog(productInfo, product, productId);
         }
    }
}
