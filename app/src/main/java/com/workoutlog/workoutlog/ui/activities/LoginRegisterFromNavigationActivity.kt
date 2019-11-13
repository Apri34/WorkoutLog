package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.application.WorkoutLog
import com.workoutlog.workoutlog.database.DatabaseSynchronizer
import com.workoutlog.workoutlog.ui.fragments.LoginFragment
import com.workoutlog.workoutlog.ui.fragments.MessageDialogFragment
import com.workoutlog.workoutlog.ui.fragments.RegisterFragment
import com.workoutlog.workoutlog.views.CustomEditText

class LoginRegisterFromNavigationActivity : AppCompatActivity(), LoginFragment.ILogin, RegisterFragment.IRegister {

    override fun returnToLogin() {
        supportFragmentManager.popBackStack()
    }

    override fun login(email: String, password: String, stayLoggedIn: Boolean) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                databaseSynchronizer.loginUser(mAuth.currentUser!!, this)
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), stayLoggedIn).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
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
            .addToBackStack(null)
            .replace(R.id.frame_layout_login_register_from_navigation_activity, fragmentRegister)
            .commit()
    }

    override fun continueAsGuest() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), true).apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.stay_logged_in), false).apply()
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    override fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                databaseSynchronizer.loginUser(mAuth.currentUser!!, this)
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), false).apply()
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
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

    private lateinit var fragmentLogin: LoginFragment
    private lateinit var fragmentRegister: RegisterFragment
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseSynchronizer: DatabaseSynchronizer
    private lateinit var mWorkoutLog: WorkoutLog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondaryDark)
        }
        setContentView(R.layout.activity_login_register_from_navigation)
        mWorkoutLog = this.applicationContext as WorkoutLog
        fragmentLogin = LoginFragment()
        fragmentRegister = RegisterFragment()

        mAuth = FirebaseAuth.getInstance()
        databaseSynchronizer = DatabaseSynchronizer.getInstance(this)
        supportFragmentManager.beginTransaction().add(R.id.frame_layout_login_register_from_navigation_activity, fragmentLogin).commit()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
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
