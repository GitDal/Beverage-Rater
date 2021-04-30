package dk.au.mad21spring.appproject.group6;

import android.content.Context;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

public class EditTextExtensions {

    public static void Enable(EditText editTextView, Context context) {
        editTextView.setEnabled(true);
    }

    public static void Disable(EditText editTextView, Context context) {
        editTextView.setEnabled(false);
        editTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
    }
}
