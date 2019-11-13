package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.views.HistoryItem

class HistoryFragmentWorkout: Fragment() {

    private lateinit var linearLayoutHistory: LinearLayout

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    companion object {
        private const val KEY_EXC = "exc"

        fun newInstance(exerciseId: Int): HistoryFragmentWorkout {
            val fragment = HistoryFragmentWorkout()
            val args = Bundle()
            args.putInt(KEY_EXC, exerciseId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbInitializer = DatabaseInitializer.getInstance(context)
        database = AppDatabase.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history_workout, container, false)

        linearLayoutHistory = view.findViewById(R.id.linear_layout_fragment_history_workout)

        val eId = arguments!!.getInt(KEY_EXC)

        val excDones = dbInitializer.getExerciseDonesByEId(database.exerciseDoneDao(), eId)
        excDones.forEach { ed ->
            val setDones = dbInitializer.getSetDonesByEdId(database.setDoneDao(), ed.edId)
            if(setDones.size != 0)
                linearLayoutHistory.addView(HistoryItem(context, ed, setDones))
        }

        return view
    }
}