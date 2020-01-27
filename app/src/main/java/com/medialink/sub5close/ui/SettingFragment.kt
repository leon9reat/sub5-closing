package com.medialink.sub5close.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.medialink.sub5close.R
import com.medialink.sub5close.alarm.AlarmReceiver

class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var key_release: String
    private lateinit var key_daily: String

    private lateinit var releaseSwitch: SwitchPreference
    private lateinit var dailySwitch: SwitchPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
        init()
        setupUi()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {
        key_release = getString(R.string.key_release_reminder)
        key_daily = getString(R.string.key_daily_reminder)
        releaseSwitch = findPreference<SwitchPreference>(key_release) as SwitchPreference
        dailySwitch = findPreference<SwitchPreference>(key_daily) as SwitchPreference
    }

    private fun setupUi() {
        val pref = preferenceManager.sharedPreferences
        releaseSwitch.isChecked = pref.getBoolean(key_release, true)
        dailySwitch.isChecked = pref.getBoolean(key_daily, true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        var alarmReceiver = AlarmReceiver()
        when (key) {
            key_release -> {
                val isRelease= sharedPreferences.getBoolean(key_release, true)
                releaseSwitch.isChecked = isRelease
                if (isRelease) {
                    alarmReceiver.setRepeatingAlarm(context!!, AlarmReceiver.ID_RELEASE)
                } else {
                    alarmReceiver.cancelAlarm(context!!, AlarmReceiver.ID_RELEASE)
                }

            }
            key_daily -> {
                val isDaily = sharedPreferences.getBoolean(key_daily, true)
                dailySwitch.isChecked = isDaily
                if (isDaily) {
                    alarmReceiver.setRepeatingAlarm(context!!, AlarmReceiver.ID_DAILY)
                } else {
                    alarmReceiver.cancelAlarm(context!!, AlarmReceiver.ID_DAILY)
                }
            }
        }
    }
}