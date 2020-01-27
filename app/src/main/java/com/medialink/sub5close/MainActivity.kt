package com.medialink.sub5close

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.medialink.sub5close.alarm.AlarmReceiver
import com.medialink.sub5close.preference.PreferenceHelper
import com.medialink.sub5close.ui.favorite.movie.MovieFavoriteViewModel
import com.medialink.sub5close.widget.Sub5Widget


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var movieFavoriteViewModel: MovieFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        movieFavoriteViewModel = ViewModelProvider(this).get(MovieFavoriteViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_popular, R.id.navigation_favorite, R.id.navigation_profile
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        supportActionBar?.hide()
        // setupViewModel
        movieFavoriteViewModel.favMovie.observe(this, Observer {
            Log.d(TAG, "fav movie berubah")

            val appWidgetManager =
                AppWidgetManager.getInstance(applicationContext)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(applicationContext, Sub5Widget::class.java)
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
        })

        initAlarm()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_main_change_language -> {
                val settingIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivityForResult(settingIntent, 901)
            }
            R.id.option_setting -> {
                navController.navigate(R.id.action_global_settingFragment)
                /*val data = arrayListOf<NotificationItem>().apply {
                    add(NotificationItem(1, "judul", "filem baru 1"))

                }
                val alarmReceiver = AlarmReceiver()
                alarmReceiver.sendNotification(this, data)*/
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAlarm() {
        val alarmReceiver = AlarmReceiver()
        if (PreferenceHelper.getInstance(this)?.isDailyReminder() == true) {
            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.ID_DAILY)
            Log.d(TAG, "onCreate: set daily reminder")
        } else {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_DAILY)
        }
        if (PreferenceHelper.getInstance(this)?.isRelesedReminder() == true) {
            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.ID_RELEASE)
            Log.d(TAG, "onCreate: set relese reminder")
        } else {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_RELEASE)
        }
    }
}
