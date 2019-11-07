package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Routine
import com.workoutlog.workoutlog.ui.fragments.*
import org.json.JSONObject
import java.sql.Date
import java.util.*

class NavigationActivity : AppCompatActivity(),
    CurrentTrainingplanFragment.ICurrentTrainingplanFragment,
    StartWorkoutDialogFragment.IStartWorkout,
    StartWorkoutNoSpecRoutineDialogFragment.IStartWorkoutNoSpecRoutine,
    HomeFragment.IHomeFragment
{

    override fun refreshHomeFragment(fragment: Fragment): Boolean {
        return if(navView.checkedItem?.itemId == R.id.nav_home && fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit()
            true
        } else false
    }

    override fun startWorkout(routine: Routine) {
        val intent = Intent(this, WorkoutActivity::class.java)
        intent.putExtra(KEY_ROUTINE_WORKOUT, routine)
        intent.putExtra(KEY_DELETABLE, false)
        startActivity(intent)
    }

    override fun editCurrentTpDayStart() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_START)
        startActivity(intent)
    }

    override fun editCurrentTp() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_SELECT_TP)
        startActivity(intent)
    }

    override fun editCurrentTpInterval() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_INTERVAL)
        startActivity(intent)
    }

    override fun goToHistory() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame_navigation_activity, historyFragment)
            .addToBackStack(null)
            .commit()
        navView.setCheckedItem(R.id.nav_history)
        supportActionBar!!.title = getString(R.string.history)
    }

    override fun chooseTp() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame_navigation_activity, currentTrainingplanFragment)
            .addToBackStack(null)
            .commit()
        navView.setCheckedItem(R.id.nav_current_trainingplan)
        supportActionBar!!.title = getString(R.string.current_trainingplan)
    }

    override fun startCurrentWorkout(rId: Int) {
        val routine = dbInitializer.getRoutineById(database.routineDao(), rId)
        val intent = Intent(this, WorkoutActivity::class.java)
        intent.putExtra(KEY_ROUTINE_WORKOUT, routine)
        intent.putExtra(KEY_DELETABLE, false)
        startActivity(intent)
    }

    override fun chooseOtherWorkout() {
        val dialog = StartWorkoutNoSpecRoutineDialogFragment()
        dialog.show(supportFragmentManager, "StartOtherWorkout")
    }

    override fun createRoutine() {
        val intent = Intent(this, CreateRoutineActivity::class.java)
        startActivity(intent)
    }

    override fun chooseRoutine() {
        val intent = Intent(this, ChooseRoutineActivity::class.java)
        startActivity(intent)
    }

    override fun refreshCurrentTrainingplanFragment(fragment: Fragment): Boolean {
        return if(navView.checkedItem?.itemId == R.id.nav_current_trainingplan && fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit()
            true
        } else false
    }

    override fun createNewCurrentTrainingplan() {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP)
            .remove(KEY_TP_ID)
            .remove(KEY_INTERVAL)
            .remove(KEY_START_DAY)
            .remove(KEY_START_MONTH)
            .remove(KEY_START_YEAR)
            .remove(KEY_DELOAD_CYCLES)
            .remove(KEY_DELOAD_VOLUME)
            .remove(KEY_DELOAD_WEIGHT)
            .remove(KEY_DELOAD_SET)
            .apply()
        startActivity(Intent(this, CreateCurrentTrainingplanActivity::class.java))
    }

    override fun editTrainingplan() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_SELECT_TP)
        startActivity(intent)
    }

    override fun editInterval() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_INTERVAL)
        startActivity(intent)
    }

    override fun editStartDay() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_START)
        startActivity(intent)
    }

    override fun editDeload() {
        val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_DELOAD)
        startActivity(intent)
    }

    override fun createCurrentTrainingplan() {
        startActivity(Intent(this, CreateCurrentTrainingplanActivity::class.java))
    }

    companion object {
        private const val HOME_FRAGMENT_KEY = "homeFragment"
        private const val EXERCISES_FRAGMENT_KEY = "exercisesFragment"
        private const val TRAININGPLANS_FRAGMENT_KEY = "trainingplansFragment"
        private const val CURRENT_TRAININGPLAN_FRAGMENT_KEY = "currentTrainingplanFragment"
        private const val HISTORY_FRAGMENT_KEY = "historyFragment"

        private const val KEY_FRAGMENT = "fragment"
        private const val KEY_FRAG_CHOOSE_INTERVAL = "chooseInterval"
        private const val KEY_FRAG_CHOOSE_START = "chooseStart"
        private const val KEY_FRAG_DELOAD = "deload"
        private const val KEY_FRAG_SELECT_TP = "selectTp"

        private const val KEY_CURRENT_TP_STATE = "currentTpState"
        private const val NO_CURRENT_TP = 0
        private const val CURRENT_TP_FINISHED = 4
        private const val KEY_TP_ID = "tpId"
        private const val KEY_INTERVAL = "interval"
        private const val KEY_START_DAY = "startDay"
        private const val KEY_START_MONTH = "startMonth"
        private const val KEY_START_YEAR = "startYear"
        private const val KEY_DELOAD_CYCLES = "deloadCycles"
        private const val KEY_DELOAD_VOLUME = "deloadVolume"
        private const val KEY_DELOAD_WEIGHT = "deloadWeight"
        private const val KEY_DELOAD_SET = "deloadSet"

        private const val KEY_ROUTINE_WORKOUT = "routineWorkout"
        private const val KEY_DELETABLE = "deletable"
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeFragment: HomeFragment
    private lateinit var exercisesFragment: ExercisesFragment
    private lateinit var trainingplansFragment: TrainingplansFragment
    private lateinit var currentTrainingplanFragment: CurrentTrainingplanFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var textViewNavHeaderUser: TextView
    private lateinit var toolbar: Toolbar

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_navigation)

        dbInitializer = DatabaseInitializer.getInstance()
        database = AppDatabase.getInstance(this)

        mAuth = FirebaseAuth.getInstance()
        navView = findViewById(R.id.nav_view_navigation_activity)
        drawerLayout = findViewById(R.id.drawer_layout_navigation_activity)
        textViewNavHeaderUser = navView.getHeaderView(0).findViewById(R.id.text_view_nav_header_user)
        toolbar = findViewById(R.id.toolbar_navigation_activity)

        if(mAuth.currentUser != null) {
            navView.inflateMenu(R.menu.drawer_view_logged_in)
            textViewNavHeaderUser.text = mAuth.currentUser!!.email!!
        } else {
            navView.inflateMenu(R.menu.drawer_view_logged_out)
            textViewNavHeaderUser.visibility = View.GONE
        }

        if(savedInstanceState == null) {
            homeFragment = HomeFragment()
            exercisesFragment = ExercisesFragment()
            trainingplansFragment = TrainingplansFragment()
            currentTrainingplanFragment = CurrentTrainingplanFragment()
            historyFragment = HistoryFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.content_frame_navigation_activity, homeFragment)
                .commit()
            navView.setCheckedItem(R.id.nav_home)
        } else {
            homeFragment =
                if(supportFragmentManager.getFragment(savedInstanceState, HOME_FRAGMENT_KEY) != null)
                    supportFragmentManager.getFragment(savedInstanceState, HOME_FRAGMENT_KEY) as HomeFragment
                else
                    HomeFragment()
            exercisesFragment =
                if(supportFragmentManager.getFragment(savedInstanceState, EXERCISES_FRAGMENT_KEY) != null)
                    supportFragmentManager.getFragment(savedInstanceState, EXERCISES_FRAGMENT_KEY) as ExercisesFragment
                else
                    ExercisesFragment()
            trainingplansFragment =
                if(supportFragmentManager.getFragment(savedInstanceState, TRAININGPLANS_FRAGMENT_KEY) != null)
                    supportFragmentManager.getFragment(savedInstanceState, TRAININGPLANS_FRAGMENT_KEY) as TrainingplansFragment
                else
                    TrainingplansFragment()
            currentTrainingplanFragment =
                if(supportFragmentManager.getFragment(savedInstanceState, CURRENT_TRAININGPLAN_FRAGMENT_KEY) != null)
                    supportFragmentManager.getFragment(savedInstanceState, CURRENT_TRAININGPLAN_FRAGMENT_KEY) as CurrentTrainingplanFragment
                else
                    CurrentTrainingplanFragment()
            historyFragment =
                if(supportFragmentManager.getFragment(savedInstanceState, HISTORY_FRAGMENT_KEY) != null)
                    supportFragmentManager.getFragment(savedInstanceState, HISTORY_FRAGMENT_KEY) as HistoryFragment
                else
                    HistoryFragment()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if(supportFragmentManager.backStackEntryCount == 0) {
                navView.setCheckedItem(R.id.nav_home)
                supportActionBar!!.title = getString(R.string.home)
            }
        }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            title = getString(R.string.home)
        }

        navView.setNavigationItemSelectedListener {menuItem ->

            when(menuItem.itemId) {
                R.id.nav_home -> {
                    if(!menuItem.isChecked) {
                        supportFragmentManager.popBackStack()
                        menuItem.isChecked = true
                    }
                    supportActionBar!!.title = getString(R.string.home)
                }
                R.id.nav_exercises -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, exercisesFragment)
                            .addToBackStack(null)
                            .commit()
                    } else if(navView.checkedItem?.itemId != R.id.nav_exercises) {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, exercisesFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                    supportActionBar!!.title = getString(R.string.exercises)
                }
                R.id.nav_trainingplans -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, trainingplansFragment)
                            .addToBackStack(null)
                            .commit()
                    } else  if(navView.checkedItem?.itemId != R.id.nav_trainingplans){
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, trainingplansFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                    supportActionBar!!.title = getString(R.string.trainingplans)
                }
                R.id.nav_current_trainingplan -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, currentTrainingplanFragment)
                            .addToBackStack(null)
                            .commit()
                    } else  if(navView.checkedItem?.itemId != R.id.nav_current_trainingplan){
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, currentTrainingplanFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                    supportActionBar!!.title = getString(R.string.current_trainingplan)
                }
                R.id.nav_history -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, historyFragment)
                            .addToBackStack(null)
                            .commit()
                    } else  if(navView.checkedItem?.itemId != R.id.nav_history){
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, historyFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                    supportActionBar!!.title = getString(R.string.history)
                }
                R.id.nav_settings -> {

                }
                R.id.nav_login -> {
                    login()
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_start_workout -> {
                    val c = Calendar.getInstance()
                    val date = Date(c.timeInMillis)
                    val workoutDone = dbInitializer.existsWorkoutOnDate(database.exerciseDoneDao(), date)
                    if(workoutDone) {
                        val dialog = WorkoutDoneDialogFragment()
                        dialog.setListener(object : WorkoutDoneDialogFragment.IWorkoutAgain {
                            override fun workoutAgain() {
                                openChooseWorkoutDialog()
                            }
                        })
                        dialog.show(supportFragmentManager, "workoutAgain")
                    } else {
                        openChooseWorkoutDialog()
                    }
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        if(intent.hasExtra("openTrainingplans")) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_navigation_activity, trainingplansFragment)
                .addToBackStack(null)
                .commit()
            navView.setCheckedItem(R.id.nav_trainingplans)
            supportActionBar!!.title = getString(R.string.trainingplans)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(homeFragment.isAdded)
            supportFragmentManager.putFragment(outState, HOME_FRAGMENT_KEY, homeFragment)
        if(exercisesFragment.isAdded)
            supportFragmentManager.putFragment(outState, EXERCISES_FRAGMENT_KEY, exercisesFragment)
        if(trainingplansFragment.isAdded)
            supportFragmentManager.putFragment(outState, TRAININGPLANS_FRAGMENT_KEY, trainingplansFragment)
        if(currentTrainingplanFragment.isAdded)
            supportFragmentManager.putFragment(outState, CURRENT_TRAININGPLAN_FRAGMENT_KEY, currentTrainingplanFragment)
        if(historyFragment.isAdded)
            supportFragmentManager.putFragment(outState, HISTORY_FRAGMENT_KEY, historyFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
             drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    private fun openChooseWorkoutDialog() {
        if(PreferenceManager.getDefaultSharedPreferences(this).contains(KEY_CURRENT_TP_STATE) &&
            (PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_CURRENT_TP_STATE, NO_CURRENT_TP) == CURRENT_TP_FINISHED)) {
            val tp = dbInitializer.getTrainingplanById(
                database.trainingplanDao(),
                PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_TP_ID, -1))
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            val rId = getRoutine(day, month, year, tp.tpId)
            if(rId != null) {
                if(rId != 0) {
                    val routine = dbInitializer.getRoutineById(database.routineDao(), rId)
                    val dialog = StartWorkoutDialogFragment.newInstance(routine.rName, tp.tpName, rId)
                    dialog.show(supportFragmentManager, "startWorkout")
                } else {
                    val dialog = StartWorkoutNoSpecRoutineDialogFragment.newInstance(getString(R.string.restday_workout_though))
                    dialog.show(supportFragmentManager, "startWorkoutRestday")
                }
            }
        } else {
            val dialog = StartWorkoutNoSpecRoutineDialogFragment()
            dialog.show(supportFragmentManager, "startWorkoutNoCurrentTp")
        }
    }

    private fun login() {
        startActivity(Intent(this, LoginRegisterFromNavigationActivity::class.java))
        finish()
    }

    private fun logout() {
        if(mAuth.currentUser != null) {
            mAuth.signOut()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getRoutine(day: Int, month: Int, year: Int, tpId: Int): Int? {
        val routines = dbInitializer.getRoutinesByTpId(database.routineDao(), tpId)
        val interval = getArrayListFromJsonString(PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_INTERVAL, "")!!)
        if (routines == null || interval.size == 0) return null

        val c = Calendar.getInstance()
        c.set(year, month, day, 0, 0, 0)
        val timeNow = c.timeInMillis

        val selectedYear = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_START_YEAR, -1)
        val selectedMonth = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_START_MONTH, -1)
        val selectedDay = PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_START_DAY, -1)
        if(selectedDay == -1 || selectedMonth == -1 || selectedYear == -1) return null
        c.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
        val timeStart = c.timeInMillis
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
}
