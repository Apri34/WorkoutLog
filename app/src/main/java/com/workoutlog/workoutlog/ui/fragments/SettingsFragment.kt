package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.workoutlog.workoutlog.R

class SettingsFragment: PreferenceFragmentCompat() {

    private var onThemeChangedListener: OnThemeChangedListener? = null
    fun setOnThemeChangedListener(listener: OnThemeChangedListener) {
        onThemeChangedListener = listener
    }

    companion object {
        private const val IS_LIGHT_THEME = "isLightTheme"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
        val switchTheme: SwitchPreferenceCompat = findPreference("theme")!!
        switchTheme.setOnPreferenceClickListener {
            if((it as SwitchPreferenceCompat).isChecked) {
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(IS_LIGHT_THEME, true)
                    .apply()
            } else {
                PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(IS_LIGHT_THEME, false)
                    .apply()
            }
            if(onThemeChangedListener != null) onThemeChangedListener!!.onThemeChanged()
            true
        }
    }

    interface OnThemeChangedListener {
        fun onThemeChanged()
    }
}