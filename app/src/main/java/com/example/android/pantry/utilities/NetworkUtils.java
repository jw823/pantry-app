package com.example.android.pantry.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by dewong4 on 5/24/17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BARCODE_QUERY_BY_VALUE = "/barcode/value/";
    private static final String PRODUCT_QUERY = "/product";

    private static String mServiceHostname;
    private static String mServicePrefix;

    public static void setServiceHostname(String hostname) {
        mServiceHostname = hostname;
        mServicePrefix = "http://" + mServiceHostname + ":4567";
    }

    public static URL buildUrlBarcodeQueryByValue(String barcodeValue) {
        Uri builtUri = Uri.parse(mServicePrefix).buildUpon()
                .appendEncodedPath(BARCODE_QUERY_BY_VALUE)
                .appendEncodedPath(barcodeValue)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlProductQueryById(String id) {
        Uri builtUri = Uri.parse(mServicePrefix).buildUpon()
                .appendEncodedPath(PRODUCT_QUERY)
                .appendEncodedPath("/" + id)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
