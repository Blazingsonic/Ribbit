package com.saier.sebastian.ribbit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Sebastian on 25.04.2015.
 */
public class AlertDialogFragment extends DialogFragment {

    public static final AlertDialogFragment newInstance(String message) {
        AlertDialogFragment adf = new AlertDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("message", message);
        adf.setArguments(bundle);
        return adf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Oops! Sorry!")
                .setMessage(message)
                .setPositiveButton("OK", null); // null to just close the window

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
