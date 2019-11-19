package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.TypedValue
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.ChooseRoutineAdapter
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine


class ChooseRoutineActivity : AppCompatActivity() {

    companion object {
        private const val KEY_ROUTINE_WORKOUT = "routineWorkout"
        private const val KEY_DELETABLE = "deletable"
        private const val IS_LIGHT_THEME = "isLightTheme"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase
    private lateinit var mWorkoutLog: WorkoutLog
    private var isLightTheme = false
    private var isDarkTheme = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
            && getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false)) {
            setTheme(R.style.AppTheme_LIGHT)
            isLightTheme = true
            isDarkTheme = false
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.topbarColor, typedValue, true)
            window.statusBarColor = typedValue.data
        }
        setContentView(R.layout.activity_choose_routine)
        mWorkoutLog = this.applicationContext as WorkoutLog

        dbInitializer = DatabaseInitializer.getInstance(this)
        database = AppDatabase.getInstance(this)

        toolbar = findViewById(R.id.toolbar_choose_workout_activity)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view_choose_routine_activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        val routines = ArrayList<List<Routine>>()
        val trainingplans = dbInitializer.getAllTrainingplans(database.trainingplanDao())
        trainingplans.forEach {
            val r = dbInitializer.getRoutinesByTpId(database.routineDao(), it.tpId)
            if(r.size != 0) routines.add(r)
        }
        val adapter = ChooseRoutineAdapter(this, routines)
        adapter.setListener(object : ChooseRoutineAdapter.IChooseRoutineAdapter {
            override fun routineSelected(routine: Routine) {
                val intent = Intent(this@ChooseRoutineActivity, WorkoutActivity::class.java)
                intent.putExtra(KEY_ROUTINE_WORKOUT, routine)
                intent.putExtra(KEY_DELETABLE, false)
                startActivity(intent)
                finish()
            }
        })
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        mWorkoutLog.currentActivity = this
        if(getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
            && getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false)
            && !isLightTheme
            || (!getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
                    || !getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false))
            && !isDarkTheme) {
            recreate()
        }
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        val currActivity = mWorkoutLog.currentActivity
        if (this == currActivity)
            mWorkoutLog.currentActivity = null
    }
}
