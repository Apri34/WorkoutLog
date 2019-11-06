package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Trainingplan
import com.workoutlog.workoutlog.views.CustomIntervalCreator
import com.workoutlog.workoutlog.views.IntervalPicker
import org.json.JSONObject
import java.util.*

class CreateCurrentTpChooseIntervalFragment: Fragment() {

    private lateinit var trainingplan: Trainingplan
    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase
    private lateinit var intervalPicker: IntervalPicker
    private lateinit var customIntervalCreator: CustomIntervalCreator

    private var isCustomInterval = false
    fun isInCustomInterval() = isCustomInterval
    fun isCustomIntervalFinished() = customIntervalCreator.isCustomIntervalFinished
    fun returnFromCustomInterval() {
        customIntervalCreator.removeCustomInterval()
        customIntervalCreator.visibility = View.GONE
        intervalPicker.visibility = View.VISIBLE
        isCustomInterval = false
    }
    fun getInterval(): ArrayList<Int> = customIntervalCreator.interval
    fun getSelectedInterval(): ArrayList<Int>? = intervalPicker.selectedInterval

    private var listener: IIntervalChosen? = null

    companion object {
        private const val KEY_TP_ID = "tpId"
        private const val KEY_INTERVAL = "interval"
        private const val KEY_IS_IN_CUSTOM_INTERVAL = "isIncustomInterval"
        private const val KEY_CUSTOM_INTERVAL = "customInterval"
        private const val KEY_SELECTED_INTERVAL = "selectedInterval"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_current_tp_choose_interval, container, false)

        dbInitializer = DatabaseInitializer.getInstance()
        database = AppDatabase.getInstance(context)

        trainingplan = dbInitializer.getTrainingplanById(database.trainingplanDao(), arguments!!.getInt(KEY_TP_ID))
        intervalPicker = view.findViewById(R.id.interval_picker)
        intervalPicker.setIntervalClickedListener(object : IntervalPicker.IIntervalClickedListener {
            override fun intervalClicked(interval: ArrayList<Int>) {
                if(listener != null) {
                    listener!!.intervalChosen(interval)
                }
            }

            override fun customInterval() {
                intervalPicker.visibility = View.GONE
                customIntervalCreator.visibility = View.VISIBLE
                isCustomInterval = true
            }

        })
        intervalPicker.setTrainingplan(trainingplan)

        customIntervalCreator = view.findViewById(R.id.custom_interval_creator)
        customIntervalCreator.setIntervalCreatedListener { interval ->
            if(listener != null)
                listener!!.intervalChosen(interval)
        }
        customIntervalCreator.setTrainingplan(trainingplan)

        if(savedInstanceState != null) {
            isCustomInterval = savedInstanceState.getBoolean(KEY_IS_IN_CUSTOM_INTERVAL)
            if(isCustomInterval) {
                intervalPicker.visibility = View.GONE
                customIntervalCreator.visibility = View.VISIBLE
                customIntervalCreator.interval = savedInstanceState.getIntegerArrayList(KEY_CUSTOM_INTERVAL)
            } else {
                intervalPicker.setInterval(savedInstanceState.getIntegerArrayList(KEY_SELECTED_INTERVAL))
                intervalPicker.refreshButtons()
            }
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_INTERVAL)) {
                val interval = getArrayListFromJsonString(
                    PreferenceManager.getDefaultSharedPreferences(context).getString(
                        KEY_INTERVAL, ""
                    )!!
                )

                isCustomInterval = intervalPicker.setInterval(interval)
                if (isCustomInterval) {
                    intervalPicker.visibility = View.GONE
                    customIntervalCreator.visibility = View.VISIBLE
                    customIntervalCreator.interval = interval
                }
            }
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_IN_CUSTOM_INTERVAL, isInCustomInterval())
        if(isInCustomInterval()) {
            outState.putIntegerArrayList(KEY_CUSTOM_INTERVAL, customIntervalCreator.interval)
        } else {
            outState.putIntegerArrayList(KEY_SELECTED_INTERVAL, intervalPicker.selectedInterval)
        }
    }

    interface IIntervalChosen {
        fun intervalChosen(interval: ArrayList<Int>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IIntervalChosen
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IIntervalChosen")
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
}