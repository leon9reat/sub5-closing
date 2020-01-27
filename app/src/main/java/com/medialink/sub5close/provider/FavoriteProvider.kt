package com.medialink.sub5close.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.medialink.sub5close.Consts
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class FavoriteProvider : ContentProvider(), CoroutineScope {


    companion object {
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private const val FAV_DIR = 1
        private const val FAV_ITEM = 2
        private const val FAV_MOVIE = 11
        private const val FAV_TV = 12
    }

    private lateinit var repository: FavoriteRepository
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    init {
        sUriMatcher.addURI(Consts.AUTHORITY, Consts.TABLE_NAME, FAV_DIR)
        sUriMatcher.addURI(Consts.AUTHORITY, "movie", FAV_MOVIE)
        sUriMatcher.addURI(Consts.AUTHORITY, "tvshow", FAV_TV)
        sUriMatcher.addURI(Consts.AUTHORITY, "${Consts.TABLE_NAME}/#", FAV_ITEM)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (sUriMatcher.match(uri)) {
            FAV_ITEM -> repository.deleteById(uri.lastPathSegment?.toLong() ?: 0)
            else -> 0
        }
        context?.contentResolver?.notifyChange(Consts.FavoriteColumn.FAVORITE_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (sUriMatcher.match(uri)) {
            FAV_DIR -> values?.let {
                repository.insertProvider(Favorite.fromContentValues(it))
            } ?: 0
            else -> 0
        }
        context?.contentResolver?.notifyChange(Consts.FavoriteColumn.FAVORITE_URI, null)
        return Uri.parse("${Consts.FavoriteColumn.FAVORITE_URI}/$added")
    }

    override fun onCreate(): Boolean {
        context?.applicationContext?.let {
            val favDao = FavoriteDatabase.getDatabase(it).favoriteDao()
            repository = FavoriteRepository(favDao)
            return true
        }
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = sUriMatcher.match(uri)
        val cursor: Cursor? =
            when (code) {
                FAV_DIR -> repository.selectAll()
                FAV_MOVIE -> repository.selectMovie()
                FAV_TV -> repository.selectTvShow()
                FAV_ITEM -> repository.selectById(uri.lastPathSegment?.toLong() ?: 0)
                else -> null
            }
        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val update: Int = when (sUriMatcher.match(uri)) {
            FAV_ITEM -> repository.updateProvider(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(Consts.FavoriteColumn.FAVORITE_URI, null)
        return update
    }

    override fun shutdown() {
        super.shutdown()
        job.cancel()
    }
}
