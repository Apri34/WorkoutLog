package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.TrainingplansAdapter
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Trainingplan

class CreateCurrentTpSelectTpFragment: Fragment(), TrainingplansAdapter.ITrainingplansAdapter {

    override fun trainingplanLongClicked(trp: Trainingplan) {}

    override fun trainingPlanClicked(trp: Trainingplan) {
        val routines = dbInitializer.getRoutinesByTpId(database.routineDao(), trp.tpId)
        if(routines.size != 0) {
            if (listener != null)
                listener!!.trainingplanSelected(trp)
        } else {
            Toast.makeText(context, getString(R.string.finish_this_plan_no_routines), Toast.LENGTH_SHORT).show()
        }
    }

    private var listener: ISelectTpFragment? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd: ImageButton

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    fun getSelectedItem(): Trainingplan? = (recyclerView.adapter as TrainingplansAdapter).getSelectedItem()

    companion object {
        private const val KEY_TP_ID = "tpId"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_current_tp_select_tp, container, false)

        dbInitializer = DatabaseInitializer.getInstance(context)
        database = AppDatabase.getInstance(context)

        buttonAdd = view.findViewById(R.id.button_add_create_current_tp_select_tp)
        buttonAdd.setOnClickListener { addTrainingplan() }
        recyclerView = view.findViewById(R.id.recycler_view_create_current_tp_select_tp)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.recyclerview_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        val trainingplans = dbInitializer.getAllTrainingplans(database.trainingplanDao())
        val adapter = TrainingplansAdapter(trainingplans)
        adapter.setListener(this)

        val selectedTp = PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(KEY_TP_ID, -1)
        if(selectedTp != -1) {
            for (i in 0 until trainingplans.size) {
                if(trainingplans[i].tpId == selectedTp) {
                    adapter.setSelectedItem(i)
                    break
                }
            }
        }
        recyclerView.adapter = adapter

        return view
    }

    private fun addTrainingplan() {
        if(listener != null)
            listener!!.addTrainingplan()
    }

    interface ISelectTpFragment {
        fun addTrainingplan()
        fun trainingplanSelected(trp: Trainingplan)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ISelectTpFragment
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}