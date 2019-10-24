package com.workoutlog.workoutlog;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class TextWatcherPerCentNoZeroMax100 implements TextWatcher {

    private final WeakReference<EditText> editText;

    public TextWatcherPerCentNoZeroMax100(EditText editText) {
        this.editText = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText et = editText.get();
        if(et == null) return;
        String s = editable.toString();
        if(s.isEmpty()) return;
        et.removeTextChangedListener(this);
        String replaceable = "%";
        String cleanString = s.replace(replaceable, "");
        if(cleanString.equals(""))
            cleanString = "0";
        if(cleanString.substring(0, 1).equals("0") && cleanString.length() > 1)
            cleanString = cleanString.substring(1);
        if(Integer.parseInt(cleanString) > 100)
            cleanString = "100";
        else if(Integer.parseInt(cleanString) < 0)
            cleanString = "0";
        if(cleanString.substring(0, 0).equals("0")) {
            cleanString = cleanString.substring(1);
        }
        String formatted = cleanString + "%";
        et.setText(formatted);
        et.setSelection(formatted.length() - 1);
        et.addTextChangedListener(this);
    }
}
