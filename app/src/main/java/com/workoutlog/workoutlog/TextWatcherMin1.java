package com.workoutlog.workoutlog;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class TextWatcherMin1 implements TextWatcher {

    private final WeakReference<EditText> editText;

    public TextWatcherMin1(EditText editText) {
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
        String s = editable.toString();
        if(s.equals("")) {
            s = "1";
        } else if(s.equals("0")) {
            s = "1";
        }
        et.removeTextChangedListener(this);
        et.setText(s);
        et.setSelection(s.length());
        et.addTextChangedListener(this);
    }
}
