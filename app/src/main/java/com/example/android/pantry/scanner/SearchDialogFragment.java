package com.example.android.pantry.scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.android.pantry.R;

/**
 * Created by dewong4 on 5/24/17.
 */

public class SearchDialogFragment extends DialogFragment {
    public interface SearchDialogListener {
        public void onDialogSearchClick(DialogFragment dialog);
        public void onDialogNotSearchClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private SearchDialogFragment.SearchDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static SearchDialogFragment newInstance(String title, String message, SearchDialogFragment.SearchDialogListener listener) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton(R.string.search_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogSearchClick(SearchDialogFragment.this);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogNotSearchClick(SearchDialogFragment.this);
                }
            }
        });

        return builder.create();
    }

}
