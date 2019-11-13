package com.workoutlog.workoutlog.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.RepeatListener
import com.workoutlog.workoutlog.Timer
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.*
import com.workoutlog.workoutlog.ui.fragments.*
import java.sql.Date
import java.util.*

class WorkoutActivity : AppCompatActivity(),
    NormalExerciseFragment.INormalSetDoneListener,
    SupersetFragment.ISupersetDoneListener,
    DropsetFragment.IDropsetDoneListener,
    WorkoutSetsDoneDialogFragment.IWorkoutSetsDoneDialog,
    WorkoutSetsNotDoneDialogFragment.IWorkoutSetsNotDoneDialog {

    override fun notDoneNext() {
        nextExercise()
    }

    override fun finishNormal(eId: Int, sets: Int, isExerciseFinished: Boolean) {
        if(isExerciseFinished) {
            nextExercise()
        } else {
            val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), eId)
            val dialog = WorkoutSetsNotDoneDialogFragment.newInstance(exercise, sets)
            dialog.show(supportFragmentManager, "notDoneFinish")
        }

    }

    override fun finishSuperset(eId1: Int, eId2: Int, sets: Int, isExerciseFinished: Boolean) {
        if(isExerciseFinished) {
            nextExercise()
        } else {
            val exercise1 = dbInitializer.getExerciseNameById(database.exerciseDao(), eId1)
            val exercise2 = dbInitializer.getExerciseNameById(database.exerciseDao(), eId2)
            val dialog = WorkoutSetsNotDoneDialogFragment.newInstance("$exercise1 - $exercise2", sets)
            dialog.show(supportFragmentManager, "notDoneFinish")
        }
    }

    override fun finishDropset(eId: Int, sets: Int, isExerciseFinished: Boolean) {
        if(isExerciseFinished) {
            nextExercise()
        } else {
            val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), eId)
            val dialog = WorkoutSetsNotDoneDialogFragment.newInstance(exercise, sets)
            dialog.show(supportFragmentManager, "notDoneFinish")
        }

    }

    override fun next() {
        nextExercise()
    }

    override fun normalSetDone(normal: Normal, reps: Int, weight: Float, rpe: Float, pos: Int) {
        if(edId == null) {
            val date = Date(Calendar.getInstance().timeInMillis)
            val numberExcDones = dbInitializer.getNumberExerciseDonesOnDate(database.exerciseDoneDao(), date)
            val ed = ExerciseDone(0, date, normal.eId, numberExcDones)
            dbInitializer.insertExerciseDone(database.exerciseDoneDao(), ed)
            edId = dbInitializer.getLastEdId(database.exerciseDoneDao())
        }

        val setDone = SetDone(0, reps, weight, rpe, pos, edId!!)
        dbInitializer.insertSetDone(database.setDoneDao(), setDone)

        val setsDone = ((viewPager.adapter as PagerAdapterNormal).getFragment(0) as NormalExerciseFragment).getSets()
        if(setsDone >= normal.sets) {
            val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), normal.eId)
            val dialog = WorkoutSetsDoneDialogFragment.newInstance(exercise, setsDone)
            dialog.show(supportFragmentManager, "setsFinished")
        }
    }

    override fun supersetDone(superset: Superset, reps1: Int, weight1: Float, rpe1: Float, reps2: Int, weight2: Float, rpe2: Float, pos: Int) {
        if(edId == null) {
            val date = Date(Calendar.getInstance().timeInMillis)
            val numberExcDones = dbInitializer.getNumberExerciseDonesOnDate(database.exerciseDoneDao(), date)
            val ed = ExerciseDone(0, date, superset.eId1, numberExcDones)
            dbInitializer.insertExerciseDone(database.exerciseDoneDao(), ed)
            edId = dbInitializer.getLastEdId(database.exerciseDoneDao())
        }
        if(edId2 == null) {
            val date = Date(Calendar.getInstance().timeInMillis)
            val numberExcDones = dbInitializer.getNumberExerciseDonesOnDate(database.exerciseDoneDao(), date)
            val ed = ExerciseDone(0, date, superset.eId2, numberExcDones)
            dbInitializer.insertExerciseDone(database.exerciseDoneDao(), ed)
            edId2 = dbInitializer.getLastEdId(database.exerciseDoneDao())
        }

        val setDone1 = SetDone(0, reps1, weight1, rpe1, pos, edId!!)
        dbInitializer.insertSetDone(database.setDoneDao(), setDone1)
        val setDone2 = SetDone(0, reps2, weight2, rpe2, pos, edId2!!)
        dbInitializer.insertSetDone(database.setDoneDao(), setDone2)

        val setsDone = ((viewPager.adapter as PagerAdapterSuperset).getFragment(0) as SupersetFragment).getSets()
        if(setsDone >= superset.sets) {
            val exercise1 = dbInitializer.getExerciseNameById(database.exerciseDao(), superset.eId1)
            val exercise2 = dbInitializer.getExerciseNameById(database.exerciseDao(), superset.eId2)
            val dialog = WorkoutSetsDoneDialogFragment.newInstance("$exercise1 - $exercise2", setsDone)
            dialog.show(supportFragmentManager, "setsFinished")
        }
    }

    override fun dropsetDone(dropset: Dropset, reps: Int, weight: Float, pos: Int) {
        if(edId == null) {
            val date = Date(Calendar.getInstance().timeInMillis)
            val numberExcDones = dbInitializer.getNumberExerciseDonesOnDate(database.exerciseDoneDao(), date)
            val ed = ExerciseDone(0, date, dropset.eId, numberExcDones)
            dbInitializer.insertExerciseDone(database.exerciseDoneDao(), ed)
            edId = dbInitializer.getLastEdId(database.exerciseDoneDao())
        }

        val setDone = SetDone(0, reps, weight, 10f, pos, edId!!)
        dbInitializer.insertSetDone(database.setDoneDao(), setDone)

        val setsDone = ((viewPager.adapter as PagerAdapterDropset).getFragment(0) as DropsetFragment).getSets()
        if(setsDone >= dropset.sets) {
            val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), dropset.eId)
            val dialog = WorkoutSetsDoneDialogFragment.newInstance(exercise, setsDone)
            dialog.show(supportFragmentManager, "setsFinished")
        }
    }

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var buttonTimerPlay: ImageButton
    private lateinit var buttonTimerIncrease: ImageButton
    private lateinit var buttonTimerDecrease: ImageButton
    private lateinit var buttonTimerRefresh: ImageButton
    private lateinit var buttonNextExc: ImageButton
    private lateinit var buttonPrevExc: ImageButton
    private lateinit var buttonTimerPause: ImageButton
    private lateinit var textViewTimer: TextView

    private lateinit var normals: List<Normal>
    private lateinit var supersets: List<Superset>
    private lateinit var dropsets: List<Dropset>

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase
    private lateinit var timer: Timer

    private var breakInMillis: Long = 0
    private var pos = 0
    private var edId: Int? = null
    private var edId2: Int? = null

    private var deletable = false
    private lateinit var routine: Routine
    private lateinit var mWorkoutLog: WorkoutLog

    companion object {
        private const val DEFAULT_BREAK_TIME = 120
        private const val KEY_ROUTINE_WORKOUT = "routineWorkout"
        private const val NUM_PAGES = 3
        private const val KEY_DELETABLE = "deletable"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_workout)
        mWorkoutLog = this.applicationContext as WorkoutLog

        routine = intent.extras?.getParcelable(KEY_ROUTINE_WORKOUT)!!
        deletable = intent.extras!!.getBoolean(KEY_DELETABLE, false)

        dbInitializer = DatabaseInitializer.getInstance(this)
        database = AppDatabase.getInstance(this)

        normals = dbInitializer.getNormalsByRoutineId(database.normalDao(), routine.rId)
        supersets = dbInitializer.getSupersetsByRoutineId(database.supersetDao(), routine.rId)
        dropsets = dbInitializer.getDropsetsByRoutineId(database.dropsetDao(), routine.rId)

        viewPager = findViewById(R.id.view_pager_workout_activity)
        tabLayout = findViewById(R.id.tab_layout_workout_activity)
        tabLayout.setupWithViewPager(viewPager)

        toolbar = findViewById(R.id.toolbar_workout_activity)
        setSupportActionBar(toolbar)

        setPagerAdapter(pos)

        buttonNextExc = findViewById(R.id.button_next_exc_workout_activity)
        buttonPrevExc = findViewById(R.id.button_prev_exc_workout_activity)
        buttonTimerIncrease = findViewById(R.id.button_plus_pause_workout_activity)
        buttonTimerDecrease = findViewById(R.id.button_minus_pause_workout_activity)
        buttonTimerPlay = findViewById(R.id.button_play_pause_workout_activity)
        buttonTimerRefresh = findViewById(R.id.button_reset_pause_workout_activity)
        buttonTimerPause = findViewById(R.id.button_pause_pause_workout_activity)
        textViewTimer = findViewById(R.id.text_view_timer_workout_activity)

        setBreakInMillis(pos)
        timer = Timer(breakInMillis, 1000)
        refreshTextView()
        buttonNextExc.setOnClickListener {
            if(pos == normals.size + supersets.size + dropsets.size - 1) {
                val dialog = MessageDialogFragment.newInstance(getString(R.string.this_is_the_last_exercise))
                dialog.show(supportFragmentManager, "lastExercise")
            } else {
                nextExercise()
            }
        }
        buttonPrevExc.setOnClickListener {
            if(pos == 0) {
                val dialog = MessageDialogFragment.newInstance(getString(R.string.this_is_the_first_exercise))
                dialog.show(supportFragmentManager, "firstExercise")
            } else {
                prevExercise()
            }
        }

        buttonTimerDecrease.setOnTouchListener(RepeatListener(400, 50, View.OnClickListener {
            if(breakInMillis >= 1000 && !timer.isRunning()) {
                breakInMillis -= 1000
                refreshTimer()
                refreshTextView()
            }
        }))
        buttonTimerIncrease.setOnTouchListener(RepeatListener(400, 50, View.OnClickListener {
            if(breakInMillis <= 3599000 && !timer.isRunning()) {
                breakInMillis += 1000
                refreshTimer()
                refreshTextView()
            }
        }))
        timer.setTimerListener(object: Timer.ITimerListener {
            override fun onTimerFinished() {
                setBreakInMillis(pos)
                buttonTimerPlay.visibility = View.VISIBLE
                buttonTimerPause.visibility = View.INVISIBLE
            }

            override fun onTimerTicked(currentMillis: Long) {
                breakInMillis = currentMillis
                refreshTextView()
            }

        })
        buttonTimerPlay.setOnClickListener {
            buttonTimerPlay.visibility = View.INVISIBLE
            buttonTimerPause.visibility = View.VISIBLE
            timer.start()
        }
        buttonTimerRefresh.setOnClickListener {
            setBreakInMillis(pos)
            timer.reset(breakInMillis)
            refreshTextView()
            buttonTimerPlay.visibility = View.VISIBLE
            buttonTimerPause.visibility = View.INVISIBLE
        }
        buttonTimerPause.setOnClickListener {
            buttonTimerPlay.visibility = View.VISIBLE
            buttonTimerPause.visibility = View.INVISIBLE
            timer.pause()
        }
    }

    override fun onBackPressed() {
        val dialog = LeaveDialogFragment.newInstance(getString(R.string.workout), getString(R.string.sure_you_want_leave))
        dialog.setListener(object: LeaveDialogFragment.ILeave {
            override fun leave() {
                finish()
            }
        })
        dialog.show(supportFragmentManager, "leave")
    }

    override fun onDestroy() {
        clearReferences()
        if(deletable) {
            dbInitializer.deleteRoutine(database.routineDao(), routine)
        }
        super.onDestroy()
    }

    private fun setPagerAdapter(pos: Int): Boolean {
        normals.forEach {
            if(it.posInRoutine == pos) {
                viewPager.adapter = PagerAdapterNormal(supportFragmentManager, it)
                val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), it.eId)
                supportActionBar!!.title = exercise
                return true
            }
        }
        dropsets.forEach {
            if(it.posInRoutine == pos) {
                viewPager.adapter = PagerAdapterDropset(supportFragmentManager, it)
                val exercise = dbInitializer.getExerciseNameById(database.exerciseDao(), it.eId)
                supportActionBar!!.title = "$exercise ${getString(R.string._dropset_)}"
                return true
            }
        }
        supersets.forEach {
            if(it.posInRoutine == pos) {
                viewPager.adapter = PagerAdapterSuperset(supportFragmentManager, it)
                val exercise1 = dbInitializer.getExerciseNameById(database.exerciseDao(), it.eId1)
                val exercise2 = dbInitializer.getExerciseNameById(database.exerciseDao(), it.eId2)
                supportActionBar!!.title = "$exercise1 - $exercise2"
                return true
            }
        }
        return false
    }

    private fun nextExercise() {
        timer.pause()
        pos++
        val isFinished = !setPagerAdapter(pos)
        if(isFinished) {
            finish()
        } else {
            edId = null
            edId2 = null
            setBreakInMillis(pos)
            refreshTimer()
            refreshTextView()
            buttonTimerPlay.visibility = View.VISIBLE
            buttonTimerPause.visibility = View.INVISIBLE
        }
    }

    private fun prevExercise() {
        timer.pause()
        pos--
        setPagerAdapter(pos)
        edId = null
        edId2 = null
        setBreakInMillis(pos)
        refreshTimer()
        refreshTextView()
        buttonTimerPlay.visibility = View.VISIBLE
        buttonTimerPause.visibility = View.INVISIBLE
    }

    private fun refreshTimer() {
        timer.setMillis(breakInMillis)
    }

    private fun refreshTextView() {
        val seconds = breakInMillis / 1000
        val minutes = seconds / 60
        val leftSeconds = seconds % 60
        textViewTimer.text = String.format("%01d:%02d", minutes, leftSeconds)
    }

    private fun setBreakInMillis(pos: Int) {
        normals.forEach {
            if(it.posInRoutine == pos) {
                val seconds = it.breakInSeconds ?: DEFAULT_BREAK_TIME
                breakInMillis = seconds.toLong() * 1000
                return
            }
        }
        dropsets.forEach {
            if(it.posInRoutine == pos) {
                val seconds = it.breakInSeconds ?: DEFAULT_BREAK_TIME
                breakInMillis = seconds.toLong() * 1000
                return
            }
        }
        supersets.forEach {
            if(it.posInRoutine == pos) {
                val seconds = it.breakInSeconds ?: DEFAULT_BREAK_TIME
                breakInMillis = seconds.toLong() * 1000
                return
            }
        }
        breakInMillis = DEFAULT_BREAK_TIME.toLong()
    }

    private inner class PagerAdapterNormal(fm: FragmentManager, private val normal: Normal): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val registeredFragments = SparseArray<Fragment>()
        fun getFragment(position: Int): Fragment? = registeredFragments.get(position)

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> NormalExerciseFragment.newInstance(normal)
                1 -> HistoryFragmentWorkout.newInstance(normal.eId)
                2 -> GoalWorkoutFragment.newInstance(normal)
                else -> Fragment()
            }
        }

        override fun getCount() = NUM_PAGES

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment =  super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Exercise"
                1 -> "History"
                2 -> "Goal"
                else -> null
            }
        }
    }

    private inner class PagerAdapterSuperset(fm: FragmentManager, private val superset: Superset): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val registeredFragments = SparseArray<Fragment>()
        fun getFragment(position: Int): Fragment? = registeredFragments.get(position)

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> SupersetFragment.newInstance(superset)
                1 -> HistorySupersetFragmentWorkout.newInstance(superset.eId1, superset.eId2)
                2 -> GoalWorkoutFragment.newInstance(superset)
                else -> Fragment()
            }
        }

        override fun getCount() = NUM_PAGES

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment =  super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Exercise"
                1 -> "History"
                2 -> "Goal"
                else -> null
            }
        }
    }

    private inner class PagerAdapterDropset(fm: FragmentManager, private val dropset: Dropset): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val registeredFragments = SparseArray<Fragment>()
        fun getFragment(position: Int): Fragment? = registeredFragments.get(position)

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> DropsetFragment.newInstance(dropset)
                1 -> HistoryFragmentWorkout.newInstance(dropset.eId)
                2 -> GoalWorkoutFragment.newInstance(dropset)
                else -> Fragment()
            }
        }

        override fun getCount() = NUM_PAGES

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment =  super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Exercise"
                1 -> "History"
                2 -> "Goal"
                else -> null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mWorkoutLog.currentActivity = this
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    private fun clearReferences() {
        val currActivity = mWorkoutLog.currentActivity
        if (this == currActivity)
            mWorkoutLog.currentActivity = null
    }
}
