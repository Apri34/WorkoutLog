package com.workoutlog.workoutlog.ui.activities

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.ui.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val IS_LIGHT_THEME = "isLightTheme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(PreferenceManager.getDefaultSharedPreferences(this).contains(IS_LIGHT_THEME)
            && PreferenceManager.getDefaultSharedPreferences(this).getBoolean(IS_LIGHT_THEME, false)) {
            setTheme(R.style.AppTheme_LIGHT)
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.topbarColor, typedValue, true)
            window.statusBarColor = typedValue.data
        }
        setContentView(R.layout.activity_settings)
        val settingsFragment = SettingsFragment()
        settingsFragment.setOnThemeChangedListener(object: SettingsFragment.OnThemeChangedListener {
            override fun onThemeChanged() {
                recreate()
            }
        })
        supportFragmentManager
            .beginTransaction()
            .add(R.id.settings_container, settingsFragment)
            .commit()
    }
}
