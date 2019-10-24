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
import com.workoutlog.workoutlog.ui.fragments.*

class NavigationActivity : AppCompatActivity(), CurrentTrainingplanFragment.ICurrentTrainingplanFragment {

    override fun refreshFragment(fragment: Fragment): Boolean {
        if(fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit()
        }
        return true
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

    override fun editInterval() {val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_INTERVAL)
        startActivity(intent)
    }

    override fun editStartDay() {val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, KEY_FRAG_CHOOSE_START)
        startActivity(intent)
    }

    override fun editDeload() {val intent = Intent(this, CreateCurrentTrainingplanActivity::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_navigation)

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
}
