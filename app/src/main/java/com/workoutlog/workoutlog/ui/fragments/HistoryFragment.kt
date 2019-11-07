package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.HistoryAdapter
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.ExerciseDone
import com.workoutlog.workoutlog.database.entities.SetDone
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment: Fragment() {

    private lateinit var buttonNextDate: ImageButton
    private lateinit var buttonPrevDate: ImageButton
    private lateinit var textViewDate: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    private lateinit var exerciseDones: ArrayList<ExerciseDone>
    private lateinit var setDones: ArrayList<List<SetDone>>

    private lateinit var c: Calendar

    companion object {
        private const val CALENDAR_DAY = "day"
        private const val CALENDAR_MONTH = "month"
        private const val CALENDAR_YEAR = "year"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        buttonNextDate = view.findViewById(R.id.button_next_date_history_fragment)
        buttonPrevDate = view.findViewById(R.id.button_prev_date_history_fragment)
        textViewDate = view.findViewById(R.id.text_view_date_history_fragment)
        recyclerView = view.findViewById(R.id.recycler_view_history_fragment)

        dbInitializer = DatabaseInitializer.getInstance()
        database = AppDatabase.getInstance(context)

        exerciseDones = ArrayList()
        setDones = ArrayList()

        c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, +1)

        buttonNextDate.setOnClickListener { getNextWorkout() }
        buttonPrevDate.setOnClickListener { getPrevWorkout() }
        textViewDate.setOnClickListener { showDatePicker(it as TextView) }

        if(savedInstanceState == null && dbInitializer.existsNoPrevWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
            MessageDialogFragment.newInstance(getString(R.string.you_have_not_finished_any_workouts)).show(childFragmentManager, "noWorkoutsYet")
        }

        if(savedInstanceState != null) {
            c.set(savedInstanceState.getInt(CALENDAR_YEAR), savedInstanceState.getInt(CALENDAR_MONTH), savedInstanceState.getInt(CALENDAR_DAY))
            val date = Date(c.timeInMillis)
            exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), date) as ArrayList<ExerciseDone>
        } else {
            while (exerciseDones.isEmpty()) {
                val date = Date(c.timeInMillis)
                exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), date) as ArrayList<ExerciseDone>
                if (exerciseDones.isEmpty()) {
                    c.add(Calendar.DAY_OF_MONTH, -1)
                    if (dbInitializer.existsNoPrevWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
                        break
                    }
                }
            }
        }
        textViewDate.text = String.format(getString(R.string.date), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))

        if(exerciseDones.isNotEmpty()) {
            exerciseDones.forEach {
                setDones.add(dbInitializer.getSetDonesByEdId(database.setDoneDao(), it.edId))
            }
            val adapter = HistoryAdapter(context!!, exerciseDones, setDones)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        } else {
            textViewDate.setOnClickListener(null)
            textViewDate.isClickable = false
            buttonNextDate.isClickable = false
            buttonPrevDate.isClickable = false
            buttonNextDate.isEnabled = false
            buttonPrevDate.isEnabled = false
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CALENDAR_DAY, c.get(Calendar.DAY_OF_MONTH))
        outState.putInt(CALENDAR_MONTH, c.get(Calendar.MONTH))
        outState.putInt(CALENDAR_YEAR, c.get(Calendar.YEAR))
    }

    private fun getNextWorkout() {
        if(!dbInitializer.existsNoNextWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
            val prevC = c
            c.add(Calendar.DAY_OF_MONTH, +1)
            exerciseDones.clear()
            while (exerciseDones.isEmpty()) {
                val date = Date(c.timeInMillis)
                exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), date) as ArrayList<ExerciseDone>
                if(exerciseDones.isEmpty()) {
                    if(dbInitializer.existsNoNextWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
                        c = prevC
                        MessageDialogFragment.newInstance(getString(R.string.this_is_the_last_workout)).show(childFragmentManager, "lastWorkout")
                        return
                    }
                    c.add(Calendar.DAY_OF_MONTH, +1)
                }
            }

            setDones.clear()
            exerciseDones.forEach {
                setDones.add(dbInitializer.getSetDonesByEdId(database.setDoneDao(), it.edId))
            }

            (recyclerView.adapter as HistoryAdapter).setData(exerciseDones, setDones)
            textViewDate.text = String.format(getString(R.string.date), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))
        } else {
            MessageDialogFragment.newInstance(getString(R.string.this_is_the_last_workout)).show(childFragmentManager, "lastWorkout")
        }
    }

    private fun getPrevWorkout() {
        if(!dbInitializer.existsNoPrevWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
            val prevC = c
            c.add(Calendar.DAY_OF_MONTH, -1)
            exerciseDones.clear()
            while (exerciseDones.isEmpty()) {
                val date = Date(c.timeInMillis)
                exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), date) as ArrayList<ExerciseDone>
                if(exerciseDones.isEmpty()) {
                    if(dbInitializer.existsNoPrevWorkout(database.exerciseDoneDao(), Date(c.timeInMillis))) {
                        c = prevC
                        MessageDialogFragment.newInstance(getString(R.string.this_is_the_last_workout)).show(childFragmentManager, "lastWorkout")
                        return
                    }
                    c.add(Calendar.DAY_OF_MONTH, -1)
                }
            }

            setDones.clear()
            exerciseDones.forEach {
                setDones.add(dbInitializer.getSetDonesByEdId(database.setDoneDao(), it.edId))
            }

            (recyclerView.adapter as HistoryAdapter).setData(exerciseDones, setDones)
            textViewDate.text = String.format(getString(R.string.date), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))
        } else {
            MessageDialogFragment.newInstance(getString(R.string.this_is_the_first_workout)).show(childFragmentManager, "firstWorkout")
        }
    }

    private fun showDatePicker(textView: TextView) {
            val dialog = DatePickerDialog.newInstance({ view, year, monthOfYear, dayOfMonth ->
                c.set(year, monthOfYear, dayOfMonth)
                exerciseDones.clear()
                while (exerciseDones.isEmpty()) {
                    val date = Date(c.timeInMillis)
                    exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), date) as ArrayList<ExerciseDone>
                    if(exerciseDones.isEmpty())
                        c.add(Calendar.DAY_OF_MONTH, -1)
                }
                textView.text = String.format(getString(R.string.date), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))
                setDones.clear()
                exerciseDones.forEach {ed->
                    setDones.add(dbInitializer.getSetDonesByEdId(database.setDoneDao(), ed.edId))
                }
                (recyclerView.adapter as HistoryAdapter).setData(exerciseDones, setDones)
                view!!.dismiss()
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
            val selectableDays = ArrayList<Calendar>()
            val exerciseDones = dbInitializer.getAllExerciseDones(database.exerciseDoneDao())
            exerciseDones.forEach {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date.time
                selectableDays.add(cal)
            }
            val array = arrayOfNulls<Calendar>(selectableDays.size)
            selectableDays.toArray(array)
            dialog.selectableDays = array
            dialog.highlightedDays = array
            dialog.show(childFragmentManager, "selectDate")
    }
}