package com.workoutlog.workoutlog.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Trainingplan
import java.lang.ClassCastException

class TrainingplansAdapter(private var mDataset: List<Trainingplan>) :
    RecyclerView.Adapter<TrainingplansAdapter.TrainingplansViewHolder>(){

private var listener: ITrainingplansAdapter? = null

    inner class TrainingplansViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trp: Trainingplan) {
            val tv = view.findViewById<TextView>(R.id.text_view_trainingplan_view_trainingplans)
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingplansViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_trainingplans, parent, false)

        return TrainingplansViewHolder(view)
    }

    override fun getItemCount() = mDataset.size

    override fun onBindViewHolder(holder: TrainingplansViewHolder, position: Int) {
        holder.bind(mDataset[position])
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