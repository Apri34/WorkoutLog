package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Trainingplan
import com.workoutlog.workoutlog.ui.fragments.*
import com.workoutlog.workoutlog.views.PageIdenticator
import org.json.JSONObject
import java.util.*

class CreateCurrentTrainingplanActivity : AppCompatActivity(),
    CreateCurrentTpSelectTpFragment.ISelectTpFragment,
    CreateCurrentTpChooseIntervalFragment.IIntervalChosen,
    CreateCurrentTpDeloadFragment.IDeloadFragment
{

    override fun noDeload() {
        getDefaultSharedPreferences(this).edit()
            .putBoolean(KEY_DELOAD_SET, false)
            .putInt(KEY_CURRENT_TP_STATE, CURRENT_TP_FINISHED)
            .remove(KEY_DELOAD_WEIGHT)
            .remove(KEY_DELOAD_CYCLES)
            .remove(KEY_DELOAD_VOLUME)
            .apply()
        finish()
    }

    override fun intervalChosen(interval: ArrayList<Int>) {
        getDefaultSharedPreferences(this).edit().putString(KEY_INTERVAL, getJsonStringFromArrayList(interval))
            .putInt(KEY_CURRENT_TP_STATE, INTERVAL_CHOSEN).apply()
        addChooseStartFragment(false)
    }

    override fun addTrainingplan() {
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("openTrainingplans", true)
        startActivity(intent)
    }
    override fun trainingplanSelected(trp: Trainingplan) {
        val currentTpId = getDefaultSharedPreferences(this).getInt(KEY_TP_ID, -1)
        getDefaultSharedPreferences(this).edit().putInt(KEY_TP_ID, trp.tpId)
            .putInt(KEY_CURRENT_TP_STATE, TP_SELECTED).apply()
        if(currentTpId != trp.tpId) {
            getDefaultSharedPreferences(this).edit()
                .putInt(KEY_CURRENT_TP_STATE, TP_SELECTED)
                .remove(KEY_INTERVAL)
                .remove(KEY_START_DAY)
                .remove(KEY_START_MONTH)
                .remove(KEY_START_YEAR)
                .apply()
        }
        addChooseIntervalFragment(false)
    }

    private lateinit var toolbar: Toolbar
    private lateinit var pageIdenticator: PageIdenticator

    private lateinit var fragmentChooseInterval: CreateCurrentTpChooseIntervalFragment
    private lateinit var fragmentChooseStart: CreateCurrentTpChooseStartFragment
    private lateinit var fragmentDeload: CreateCurrentTpDeloadFragment
    private lateinit var fragmentSelectTp: CreateCurrentTpSelectTpFragment

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase
    private lateinit var mWorkoutLog: WorkoutLog

    companion object {
        private const val KEY_FRAGMENT = "fragment"
        private const val KEY_FRAG_CHOOSE_INTERVAL = "chooseInterval"
        private const val KEY_FRAG_CHOOSE_START = "chooseStart"
        private const val KEY_FRAG_DELOAD = "deload"
        private const val KEY_FRAG_SELECT_TP = "selectTp"
        private const val NO_CURRENT_TP = 0
        private const val TP_SELECTED = 1
        private const val INTERVAL_CHOSEN = 2
        private const val START_DAY_CHOSEN = 3
        private const val CURRENT_TP_FINISHED = 4
        private const val KEY_CURRENT_TP_STATE = "currentTpState"
        private const val KEY_TP_ID = "tpId"
        private const val KEY_INTERVAL = "interval"
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"
        private const val KEY_DELOAD_CYCLES = "deloadCycles"
        private const val KEY_DELOAD_VOLUME = "deloadVolume"
        private const val KEY_DELOAD_WEIGHT = "deloadWeight"
        private const val KEY_DELOAD_SET = "deloadSet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_create_current_trainingplan)
        mWorkoutLog = this.applicationContext as WorkoutLog

        dbInitializer = DatabaseInitializer.getInstance(this)
        database = AppDatabase.getInstance(this)

        if(savedInstanceState != null) {
            fragmentChooseInterval = if(supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_CHOOSE_INTERVAL) != null)
                supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_CHOOSE_INTERVAL) as CreateCurrentTpChooseIntervalFragment
            else
                CreateCurrentTpChooseIntervalFragment()
            fragmentChooseStart = if(supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_CHOOSE_START) != null)
                supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_CHOOSE_START) as CreateCurrentTpChooseStartFragment
            else
                CreateCurrentTpChooseStartFragment()
            fragmentDeload = if(supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_DELOAD) != null)
                supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_DELOAD) as CreateCurrentTpDeloadFragment
            else
                CreateCurrentTpDeloadFragment()
            fragmentSelectTp = if(supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_SELECT_TP) != null)
                supportFragmentManager.getFragment(savedInstanceState, KEY_FRAG_SELECT_TP) as CreateCurrentTpSelectTpFragment
            else
                CreateCurrentTpSelectTpFragment()
        } else {
            fragmentChooseInterval = CreateCurrentTpChooseIntervalFragment()
            fragmentChooseStart = CreateCurrentTpChooseStartFragment()
            fragmentDeload = CreateCurrentTpDeloadFragment()
            fragmentSelectTp = CreateCurrentTpSelectTpFragment()
        }

        toolbar = findViewById(R.id.toolbar_create_current_trainingplan_activity)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        setSupportActionBar(toolbar)

        pageIdenticator = findViewById(R.id.page_identicator_activity_current_tp)
        val fragment = intent.extras?.getString(KEY_FRAGMENT)

        if(savedInstanceState == null) {
            when (getDefaultSharedPreferences(this).getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP)) {
                NO_CURRENT_TP -> {
                    addSelectTpFragment(false)
                    supportActionBar!!.title = getString(R.string.select_a_trainingplan)
                }
                TP_SELECTED -> {
                    addSelectTpFragment(false)
                    addChooseIntervalFragment(false)
                    supportActionBar!!.title = getString(R.string.choose_an_interval)
                }
                INTERVAL_CHOSEN -> {
                    addSelectTpFragment(false)
                    addChooseIntervalFragment(false)
                    addChooseStartFragment(false)
                    supportActionBar!!.title = getString(R.string.choose_a_day_to_start)
                }
                START_DAY_CHOSEN -> {
                    addSelectTpFragment(false)
                    addChooseIntervalFragment(false)
                    addChooseStartFragment(false)
                    addDeloadFragment()
                    supportActionBar!!.title = getString(R.string.deload)
                }
                CURRENT_TP_FINISHED -> {
                    if (fragment == null) finish()
                    else {
                        when (fragment) {
                            KEY_FRAG_SELECT_TP -> {
                                addSelectTpFragment(false)
                            }
                            KEY_FRAG_CHOOSE_INTERVAL -> {
                                addSelectTpFragment(false)
                                addChooseIntervalFragment(false)
                            }
                            KEY_FRAG_CHOOSE_START -> {
                                addSelectTpFragment(false)
                                addChooseIntervalFragment(false)
                                addChooseStartFragment(false)
                            }
                            KEY_FRAG_DELOAD -> {
                                addSelectTpFragment(false)
                                addChooseIntervalFragment(false)
                                addChooseStartFragment(false)
                                addDeloadFragment()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(fragmentChooseInterval.isAdded)
            supportFragmentManager.putFragment(outState, KEY_FRAG_CHOOSE_INTERVAL, fragmentChooseInterval)
        if(fragmentChooseStart.isAdded)
            supportFragmentManager.putFragment(outState, KEY_FRAG_CHOOSE_START, fragmentChooseStart)
        if(fragmentDeload.isAdded)
            supportFragmentManager.putFragment(outState, KEY_FRAG_DELOAD, fragmentDeload)
        if(fragmentSelectTp.isAdded)
            supportFragmentManager.putFragment(outState, KEY_FRAG_SELECT_TP, fragmentSelectTp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_create_current_tp, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            android.R.id.home -> {
                when {
                    fragmentDeload.isAdded -> {
                        getPrevFragment()
                        pageIdenticator.prevPage()
                        supportActionBar!!.title = getString(R.string.choose_a_day_to_start)
                    }
                    fragmentChooseStart.isAdded -> {
                        getPrevFragment()
                        pageIdenticator.prevPage()
                        supportActionBar!!.title = getString(R.string.choose_an_interval)
                    }
                    fragmentChooseInterval.isAdded -> {
                        if(fragmentChooseInterval.isInCustomInterval()) fragmentChooseInterval.returnFromCustomInterval()
                        else {
                            getPrevFragment()
                            pageIdenticator.prevPage()
                            supportActionBar!!.title = getString(R.string.select_a_trainingplan)
                        }
                    }
                    fragmentSelectTp.isAdded -> {
                        val dialog = LeaveDialogFragment.newInstance(getString(R.string.current_trainingplan), getString(R.string.you_are_about_to_leave))
                        dialog.setListener(object: LeaveDialogFragment.ILeave {
                            override fun leave() {
                                finish()
                            }
                        })
                        dialog.show(supportFragmentManager, "leave")
                    }
                }
                true
            }
            R.id.action_finished_tp -> {
                when {
                    fragmentDeload.isAdded -> {
                        val cycles = fragmentDeload.getCycles()
                        val volume = fragmentDeload.getVolume()
                        val weight = fragmentDeload.getWeight()
                        getDefaultSharedPreferences(this).edit()
                            .putInt(KEY_DELOAD_CYCLES, cycles)
                            .putInt(KEY_DELOAD_VOLUME, volume)
                            .putInt(KEY_DELOAD_WEIGHT, weight)
                            .putBoolean(KEY_DELOAD_SET, true)
                            .putInt(KEY_CURRENT_TP_STATE, CURRENT_TP_FINISHED)
                            .apply()
                        finish()
                        return true
                    }
                    fragmentChooseStart.isAdded -> {
                        val startDay = fragmentChooseStart.getStartDay()
                        if(startDay == -1) {
                            Toast.makeText(this, getString(R.string.you_have_to_choose_start_day), Toast.LENGTH_SHORT).show()
                        } else {
                            val startMonth = fragmentChooseStart.getStartMonth()
                            val startYear = fragmentChooseStart.getStartYear()
                            getDefaultSharedPreferences(this).edit()
                                .putInt(KEY_START_DAY, startDay)
                                .putInt(KEY_START_MONTH, startMonth)
                                .putInt(KEY_START_YEAR, startYear)
                                .putInt(KEY_CURRENT_TP_STATE, START_DAY_CHOSEN)
                                .apply()
                            addDeloadFragment()
                        }
                        return true
                    }
                    fragmentChooseInterval.isAdded -> {
                        if (!fragmentChooseInterval.isInCustomInterval()) {
                            val selectedInterval = fragmentChooseInterval.getSelectedInterval()
                            if (selectedInterval != null){
                                getDefaultSharedPreferences(this).edit()
                                    .putString(KEY_INTERVAL, getJsonStringFromArrayList(selectedInterval))
                                    .putInt(KEY_CURRENT_TP_STATE, INTERVAL_CHOSEN)
                                    .apply()
                                addChooseStartFragment(false)
                            } else {
                                Toast.makeText(
                                    this,
                                    getString(R.string.you_have_to_choose_interval),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (!fragmentChooseInterval.isCustomIntervalFinished()) {
                            Toast.makeText(
                                this,
                                getString(R.string.you_have_to_finish_interval),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            getDefaultSharedPreferences(this).edit()
                                .putString(
                                    KEY_INTERVAL,
                                    getJsonStringFromArrayList(fragmentChooseInterval.getInterval())
                                )
                                .putInt(KEY_CURRENT_TP_STATE, INTERVAL_CHOSEN)
                                .apply()
                            addChooseStartFragment(false)
                        }
                        return true
                    }
                    fragmentSelectTp.isAdded -> {
                        val selectedItem = fragmentSelectTp.getSelectedItem()
                        if(selectedItem != null) {
                            getDefaultSharedPreferences(this).edit()
                                .putInt(KEY_TP_ID, selectedItem.tpId)
                                .apply()
                            addChooseIntervalFragment(false)
                        } else {
                            Toast.makeText(this, getString(R.string.you_have_to_select_tp), Toast.LENGTH_SHORT).show()
                        }
                        return true
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        val dialog = LeaveDialogFragment.newInstance(getString(R.string.current_trainingplan), getString(R.string.you_are_about_to_leave))
        dialog.setListener(object: LeaveDialogFragment.ILeave {
            override fun leave() {
                finish()
            }
        })
        dialog.show(supportFragmentManager, "leave")
    }

    private fun addSelectTpFragment(remove: Boolean) {
        if(!remove) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content_frame_create_current_trainingplan_activity, fragmentSelectTp)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentSelectTp)
                .commit()
        }
        supportActionBar!!.title = getString(R.string.select_a_trainingplan)
        pageIdenticator.selectPage(0)
    }
    private fun addChooseIntervalFragment(remove: Boolean) {
        val tpId = getDefaultSharedPreferences(this).getInt(KEY_TP_ID, 0)
        if(tpId == 0) {
            //???
        }
        val args = Bundle()
        args.putInt(KEY_TP_ID, tpId)
        fragmentChooseInterval.arguments = args
        if(!remove) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentChooseInterval)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentChooseInterval)
                .commit()
        }
        supportActionBar!!.title = getString(R.string.choose_an_interval)
        pageIdenticator.selectPage(1)
    }
    private fun addChooseStartFragment(remove: Boolean) {
        val sInterval = getDefaultSharedPreferences(this).getString(KEY_INTERVAL, "")
        val interval =
            (if(sInterval == "") {
                null
            } else {
                getArrayListFromJsonString(sInterval!!)
            }) ?: return

        val routines = (dbInitializer.getRoutinesByTpId(database.routineDao(),
            getDefaultSharedPreferences(this).getInt(KEY_TP_ID, 0))) ?: return
        val args = Bundle()
        args.putIntegerArrayList("interval", interval)
        args.putParcelableArrayList("routines", routines as ArrayList<out Parcelable>)
        fragmentChooseStart.arguments = args
        if(!remove) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentChooseStart)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentChooseStart)
                .commit()
        }
        supportActionBar!!.title = getString(R.string.choose_a_day_to_start)
        pageIdenticator.selectPage(2)
    }
    private fun addDeloadFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.content_frame_create_current_trainingplan_activity, fragmentDeload)
            .commit()
        supportActionBar!!.title = getString(R.string.deload)
        pageIdenticator.selectPage(3)
    }

    private fun getJsonStringFromArrayList(list: ArrayList<Int>): String {
        var string = "{"
        for(i in 0 until list.size) {
            string += "\"$i\": ${list[i]}"
            if(i < list.size - 1) {
                string += ","
            }
        }
        string += "}"
        return string
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

    private fun getPrevFragment() {
        when {
            fragmentDeload.isAdded ->
                addChooseStartFragment(true)
            fragmentChooseStart.isAdded ->
                addChooseIntervalFragment(true)
            fragmentChooseInterval.isAdded ->
                addSelectTpFragment(true)
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
