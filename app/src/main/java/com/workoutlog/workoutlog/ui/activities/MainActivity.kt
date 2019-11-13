package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer
import com.workoutlog.workoutlog.database.entities.Trainingplan
import com.workoutlog.workoutlog.ui.fragments.LoginFragment
import com.workoutlog.workoutlog.ui.fragments.MessageDialogFragment
import com.workoutlog.workoutlog.ui.fragments.RegisterFragment
import com.workoutlog.workoutlog.views.CustomEditText

class MainActivity : FragmentActivity(), LoginFragment.ILogin, RegisterFragment.IRegister {

    override fun returnToLogin() {
        supportFragmentManager.popBackStack()
    }

    override fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) {

                getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener {e ->
                if(e !is FirebaseAuthException) {
                    MessageDialogFragment.newInstance(getString(R.string.something_went_wrong_check_internet)).show(supportFragmentManager, "noConnection")
                } else {
                    when (e.errorCode) {
                        "ERROR_EMAIL_ALREADY_IN_USE" -> {
                            fragmentRegister.showErrorMessageOnEmail(CustomEditText.Error.ERROR_EMAIL_ALREADY_USED)
                        }
                        "ERROR_INVALID_EMAIL" -> {
                            fragmentRegister.showErrorMessageOnEmail(CustomEditText.Error.ERROR_EMAIL_NOT_EXISTS)
                        }
                        "ERROR_WEAK_PASSWORD" -> {
                            fragmentRegister.showErrorMessageOnPassword(CustomEditText.Error.ERROR_PASSWORD_REQUIREMENTS_NOT_MET)
                        }
                    }
                }
            }
    }

    override fun login(email: String, password: String, stayLoggedIn: Boolean) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) {
                getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), stayLoggedIn).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                if(e !is FirebaseAuthException) {
                    MessageDialogFragment.newInstance(getString(R.string.something_went_wrong_check_internet)).show(supportFragmentManager, "noConnection")
                } else {
                    when (e.errorCode) {
                        "ERROR_INVALID_EMAIL" -> {
                            fragmentLogin.showErrorMessageOnEmail(CustomEditText.Error.ERROR_WRONG_EMAIL)
                        }
                        "ERROR_USER_NOT_FOUND" -> {
                            fragmentLogin.showErrorMessageOnEmail(CustomEditText.Error.ERROR_WRONG_EMAIL)
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            fragmentLogin.showErrorMessageOnPassword(CustomEditText.Error.ERROR_WRONG_PASSWORD)
                        }
                    }
                }
            }
    }

    override fun goToRegisterScreen() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.frame_layout_main_activity, fragmentRegister)
            .addToBackStack(null)
            .commit()
    }

    override fun continueAsGuest() {
        startActivity(Intent(this, NavigationActivity::class.java))
        getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), true).apply()
        getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), false).apply()
        finish()
    }

    private lateinit var mAuth: FirebaseAuth

    private lateinit var fragmentLogin: LoginFragment
    private lateinit var fragmentRegister: RegisterFragment
    private lateinit var mWorkoutLog: WorkoutLog

    companion object {
        private const val KEY_IS_DB_CREATED = "isDbCreated"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_main)
        mWorkoutLog = this.applicationContext as WorkoutLog

        if(!getDefaultSharedPreferences(this).getBoolean(KEY_IS_DB_CREATED, false)) {
            val tp = Trainingplan(-1, "tpForSingleRoutines")
            val dbInitializer = DatabaseInitializer.getInstance(this)
            val database = AppDatabase.getInstance(this)
            dbInitializer.insertTrainingplan(database.trainingplanDao(), tp)
            getDefaultSharedPreferences(this).edit().putBoolean(KEY_IS_DB_CREATED, true).apply()
        }

        fragmentLogin = LoginFragment()
        fragmentRegister = RegisterFragment()

        mAuth = FirebaseAuth.getInstance()

        val stayLoggedIn = getDefaultSharedPreferences(this).getBoolean(getString(R.string.stay_logged_in), false)
        if(!stayLoggedIn && mAuth.currentUser != null) {
            mAuth.signOut()
        }

        val currentUser = mAuth.currentUser
        if(currentUser != null || getDefaultSharedPreferences(this).getBoolean(getString(R.string.continue_guest), false)) {
            startActivity(Intent(this, NavigationActivity::class.java))
            finish()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_main_activity, fragmentLogin)
            .commit()
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
