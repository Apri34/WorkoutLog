package com.workoutlog.workoutlog.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.workoutlog.workoutlog.ui.activities.EditTrainingplanActivity

class TrainingplansFragment: Fragment(),
    TrainingplansAdapter.ITrainingplansAdapter,
    DeleteOrEditDialogFragment.IDeleteOrEditDialog<Trainingplan>,
    ConfirmDeleteDialog.IConfirmDelete<Trainingplan>,
    AddTrainingplanDialogFragment.IAddTrainingplan {

    override fun trainingPlanClicked(trp: Trainingplan) {
        editTrainingplan(trp)
    }

    override fun addTrainingplan(tpName: String) {
        val trainingplan = Trainingplan(0, tpName)
        dbInitializer.insertTrainingplan(database.trainingplanDao(), trainingplan)
        editTrainingplan(dbInitializer.getLastTrainingplan(database.trainingplanDao()))
    }

    override fun delete(id: Int?, item: Trainingplan) {
        if(id == 1) {
            dbInitializer.deleteTrainingplan(database.trainingplanDao(), item)
            (recyclerView.adapter as TrainingplansAdapter).setDataset(
                dbInitializer.getAllTrainingplans(database.trainingplanDao())
            )
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    override fun edit(item: Trainingplan) {
        editTrainingplan(item)
    }

    override fun delete(item: Trainingplan) {
        if (PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_CURRENT_TP_STATE) &&
                PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_CURRENT_TP_STATE, -1) >= TP_SELECTED) {
            val tpId = PreferenceManager.getDefaultSharedPreferences(context).getInt(TP_ID_KEY, -1)
            if(item.tpId == tpId) {
                val dialog = MessageDialogFragment.newInstance(getString(R.string.current_tp_cant_delete))
                dialog.show(childFragmentManager, "tpNotDeleted")
                return
            }
        }
        val dialog = ConfirmDeleteDialog<Trainingplan>()
        dialog.setConfirmDeleteDialogId(1)
        dialog.setItem(item)
        dialog.setListener(this)
        dialog.setMessage(getString(R.string.really_delete_trainingplan_question))
        dialog.setTitle(item.tpName)
        dialog.show(childFragmentManager, "deleteTrainingplan")
    }

    override fun trainingplanLongClicked(trp: Trainingplan) {
        val dialog = DeleteOrEditDialogFragment<Trainingplan>()
        dialog.setItem(trp)
        dialog.setListener(this)
        dialog.show(childFragmentManager, "deleteOrEdit")
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd: ImageButton

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    companion object {
        const val TP_ID_KEY = "tpId"
        private const val TP_SELECTED = 1
        private const val KEY_CURRENT_TP_STATE = "currentTpState"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trainingplans, container, false)

        dbInitializer = DatabaseInitializer.getInstance()
        database = AppDatabase.getInstance(context)

        recyclerView = view.findViewById(R.id.recycler_view_fragment_trainingplans)
        buttonAdd = view.findViewById(R.id.button_add_fragment_trainingplans)
        buttonAdd.setOnClickListener { addTrainingplan() }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.recyclerview_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        val adapter = TrainingplansAdapter(dbInitializer.getAllTrainingplans(database.trainingplanDao()))
        adapter.setListener(this)
        recyclerView.adapter = adapter

        return view
    }

    private fun addTrainingplan() {
        val dialog = AddTrainingplanDialogFragment()
        dialog.setListener(this)
        dialog.show(childFragmentManager, "addTrainingplan")
    }

    private fun editTrainingplan(trainingplan: Trainingplan) {
        val intent = Intent(context, EditTrainingplanActivity::class.java)
        intent.putExtra(TP_ID_KEY, trainingplan.tpId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        (recyclerView.adapter as TrainingplansAdapter).setDataset(
            dbInitializer.getAllTrainingplans(database.trainingplanDao())
        )
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}