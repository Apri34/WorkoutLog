package com.workoutlog.workoutlog.ui.fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.FullRoutineAdapter
import com.workoutlog.workoutlog.adapters.HistoryAdapter
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.database.entities.SetDone
import org.json.JSONObject
import java.sql.Date
import java.util.*

class HomeFragment: Fragment() {

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase
    private var listener: IHomeFragment? = null

    private var refreshed = true

    init {
        refreshed = true
    }

    companion object {
        private const val KEY_CURRENT_TP_STATE = "currentTpState"
        private const val NO_CURRENT_TP = 0
        private const val CURRENT_TP_FINISHED = 4
        private const val KEY_INTERVAL = "interval"
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"
        private const val KEY_TP_ID = "tpId"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dbInitializer = DatabaseInitializer.getInstance(context)
        database = AppDatabase.getInstance(context)

        val c = Calendar.getInstance()
        val date = Date(c.timeInMillis)
        val workoutDone = dbInitializer.existsWorkoutOnDate(database.exerciseDoneDao(), date)

        return if(workoutDone) {
            inflateFragmentWorkoutFinished(inflater, container)
        } else {
            if(PreferenceManager.getDefaultSharedPreferences(context).contains(KEY_CURRENT_TP_STATE) &&
                (PreferenceManager.getDefaultSharedPreferences(context).getInt(
                    KEY_CURRENT_TP_STATE,
                    NO_CURRENT_TP
                ) == CURRENT_TP_FINISHED)) {
                val tp = dbInitializer.getTrainingplanById(
                    database.trainingplanDao(),
                    PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TP_ID, -1))
                val day = c.get(Calendar.DAY_OF_MONTH)
                val month = c.get(Calendar.MONTH)
                val year = c.get(Calendar.YEAR)
                val rId = getRoutine(day, month, year, tp.tpId)
                if(rId == 0 || rId == null) {
                    inflateFragmentRestDay(inflater, container)
                } else {
                    inflateFragmentWorkoutNotFinished(inflater, container, rId)
                }
            } else {
                inflateFragmentNoCurrentTp(inflater, container)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(refreshed) refreshed = false
        else
            if(listener != null) refreshed = listener!!.refreshHomeFragment(this)
    }

    private fun inflateFragmentWorkoutFinished(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_home_workout_finished, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_fragment_home_workout_finished)
        val c = Calendar.getInstance()
        val exerciseDones = dbInitializer.getExerciseDonesByDate(database.exerciseDoneDao(), Date(c.timeInMillis))
        val setDones = ArrayList<List<SetDone>>()
        exerciseDones.forEach {
            setDones.add(dbInitializer.getSetDonesByEdId(database.setDoneDao(), it.edId))
        }
        val adapter = HistoryAdapter(context!!, exerciseDones, setDones)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    private fun inflateFragmentWorkoutNotFinished(inflater: LayoutInflater, container: ViewGroup?, rId: Int?): View {
        val view = inflater.inflate(R.layout.fragment_home_workout_not_finished, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_fragment_home_workout_not_finished)
        val textViewTp = view.findViewById<TextView>(R.id.text_view_trainingplan_home_fragment_workout_not_finished)
        val textViewRoutine = view.findViewById<TextView>(R.id.text_view_routine_home_fragment_workout_not_finished)
        val buttonEditTp = view.findViewById<ImageButton>(R.id.button_edit_trainingplan_home_fragment_workout_not_finished)
        val buttonEditRoutine = view.findViewById<ImageButton>(R.id.button_edit_routine_home_fragment_workout_not_finished)
        if(rId == null) return view
        val routine = dbInitializer.getRoutineById(database.routineDao(), rId)

        val adapter = FullRoutineAdapter(context!!, routine)
        adapter.setListener(object : FullRoutineAdapter.IButtonClicked {
            override fun startWorkout() {
                if(listener != null) listener!!.startWorkout(routine)
            }

            override fun pauseToday() {
                if(listener != null) listener!!.editCurrentTpDayStart()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler_view_between_items_home_fragment)))

        val tp = dbInitializer.getTrainingplanById(database.trainingplanDao(), routine.tpId)
        textViewTp.text = tp.tpName
        textViewRoutine.text = routine.rName

        buttonEditTp.setOnClickListener {
            if(listener != null) listener!!.editCurrentTp()
        }

        buttonEditRoutine.setOnClickListener {
            if(listener != null) listener!!.editCurrentTpInterval()
        }
        return view
    }

    private fun inflateFragmentRestDay(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_home_restday, container, false)
        val buttonHistory = view.findViewById<Button>(R.id.button_history_home_fragment_restday)
        val buttonEditTp = view.findViewById<Button>(R.id.button_edit_tp_home_fragment_restday)
        buttonHistory.setOnClickListener {
            if(listener != null) listener!!.goToHistory()
        }
        buttonEditTp.setOnClickListener {
            if(listener != null) listener!!.editCurrentTp()
        }
        return view
    }

    private fun inflateFragmentNoCurrentTp(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_home_no_current_tp, container, false)
        val buttonChooseTp = view.findViewById<Button>(R.id.button_choose_tp_home_fragment_current_tp_not_chosen)
        val buttonHistory = view.findViewById<Button>(R.id.button_history_home_fragment_current_tp_not_chosen)
        buttonChooseTp.setOnClickListener {
            if(listener != null) listener!!.chooseTp()
        }
        buttonHistory.setOnClickListener {
            if(listener != null) listener!!.goToHistory()
        }
        return view
    }


    private fun getRoutine(day: Int, month: Int, year: Int, tpId: Int): Int? {
        val routines = dbInitializer.getRoutinesByTpId(database.routineDao(), tpId)
        val interval = getArrayListFromJsonString(PreferenceManager.getDefaultSharedPreferences(context).getString(
            KEY_INTERVAL, "")!!)
        if (routines == null || interval.size == 0) return null

        val c = Calendar.getInstance()
        c.set(year, month, day, 0, 0, 0)
        val timeNow = c.timeInMillis

        val selectedYear = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_YEAR, -1)
        val selectedMonth = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_MONTH, -1)
        val selectedDay = PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_START_DAY, -1)
        if(selectedDay == -1 || selectedMonth == -1 || selectedYear == -1) return null
        c.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
        val timeStart = c.timeInMillis
        if(timeStart > timeNow) return null
        val dif = timeNow - timeStart
        val days = Math.round(dif.toFloat() / (1000 * 60 * 60 * 24).toFloat())
        val dayInInterval = days % interval.size
        return interval[dayInInterval]
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

    interface IHomeFragment {
        fun startWorkout(routine: Routine)
        fun editCurrentTpDayStart()
        fun editCurrentTp()
        fun editCurrentTpInterval()
        fun goToHistory()
        fun chooseTp()
        fun refreshHomeFragment(fragment: Fragment): Boolean
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IHomeFragment
        } catch (e: ClassCastException) {
            Log.i(context.toString(), " must implement IHomeFragment")
        }
    }

    inner class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                left =  spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }
}