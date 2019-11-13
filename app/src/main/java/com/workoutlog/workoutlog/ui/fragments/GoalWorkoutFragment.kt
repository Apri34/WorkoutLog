package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Dropset
import com.workoutlog.workoutlog.database.entities.Normal
import com.workoutlog.workoutlog.database.entities.Superset
import com.workoutlog.workoutlog.views.GoalDropset
import com.workoutlog.workoutlog.views.GoalNormal
import com.workoutlog.workoutlog.views.GoalSuperset

class GoalWorkoutFragment: Fragment() {

    companion object {
        private const val KEY_NORMAL = "normal"
        private const val KEY_SUPERSET = "superset"
        private const val KEY_DROPSET = "dropset"

        fun newInstance(normal: Normal): GoalWorkoutFragment {
            val fragment = GoalWorkoutFragment()
            val args = Bundle()
            args.putParcelable(KEY_NORMAL, normal)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(superset: Superset): GoalWorkoutFragment {
            val fragment = GoalWorkoutFragment()
            val args = Bundle()
            args.putParcelable(KEY_SUPERSET, superset)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(dropset: Dropset): GoalWorkoutFragment {
            val fragment = GoalWorkoutFragment()
            val args = Bundle()
            args.putParcelable(KEY_DROPSET, dropset)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val normal = arguments?.getParcelable(KEY_NORMAL) as Normal?
        val superset = arguments?.getParcelable(KEY_SUPERSET) as Superset?
        val dropset = arguments?.getParcelable(KEY_DROPSET) as Dropset?

        return when {
            normal != null -> {
                GoalNormal(context, normal.sets, normal.reps, normal.rpe?.toFloat(), normal.breakInSeconds)
            }
            superset != null -> {
                val exc1 = DatabaseInitializer.getInstance(context).getExerciseNameById(AppDatabase.getInstance(context).exerciseDao(), superset.eId1)
                val exc2 = DatabaseInitializer.getInstance(context).getExerciseNameById(AppDatabase.getInstance(context).exerciseDao(), superset.eId2)
                GoalSuperset(context, exc1, exc2, superset.reps1, superset.reps2, superset.rpe1?.toFloat(), superset.rpe2?.toFloat(), superset.sets, superset.breakInSeconds)
            }
            dropset != null -> {
                GoalDropset(context, dropset.sets, dropset.reps, dropset.drops, dropset.breakInSeconds)
            }
            else -> null
        }
    }
}