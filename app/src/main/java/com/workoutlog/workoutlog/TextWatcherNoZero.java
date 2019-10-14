package com.workoutlog.workoutlog;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class TextWatcherNoZero implements TextWatcher {

    private final WeakReference<EditText> editText;

    public TextWatcherNoZero(EditText editText) {
        this.editText = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        EditText et = editText.get();
        if(et == null) return;
        String number = s.toString();
        if(!number.equals("0")) return;
        else number = "";
        et.removeTextChangedListener(this);
        et.setText(number);
        et.setSelection(number.length());
        et.addTextChangedListener(this);
    }
}
