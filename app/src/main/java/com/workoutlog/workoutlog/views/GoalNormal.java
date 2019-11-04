package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.workoutlog.workoutlog.R;

public class GoalNormal extends ConstraintLayout {

    public GoalNormal(Context context, int sets, int reps, Float rpe, Integer seconds) {
        this(context, null, 0);
        TextView tvSets = findViewById(R.id.text_view_goal_normal_sets);
        TextView tvReps = findViewById(R.id.text_view_goal_normal_reps);
        TextView tvRpe = findViewById(R.id.text_view_goal_normal_rpe);
        TextView tvBreak = findViewById(R.id.text_view_goal_normal_break);
        tvSets.setText(String.valueOf(sets));
        tvReps.setText(String.valueOf(reps));
        if(rpe != null) {
            tvRpe.setText(String.valueOf(rpe));
        }
        if(seconds != null) {
            tvBreak.setText(String.valueOf(seconds));
        }
    }

    public GoalNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.goal_normal, this, true);
    }
}
