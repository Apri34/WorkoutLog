package com.workoutlog.workoutlog.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.workoutlog.workoutlog.R;
import com.workoutlog.workoutlog.database.entities.ExerciseDone;
import com.workoutlog.workoutlog.database.entities.SetDone;

import java.util.List;

public class HistoryItem extends ConstraintLayout {

    private final TextView tvDate;
    private final LinearLayout llSets;

    public HistoryItem(Context context) {
        this(context, null, 0);
    }

    public HistoryItem(Context context, ExerciseDone exc, List<SetDone> sets) {
        this(context, null, 0);
        tvDate.setText(exc.getDate().toString());
        for(int i = 0; i < sets.size(); i++) {
            llSets.addView(new Set(context, i+1, sets.get(i).getReps(), sets.get(i).getWeightInKg(), sets.get(i).getRpe()));
        }
    }

    public HistoryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.history_item, this, true);
        tvDate = findViewById(R.id.text_view_date_history_item);
        llSets = findViewById(R.id.linear_layout_history_item_sets);
        setPadding(
                getResources().getDimensionPixelSize(R.dimen.padding_side_history_item),
                getResources().getDimensionPixelSize(R.dimen.padding_vertical_history_item),
                getResources().getDimensionPixelSize(R.dimen.padding_side_history_item),
                getResources().getDimensionPixelSize(R.dimen.padding_vertical_history_item));
    }


    private class Set extends AppCompatTextView {

        Set(Context context, int setNum, int reps, float weight, float rpe) {
            super(context, null, 0);
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.historyItemColor, typedValue, true);
            setTextColor(typedValue.data);
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            makeSet(setNum, reps, weight, rpe);
            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        private void makeSet(int setNum, int reps, float weight, float rpe) {
            @SuppressLint("StringFormatMatches")
            String set = String.format(getContext().getString(R.string.history_item_set), setNum, reps, weight, rpe);
            setText(set);
        }
    }
}
