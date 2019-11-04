package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.views.Calender

class CreateCurrentTpChooseStartFragment: Fragment() {

    private lateinit var calender: Calender

    fun getStartDay() = calender.selectedDay
    fun getStartMonth() = calender.selectedMonth
    fun getStartYear() = calender.selectedYear
    private fun setStart(day: Int, month: Int, year: Int) {
        calender.setStart(day, month, year)
    }

    companion object {
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_current_tp_choose_day_to_start, container, false)

        val routines = arguments!!.getParcelableArrayList<Routine>("routines")
        val interval = arguments!!.getIntegerArrayList("interval")

        calender = view.findViewById(R.id.calender_choose_start_day)
        calender.setInterval(interval, routines)

        if(savedInstanceState != null) {
            val day = savedInstanceState.getInt(KEY_START_DAY, -1)
            val month = savedInstanceState.getInt(KEY_START_MONTH, -1)
            val year = savedInstanceState.getInt(KEY_START_YEAR, -1)
            calender.setStart(day, month, year)
        } else if(PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_START_DAY) &&
            PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_START_MONTH) &&
            PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_START_YEAR)) {
            setStart(PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_DAY, -1),
                PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_MONTH, -1),
                PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_YEAR, -1))
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val day = calender.selectedDay
        val month = calender.selectedMonth
        val year = calender.selectedYear
        outState.putInt(KEY_START_DAY, day)
        outState.putInt(KEY_START_MONTH, month)
        outState.putInt(KEY_START_YEAR, year)
    }
}