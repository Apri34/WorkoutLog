package com.workoutlog.workoutlog.ui.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.workoutlog.workoutlog.R

class NavigationActivity : AppCompatActivity() {

    private lateinit var tv: TextView
    private lateinit var buttonLogin: Button
    private lateinit var buttonLogout: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        mAuth = FirebaseAuth.getInstance()

        tv = findViewById(R.id.textview_activity_navigation_email)
        buttonLogin = findViewById(R.id.button_activity_navigation_login_register)
        buttonLogin.setOnClickListener {
            login()
        }
        buttonLogout = findViewById(R.id.button_activity_navigation_logout)
        buttonLogout.setOnClickListener {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()
        getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.continue_guest), mAuth.currentUser == null).apply()

        if(mAuth.currentUser != null) {
            buttonLogin.visibility = View.GONE
            buttonLogout.visibility = View.VISIBLE
        } else {
            buttonLogout.visibility = View.GONE
            buttonLogin.visibility = View.VISIBLE
        }

        val user = mAuth.currentUser
        if(user != null) {
            tv.text = user.email!!
        } else {
            tv.text = "no user"
        }
    }

    private fun login() {
        startActivity(Intent(this, LoginRegisterFromNavigationActivity::class.java))
    }

    private fun logout() {
        if(mAuth.currentUser != null) {
            mAuth.signOut()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
