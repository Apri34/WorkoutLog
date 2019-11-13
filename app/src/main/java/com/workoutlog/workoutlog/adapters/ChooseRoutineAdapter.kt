package com.workoutlog.workoutlog.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine

class ChooseRoutineAdapter(private val context: Context, private val mDataset: List<List<Routine>>): RecyclerView.Adapter<ChooseRoutineAdapter.ChooseRoutineViewHolder>() {

    private var dbInitializer = DatabaseInitializer.getInstance(context)
    private var database = AppDatabase.getInstance(context)
    private var listener: IChooseRoutineAdapter? = null

    fun setListener(listener: IChooseRoutineAdapter) {
        this.listener = listener
    }

    inner class ChooseRoutineViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(routines: List<Routine>) {
            val tp = dbInitializer.getTrainingplanById(database.trainingplanDao(), routines[0].tpId)
            val tvTrainingplan = view.findViewById<TextView>(R.id.text_view_choose_routine_tp)
            tvTrainingplan.text = tp.tpName
            val layout = view.findViewById<LinearLayout>(R.id.layout_view_choose_routine)
            routines.forEach {
                layout.addView(createRoutine(it))
            }
        }

        private fun createRoutine(routine: Routine): TextView {
            val textView = TextView(context)
            textView.tag = routine
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.background = ContextCompat.getDrawable(context, R.drawable.background_recyclerview_item)
            textView.text = routine.rName
            textView.setOnClickListener {
                if(listener != null) listener!!.routineSelected(routine)
            }
            return textView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseRoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_choose_routine, parent, false)
        return ChooseRoutineViewHolder(view)
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: ChooseRoutineViewHolder, position: Int) {
        holder.bind(mDataset[position])
    }

    interface IChooseRoutineAdapter {
        fun routineSelected(routine: Routine)
    }
}