package com.workoutlog.workoutlog.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;

import java.util.List;

@SuppressWarnings("unchecked")
public class HintAdapter extends ArrayAdapter {
    public HintAdapter(Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
