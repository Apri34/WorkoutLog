package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.views.Calender
import org.json.JSONObject
import java.util.*

class CurrentTrainingplanFragment: Fragment(), ConfirmDeleteCurrentTpDialogFragment.IConfirmDeleteCurrentTpDialog {

    override fun deleteAndCreateNew() {
        if(listener != null) listener!!.createNewCurrentTrainingplan()
    }

    private lateinit var buttonCreateTrainingplan: Button
    private lateinit var textViewTrainingplan: TextView
    private lateinit var buttonEditTrainingplan: ImageButton
    private lateinit var linearLayoutInterval: LinearLayout
    private lateinit var buttonEditInterval: ImageButton
    private lateinit var calender: Calender
    private lateinit var buttonEditCalender: ImageButton
    private lateinit var textViewDeloadCycles: TextView
    private lateinit var textViewDeloadVolume: TextView
    private lateinit var textViewDeloadWeight: TextView
    private lateinit var buttonEditDeload: ImageButton
    private lateinit var buttonCreateNew: Button

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    private var created = false
    private var refreshed = true

    private var listener: ICurrentTrainingplanFragment? = null

    companion object {
        private const val NO_CURRENT_TP = 0
        private const val CURRENT_TP_FINISHED = 4
        private const val KEY_CURRENT_TP_STATE = "currentTpState"
        private const val KEY_TP_ID = "tpId"
        private const val KEY_INTERVAL = "interval"
        private const val KEY_DELOAD_CYCLES = "deloadCycles"
        private const val KEY_DELOAD_VOLUME = "deloadVolume"
        private const val KEY_DELOAD_WEIGHT = "deloadWeight"
        private const val KEY_DELOAD_SET = "deloadSet"
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"
    }

    init {
        refreshed = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        created = PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_CURRENT_TP_STATE) &&
                PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP) == CURRENT_TP_FINISHED
        dbInitializer = DatabaseInitializer.getInstance(context)
        database = AppDatabase.getInstance(context)
        return if(created) {
            currentTpCreatedView(inflater, container)
        } else {
            currentTpNotCreatedView(inflater, container)
        }
    }

    override fun onResume() {
        super.onResume()
        if(refreshed) refreshed = false
        else
            if(listener != null) refreshed = listener!!.refreshCurrentTrainingplanFragment(this)
    }

    interface ICurrentTrainingplanFragment {
        fun createCurrentTrainingplan()
        fun editTrainingplan()
        fun editInterval()
        fun editStartDay()
        fun editDeload()
        fun createNewCurrentTrainingplan()
        fun refreshCurrentTrainingplanFragment(fragment: Fragment): Boolean
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ICurrentTrainingplanFragment
        } catch (e: ClassCastException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun getArrayListFromJsonString(json: String): ArrayList<Int> {
        val list = ArrayList<Int>()
        val obj = JSONObject(json)
        var x = 0
        while(obj.has(x.toString())) {
            list.add(obj.getInt(x.toString()))
            x++
        }
        return list
    }

    private fun createIntervalItem(routine: String): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        tv.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        val textSize = resources.getDimensionPixelSize(R.dimen.text_size_create_current_tp_created_items)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        tv.text = routine
        return tv
    }

    private fun noDeload() {
        textViewDeloadCycles.text = getString(R.string.no_deload)
        textViewDeloadWeight.visibility = View.INVISIBLE
        textViewDeloadVolume.visibility = View.INVISIBLE
    }

    private fun currentTpCreatedView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_current_trainingplan_created, container, false)

        textViewTrainingplan = view.findViewById(R.id.text_view_current_trainingplan_trainingplan)
        buttonEditTrainingplan = view.findViewById(R.id.image_button_edit_trainingplan_current_trainingplan)
        linearLayoutInterval = view.findViewById(R.id.linear_layout_current_trainingplan_interval)
        buttonEditInterval = view.findViewById(R.id.image_button_edit_interval_current_trainingplan)
        calender = view.findViewById(R.id.calender_current_trainingplan)
        buttonEditCalender = view.findViewById(R.id.image_button_edit_interval_current_calender)
        textViewDeloadCycles = view.findViewById(R.id.text_view_current_trainingplan_deload_cycles)
        textViewDeloadVolume = view.findViewById(R.id.text_view_current_trainingplan_deload_volume)
        textViewDeloadWeight = view.findViewById(R.id.text_view_current_trainingplan_deload_weight)
        buttonEditDeload = view.findViewById(R.id.image_button_edit_interval_current_deload)
        buttonCreateNew = view.findViewById(R.id.button_create_new_current_trainingplan)

        val tpId = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TP_ID, -1)
        if(tpId != -1) {
            val tp = dbInitializer.getTrainingplanById(database.trainingplanDao(), tpId)
            textViewTrainingplan.text = tp.tpName
        }
        buttonEditTrainingplan.setOnClickListener {
            if(listener != null) listener!!.editTrainingplan()
        }

        val sInterval = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_INTERVAL, "")

        val routines = dbInitializer.getRoutinesByTpId(database.routineDao(), tpId)
        val interval = getArrayListFromJsonString(sInterval!!)
        interval.forEach { i->
            if(i == 0) {
                linearLayoutInterval.addView(createIntervalItem(getString(R.string.pause)))
            } else {
                routines.forEach {r ->
                    if(r.rId == i) {
                        linearLayoutInterval.addView(createIntervalItem(r.rName))
                    }
                }
            }
        }
        buttonEditInterval.setOnClickListener {
            if(listener != null) listener!!.editInterval()
        }

        calender.setInterval(interval, routines)
        val sDay = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_DAY, -1)
        val sMonth = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_MONTH, -1)
        val sYear = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_YEAR, -1)
        calender.setStart(sDay, sMonth, sYear)
        buttonEditCalender.setOnClickListener {
            if(listener != null) listener!!.editStartDay()
        }

        val isDeloadSet = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_DELOAD_SET, false)
        if(isDeloadSet) {
            val delCycles = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_DELOAD_CYCLES, -1)
            val delVolume = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_DELOAD_VOLUME, -1)
            val delWeight = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_DELOAD_WEIGHT, -1)
            if(delCycles != -1 && delVolume != -1 && delWeight != -1) {
                textViewDeloadCycles.text = String.format(getString(R.string.deload_after_cycles), delCycles)
                textViewDeloadVolume.text = String.format(getString(R.string.volume_), delVolume)
                textViewDeloadWeight.text = String.format(getString(R.string.weight_), delWeight)
            } else {
                noDeload()
            }
        } else {
            noDeload()
        }

        buttonEditDeload.setOnClickListener {
            if(listener != null) listener!!.editDeload()
        }

        buttonCreateNew.setOnClickListener {
            val dialog = ConfirmDeleteCurrentTpDialogFragment.newInstance(getString(R.string.create_new_current_trainingplan), getString(
                R.string.create_new_current_tp_old_one_gets_deleted))
            dialog.setListener(this)
            dialog.show(childFragmentManager, "CreateNewTrainingplan")
        }

        return view
    }

    private fun currentTpNotCreatedView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_current_trainingplan_not_created, container, false)

        buttonCreateTrainingplan = view.findViewById(R.id.button_create_current_tp_fragment_not_created)

        if(PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP) > NO_CURRENT_TP)
            buttonCreateTrainingplan.text = getString(R.string.finish_current_tp)

        buttonCreateTrainingplan.setOnClickListener {
            if(listener != null) listener!!.createCurrentTrainingplan()
        }

        return view
    }
}