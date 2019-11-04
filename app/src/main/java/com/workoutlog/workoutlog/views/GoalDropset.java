package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.workoutlog.workoutlog.R;

public class GoalDropset extends ConstraintLayout {

    public GoalDropset(Context context, int sets, int reps, int drops, Integer seconds) {
        this(context, null, 0);
        TextView tvSets = findViewById(R.id.text_view_goal_dropset_sets);
        TextView tvReps = findViewById(R.id.text_view_goal_dropset_reps);
        TextView tvDrops = findViewById(R.id.text_view_goal_dropset_drops);
        TextView tvBreak = findViewById(R.id.text_view_goal_dropset_break);
        tvSets.setText(String.valueOf(sets));
        tvReps.setText(String.valueOf(reps));
        tvDrops.setText(String.valueOf(drops));
        if(seconds != null) {
            tvBreak.setText(String.valueOf(seconds));
        }
    }

    public GoalDropset(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.goal_dropset, this, true);
    }
}
