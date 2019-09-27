package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.ui.fragments.*

class NavigationActivity : AppCompatActivity() {

    companion object {
        private const val HOME_FRAGMENT_KEY = "homeFragment"
        private const val EXERCISES_FRAGMENT_KEY = "exercisesFragment"
        private const val TRAININGPLANS_FRAGMENT_KEY = "trainingplansFragment"
        private const val CURRENT_TRAININGPLAN_FRAGMENT_KEY = "currentTrainingplanFragment"
        private const val HISTORY_FRAGMENT_KEY = "historyFragment"
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
            }
        }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        navView.setNavigationItemSelectedListener {menuItem ->

            when(menuItem.itemId) {
                R.id.nav_home -> {
                    if(!menuItem.isChecked) {
                        supportFragmentManager.popBackStack()
                        menuItem.isChecked = true
                    }
                }
                R.id.nav_exercises -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, exercisesFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, exercisesFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                }
                R.id.nav_trainingplans -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, trainingplansFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, trainingplansFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                }
                R.id.nav_current_trainingplan -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, currentTrainingplanFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, currentTrainingplanFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
                }
                R.id.nav_history -> {
                    if(navView.checkedItem?.itemId == R.id.nav_home) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, historyFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame_navigation_activity, historyFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    menuItem.isChecked = true
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
