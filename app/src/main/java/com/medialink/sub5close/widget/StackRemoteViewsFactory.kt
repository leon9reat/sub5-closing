package com.medialink.sub5close.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext


internal class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope {

    val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val widgetItem = arrayListOf<Favorite>()

    override fun onCreate() {
        Log.d("debug", "onCreate :")
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        runBlocking {
            val favDao = FavoriteDatabase.getDatabase(context).favoriteDao()
            val favMovie = favDao.getAllFavorite()

            widgetItem.clear()
            widgetItem.addAll(favMovie)

            Log.d("debug", " onDataSetChanged fav size: ${favMovie.size}")
        }
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {

        Log.d("debug", "getViewAt fav size: ${widgetItem.size}")

        // terpaksa menggunakan Glide lagi, karena menggunakan coil tidak tampil gambarnya
        val remoteView = RemoteViews(context.packageName, R.layout.widget_item)
        try {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(Consts.TMDB_PHOTO_URL + widgetItem[position].poster)
                .submit()
                .get()
            remoteView.setImageViewBitmap(R.id.img_widget, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val extras = bundleOf(Sub5Widget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        fillInIntent.putExtra("PARCEL", widgetItem[position].title)

        remoteView.setOnClickFillInIntent(R.id.img_widget, fillInIntent)
        return remoteView
    }

    override fun getCount(): Int = widgetItem.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        job.cancel()
    }

}