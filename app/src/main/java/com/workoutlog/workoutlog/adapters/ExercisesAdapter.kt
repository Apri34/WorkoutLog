package com.workoutlog.workoutlog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Exercise

class ExercisesAdapter(private var mDataset: List<Exercise>) :
    RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {

    private var listener: IExerciseAdapter? = null

    inner class ExercisesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(exc: Exercise) {
            val tv = view.findViewById<TextView>(R.id.text_view_exercise_view_exercise)
            tv.text = exc.eName

            if(listener != null) {
                view.setOnLongClickListener {
                    listener!!.exerciseLongClicked(exc)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_exercises, parent, false)

        return ExercisesViewHolder(view)
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bind(mDataset[position])
    }



    interface IExerciseAdapter {
        fun exerciseLongClicked(exc: Exercise)
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as IExerciseAdapter
        } catch (e: ClassCastException) {

        }
    }

    fun setDataset(dataset: List<Exercise>) {
        mDataset = dataset
    }
}