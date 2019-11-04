package com.workoutlog.workoutlog.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.workoutlog.workoutlog.R;

public class GoalSuperset extends ConstraintLayout {

    public GoalSuperset(Context context, String exc1, String exc2, int reps1, int reps2, Float rpe1, Float rpe2, int sets, Integer seconds) {
        this(context, null, 0);
        TextView tvReps1 = findViewById(R.id.text_view_goal_superset_reps1);
        TextView tvReps2 = findViewById(R.id.text_view_goal_superset_reps2);
        TextView tvRpe1 = findViewById(R.id.text_view_goal_superset_rpe1);
        TextView tvRpe2 = findViewById(R.id.text_view_goal_superset_rpe2);
        TextView tvSets = findViewById(R.id.text_view_goal_superset_sets);
        TextView tvBreak = findViewById(R.id.text_view_goal_superset_break);
        TextView tvExc1 = findViewById(R.id.text_view_goal_superset_exc1);
        TextView tvExc2 = findViewById(R.id.text_view_goal_superset_exc2);

        tvExc1.setText(exc1);
        tvExc2.setText(exc2);
        tvReps1.setText(String.valueOf(reps1));
        tvReps2.setText(String.valueOf(reps2));
        tvSets.setText(String.valueOf(sets));
        if(rpe1 != null)
            tvRpe1.setText(String.valueOf(rpe1));
        if(rpe2 != null)
            tvRpe2.setText(String.valueOf(rpe2));
        if(seconds != null)
            tvBreak.setText(String.valueOf(seconds));
    }

    public GoalSuperset(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.goal_superset, this, true);
    }
}
