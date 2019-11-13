package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.ExerciseAdapterMultiselet
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.ui.fragments.CreateNormalForWorkoutDialogFragment

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    private lateinit var routine: Routine
    private var deletable = true
    private lateinit var mWorkoutLog: WorkoutLog

    companion object {
        private const val KEY_ROUTINE_WORKOUT = "routineWorkout"
        private const val KEY_DELETABLE = "deletable"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_create_routine)
        mWorkoutLog = this.applicationContext as WorkoutLog

        dbInitializer = DatabaseInitializer.getInstance(this)
        database = AppDatabase.getInstance(this)

        toolbar = findViewById(R.id.toolbar_create_workout_activity)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view_create_routine_activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        dbInitializer.insertRoutine(database.routineDao(), Routine(0, "singleRoutine", -1, 0))
        routine = dbInitializer.getLastRoutine(database.routineDao())
        val exercises = dbInitializer.getAllExercises(database.exerciseDao())
        val adapter = ExerciseAdapterMultiselet(exercises, routine.rId)
        adapter.setListener(object : ExerciseAdapterMultiselet.IShowDialog{
            override fun showDialog(dialog: CreateNormalForWorkoutDialogFragment) {
                dialog.show(supportFragmentManager, "createNormalForWorkout")
            }
        })

        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_create_current_tp, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_finished_tp -> {
                val normals = (recyclerView.adapter as ExerciseAdapterMultiselet).getNormals()
                if(normals.size == 0) {
                    Toast.makeText(this, getString(R.string.you_have_to_select_exercises), Toast.LENGTH_SHORT).show()
                } else {
                    normals.forEach {
                        dbInitializer.insertNormal(database.normalDao(), it)
                    }
                    val intent = Intent(this, WorkoutActivity::class.java)
                    intent.putExtra(KEY_ROUTINE_WORKOUT, routine)
                    intent.putExtra(KEY_DELETABLE, true)
                    startActivity(intent)
                    deletable = false
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        clearReferences()
        if(deletable)
            dbInitializer.deleteRoutine(database.routineDao(), routine)
        super.onDestroy()
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
