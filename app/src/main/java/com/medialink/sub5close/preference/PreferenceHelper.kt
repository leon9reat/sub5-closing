package com.medialink.sub5close.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.medialink.sub5close.R

class PreferenceHelper {

    companion object {
        private var mPrefHelper: PreferenceHelper? = null
        private var mPref: SharedPreferences? = null
        private var mContext: Context? = null

        fun getInstance(context: Context?): PreferenceHelper? {
            if (mPrefHelper == null) {
                mPrefHelper = PreferenceHelper()
                mContext = context
                mPref = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return mPrefHelper
        }
    }

    private var isRelesedReminder = false
    private var isDailyReminder = false


    fun isRelesedReminder(): Boolean {
        val key = mContext!!.getString(R.string.key_release_reminder)
        isRelesedReminder = mPref!!.getBoolean(key, true)
        return isRelesedReminder
    }

    fun setRelesedReminder(relesedReminder: Boolean) {
        isRelesedReminder = relesedReminder
    }

    fun isDailyReminder(): Boolean {
        val key = mContext!!.getString(R.string.key_daily_reminder)
        isDailyReminder = mPref!!.getBoolean(key, true)
        return isDailyReminder
    }

    fun setDailyReminder(dailyReminder: Boolean) {
        isDailyReminder = dailyReminder
    }

    fun clear(): Boolean {
        return mPref!!.edit()
            .clear()
            .commit()
    }
}