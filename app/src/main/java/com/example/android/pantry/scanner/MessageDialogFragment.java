package com.example.android.pantry.scanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.pantry.R;
import com.example.android.pantry.dataStore.LocationsTable;
import com.example.android.pantry.model.InventoryItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by dewong4 on 5/13/17.
 */

public class MessageDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public interface MessageDialogListener {
        public void onDialogAddClick(DialogFragment dialog);
        public void onDialogRemoveClick(DialogFragment dialog);
        public void onDialogCancelClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private String[] mLocations;
    private InventoryItem mItem;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, String[] locations,
                                                    InventoryItem item, MessageDialogListener listener) {
        Log.d("create", "item: " + item);
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mLocations = locations;
        fragment.mItem = item;
        fragment.mListener = listener;
        return fragment;
    }

    final Calendar myCalendar = Calendar.getInstance();
    // final TimeZone timezone = TimeZone.getDefault();    // specific, use TimeZone.getTimeZone("America/Los_Angeles")


    private void updateLabel(EditText view) {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        view.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View addInventoryView = inflater.inflate(R.layout.add_inventory, null);
        TextView messageTextView = (TextView) addInventoryView.findViewById(R.id.tv_message);
        messageTextView.setText(mMessage);
        Log.e("inventory","locations header:");
        for (int i=0; i<mLocations.length; i++) {
            Log.e("inventory:","locations[" + i + "]: " + mLocations[i]);
        }

        Spinner locationsSpinner = (Spinner) addInventoryView.findViewById(R.id.locations_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, mLocations);
        locationsSpinner.setAdapter(adapter);

        final EditText expireDateEditText = (EditText) addInventoryView.findViewById(R.id.et_expire_date);

        // set onDateSetListener to save the date from DatePicker in dateEditText
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(expireDateEditText);

                // TODO: is ther better way to get date?
                GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault());
                gc.clear();
                gc.set(year, monthOfYear, dayOfMonth, 23, 59, 0);
                Log.d("set expire date: ", "mItem: " + mItem);
                if (mItem != null) {
                    mItem.setExpirationDate(gc.getTimeInMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    sdf.setTimeZone(TimeZone.getDefault());
                    Log.d("get purchase date: ", sdf.format(new Date(mItem.getPurchaseDate())));
                    Log.d("get expiration date: ", sdf.format(new Date(mItem.getExpirationDate())));

                }
            }
        };

        // Set date to current date
        updateLabel(expireDateEditText);

        // set onClick to launch DatePickerDialog() if dateEditText clicked
        expireDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Log.d("timezone: ", timezone.getDisplayName());
        if (mItem != null) {
            mItem.setPurchaseDate((long)(new Date().getTime()));
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.setTimeZone(TimeZone.getDefault());
            Log.d("get purchase date: ", sdf.format(new Date(mItem.getPurchaseDate())));
        }


        final EditText purchasePriceEditText = (EditText) addInventoryView.findViewById(R.id.et_purchase_price);
        if (mItem != null) {
            purchasePriceEditText.setText(String.valueOf(mItem.getPurchasePrice()), TextView.BufferType.EDITABLE);
        }
        else {
            purchasePriceEditText.setText("0", TextView.BufferType.EDITABLE);
        }


        purchasePriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do
            }

            @Override
            public void afterTextChanged(Editable s) {
                // save purchase price
                Log.d("set purchase price: ", "mItem: " + mItem);
                if (mItem != null && !s.toString().equals("")) {
                    mItem.setPurchasePrice((long)Math.round(Double.parseDouble(s.toString()) * 100));
                    Log.d("get purchase price: ", Long.toString(mItem.getPurchasePrice()));
                }
            }
        });

        builder.setTitle(mTitle)
                // .setMessage(mMessage)
                .setView(addInventoryView);

        builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogAddClick(MessageDialogFragment.this);
                }
            }
        });

        builder.setNeutralButton(R.string.cancel_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogCancelClick(MessageDialogFragment.this);
                }
            }
        });

        builder.setNegativeButton(R.string.remove_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogRemoveClick(MessageDialogFragment.this);
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
