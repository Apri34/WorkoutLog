package com.workoutlog.workoutlog.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Routine
import java.lang.ClassCastException

class RoutinesAdapter(private var mDataset: List<Routine>) : RecyclerView.Adapter<RoutinesAdapter.RoutinesViewHolder>() {

    private var listener: IRoutinesAdapter? = null

    inner class RoutinesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(routine: Routine) {
            val tv = view.findViewById<TextView>(R.id.text_view_routine_view_routine)
            tv.text = routine.rName

            if(listener != null) {
                view.setOnLongClickListener {
                    listener!!.routineLongClicked(routine)
                    true
                }
                view.setOnClickListener {
                    listener!!.routineClicked(routine)
                }
            }
        }
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: RoutinesViewHolder, position: Int) {
        holder.bind(mDataset[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutinesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_routines, parent, false)

        return RoutinesViewHolder(view)
    }

    interface IRoutinesAdapter {
        fun routineLongClicked(rtn: Routine)
        fun routineClicked(rtn: Routine)
    }

    fun setListener(context: Context) {
        try {
            listener = context as IRoutinesAdapter
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IRoutinesAdapter")
        }
    }

    fun setDataset(dataset: List<Routine>) {
        mDataset = dataset
    }
}