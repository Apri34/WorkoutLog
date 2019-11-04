package com.workoutlog.workoutlog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Exercise
import com.workoutlog.workoutlog.database.entities.Normal
import com.workoutlog.workoutlog.ui.fragments.CreateNormalForWorkoutDialogFragment

class ExerciseAdapterMultiselet(private var mDataset: List<Exercise>, private val routineId: Int) :
    RecyclerView.Adapter<ExerciseAdapterMultiselet.ExercisesMultiselectViewHolder>() {

    private val normals = ArrayList<Normal>()

    private var listener: IShowDialog? = null
    fun setListener(listener: IShowDialog) {
        this.listener = listener
    }

    fun getNormals() = normals

    inner class ExercisesMultiselectViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(exc: Exercise, isSelected: Boolean) {
            val tv = view.findViewById<TextView>(R.id.text_view_exercise_view_exercise)
            val layout = view.findViewById<LinearLayout>(R.id.layout_view_exercises)
            layout.isClickable = true
            tv.text = exc.eName
            tv.isSelected = isSelected
            layout.isSelected = isSelected
            layout.setOnClickListener {
                if(!isSelected) {
                    val dialog = CreateNormalForWorkoutDialogFragment.newInstance(
                        exc.eName,
                        exc.eId,
                        routineId,
                        normals.size
                    )
                    dialog.setListener(object : CreateNormalForWorkoutDialogFragment.ICreateNormalForWorkout {
                        override fun addNormal(normal: Normal) {
                            normals.add(normal)
                            notifyDataSetChanged()
                        }
                    })
                    if(listener != null) listener!!.showDialog(dialog)
                } else {
                    normals.forEach {
                        if(exc.eId == it.eId) normals.remove(it)
                        notifyDataSetChanged()
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesMultiselectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_exercises, parent, false)

        return ExercisesMultiselectViewHolder(view)
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: ExercisesMultiselectViewHolder, position: Int) {
        var isSelected = false
        normals.forEach {
            if(it.eId == mDataset[position].eId) isSelected = true
        }
        holder.bind(mDataset[position], isSelected)
    }

    interface IShowDialog {
        fun showDialog(dialog: CreateNormalForWorkoutDialogFragment)
    }
}