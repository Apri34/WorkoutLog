package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.adapters.RoutinesAdapter
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.database.entities.Trainingplan
import com.workoutlog.workoutlog.ui.fragments.*

class EditTrainingplanActivity : AppCompatActivity(),
    AddRoutineDialogFragment.IAddRoutine,
    RoutinesAdapter.IRoutinesAdapter,
    DeleteOrEditDialogFragment.IDeleteOrEditDialog<Routine>,
    ConfirmDeleteDialogFragment.IConfirmDelete<Routine>,
    EditTrainingplanDialogFragment.IEditTrainingplan {

    override fun routineClicked(rtn: Routine) {
        editRoutine(rtn)
    }

    override fun editTrainingplan(tpId: Int, tpName: String) {
        dbInitializer.updateTrainingplan(database.trainingplanDao(), Trainingplan(tpId, tpName))
        trainingplan = dbInitializer.getTrainingplanById(database.trainingplanDao(), tpId)
        supportActionBar!!.title = trainingplan.tpName
    }

    override fun delete(id: Int?, item: Routine) {
        if(id == 1) {
            dbInitializer.deleteRoutine(database.routineDao(), item)
            (recyclerView.adapter as RoutinesAdapter).setDataset(
                dbInitializer.getRoutinesByTpId(database.routineDao(), trainingplan.tpId)
            )
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    override fun delete(item: Routine) {
        if(isCurrentTp) {
            MessageDialogFragment.newInstance(getString(R.string.you_cant_add_or_delete_routines_in_yout_current_tp)).show(
                supportFragmentManager, "cant_delete_or_add_routine"
            )
        } else {
            val dialog = ConfirmDeleteDialogFragment<Routine>()
            dialog.setItem(item)
            dialog.setTitle(item.rName)
            dialog.setMessage(getString(R.string.really_delete_routine_question))
            dialog.setListener(this)
            dialog.setConfirmDeleteDialogId(1)
            dialog.show(supportFragmentManager, "confirmDelete")
        }
    }

    override fun edit(item: Routine) {
        editRoutine(item)
    }

    override fun routineLongClicked(rtn: Routine) {
        val dialog = DeleteOrEditDialogFragment<Routine>()
        dialog.setListener(this)
        dialog.setItem(rtn)
        dialog.show(supportFragmentManager, "deleteOrEdit")
    }

    override fun addRoutine(rName: String) {
        val posInTp = dbInitializer.getNumberRoutinesInTp(database.routineDao(), trainingplan.tpId) + 1
        val routine = Routine(0, rName, trainingplan.tpId, posInTp)
        dbInitializer.insertRoutine(database.routineDao(), routine)
        editRoutine(dbInitializer.getLastRoutine(database.routineDao()))
    }

    companion object {
        private const val TP_ID_KEY = "tpId"
        private const val ROUTINE_ID_KEY = "routineId"
        private const val KEY_TP_ID = "tpId"
        private const val IS_LIGHT_THEME = "isLightTheme"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd: ImageButton

    private lateinit var trainingplan: Trainingplan
    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    private var isCurrentTp = false
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
        setContentView(R.layout.activity_edit_trainingplan)
        mWorkoutLog = this.applicationContext as WorkoutLog

        isCurrentTp = getDefaultSharedPreferences(this).getInt(KEY_TP_ID, -1) == intent.extras!!.getInt(TP_ID_KEY)

        dbInitializer = DatabaseInitializer.getInstance(this)
        database = AppDatabase.getInstance(this)
        trainingplan = dbInitializer.getTrainingplanById(database.trainingplanDao(), intent.extras!!.getInt(TP_ID_KEY))

        toolbar = findViewById(R.id.toolbar_edit_trainingplan_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = trainingplan.tpName
        buttonAdd = findViewById(R.id.button_add_activity_edit_trainingplan)
        buttonAdd.setOnClickListener {
            addRoutine()
        }
        recyclerView = findViewById(R.id.recycler_view_edit_trainingplan_activity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        val routines = dbInitializer.getRoutinesByTpId(database.routineDao(), trainingplan.tpId)
        val adapter = RoutinesAdapter(routines)
        adapter.setListener(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        mWorkoutLog.currentActivity = this
        (recyclerView.adapter as RoutinesAdapter).setDataset(
            dbInitializer.getRoutinesByTpId(database.routineDao(), trainingplan.tpId)
        )
        recyclerView.adapter!!.notifyDataSetChanged()
        if(getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
            && getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false)
            && !isLightTheme
            || (!getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
                    || !getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false))
            && !isDarkTheme) {
            recreate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_edit_trainingplan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            item?.itemId == R.id.action_finished -> finish()
            item?.itemId == R.id.action_edit -> {
                val dialog = EditTrainingplanDialogFragment()
                dialog.setListener(this)
                dialog.setTrainingplan(trainingplan)
                dialog.show(supportFragmentManager, "editTrainingplanName")
            }
            item?.itemId == R.id.action_add -> addRoutine()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addRoutine() {
        if(isCurrentTp) {
            MessageDialogFragment.newInstance(getString(R.string.you_cant_add_or_delete_routines_in_yout_current_tp)).show(
                supportFragmentManager, "cant_delete_or_add_routine"
            )
        } else {
            val dialog = AddRoutineDialogFragment.getInstance(trainingplan.tpId)
            dialog.show(supportFragmentManager, "addRoutine")
        }
    }

    private fun editRoutine(routine: Routine) {
        val intent = Intent(this, EditRoutineActivity::class.java)
        intent.putExtra(ROUTINE_ID_KEY, routine.rId)
        startActivity(intent)
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
