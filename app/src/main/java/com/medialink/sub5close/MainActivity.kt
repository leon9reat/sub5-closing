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
import com.medialink.sub5close.ui.favorite.movie.MovieFavoriteViewModel
import com.medialink.sub5close.ui.favorite.tvShow.TvShowFavoriteViewModel
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

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_main_change_language) {
            val settingIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivityForResult(settingIntent, 901)
        }
        return super.onOptionsItemSelected(item)
    }
}
