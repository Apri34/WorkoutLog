package com.workoutlog.workoutlog.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Trainingplan
import java.lang.ClassCastException
import java.lang.Exception

class TrainingplansAdapter(private var mDataset: List<Trainingplan>) :
    RecyclerView.Adapter<TrainingplansAdapter.TrainingplansViewHolder>(){

    companion object {
        const val NO_SELECTION = -1
    }

    private var selectedItem = NO_SELECTION
    fun setSelectedItem(position: Int) {
        selectedItem = position
    }
    fun getSelectedItem(): Trainingplan? {
        if(selectedItem == NO_SELECTION) return null
        return try {
            mDataset[selectedItem]
        } catch (e: Exception) { null }
    }

    private var listener: ITrainingplansAdapter? = null

    inner class TrainingplansViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trp: Trainingplan, position: Int) {
            val tv = view.findViewById<TextView>(R.id.text_view_trainingplan_view_trainingplans)
            val layout = view.findViewById<LinearLayout>(R.id.layout_view_trainingplans)
            tv.text = trp.tpName

            if(listener != null) {
                view.setOnLongClickListener {
                    listener!!.trainingplanLongClicked(trp)
                    true
                }
                view.setOnClickListener {
                    listener!!.trainingPlanClicked(trp)
                }
            }
            if(position == selectedItem) {
                layout.isSelected = true
                tv.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingplansViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_trainingplans, parent, false)

        return TrainingplansViewHolder(view)
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: TrainingplansViewHolder, position: Int) {
        holder.bind(mDataset[position], position)
    }

    interface ITrainingplansAdapter {
        fun trainingplanLongClicked(trp: Trainingplan)
        fun trainingPlanClicked(trp: Trainingplan)
    }

    fun setListener(listener: Fragment) {
        try {
            this.listener = listener as ITrainingplansAdapter
        } catch (e: ClassCastException) {
            Log.i(listener.toString(), " must implement ITrainingplansAdapter")
        }
    }

    fun setDataset(dataset: List<Trainingplan>) {
        mDataset = dataset
    }
}