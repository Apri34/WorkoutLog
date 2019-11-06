package com.workoutlog.workoutlog.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.database.entities.ExerciseDone
import com.workoutlog.workoutlog.database.entities.SetDone
import com.workoutlog.workoutlog.views.HistoryItemHistoryFragment

class HistoryAdapter(private val context: Context, private var mExerciseDones: List<ExerciseDone>, private var mSetDones: List<List<SetDone>>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    fun setData(exerciseDones: List<ExerciseDone>, setDones: List<List<SetDone>>) {
        mExerciseDones = exerciseDones
        mSetDones = setDones
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val view: HistoryItemHistoryFragment): RecyclerView.ViewHolder(view) {

        fun bind(exerciseDone: ExerciseDone, setDones: List<SetDone>) {
            view.setValues(context, exerciseDone, setDones)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = HistoryItemHistoryFragment(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return HistoryViewHolder(view)
    }

    override fun getItemCount() = mExerciseDones.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(mExerciseDones[position], mSetDones[position])
    }
}