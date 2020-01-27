package com.medialink.sub5close.database

import android.content.ContentValues
import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.medialink.sub5close.Consts

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    val favMovie: LiveData<List<Favorite>> = favoriteDao.getFavorite(Consts.MOVIE_TYPE)
    val favTvShow: LiveData<List<Favorite>> = favoriteDao.getFavorite(Consts.TV_SHOW_TYPE)

    @WorkerThread
    suspend fun isFavorite(pid: Int): Boolean = favoriteDao.getCountFavorite(pid) > 0

    @WorkerThread
    suspend fun insert(aFavorite: Favorite) {
        favoriteDao.insert(aFavorite)
    }

    @WorkerThread
    suspend fun delete(aFavorite: Favorite) {
        favoriteDao.delete(aFavorite)
    }

    @WorkerThread
    suspend fun deleteOne(aTmdbId: Int) {
        favoriteDao.deleteOneFavorite(aTmdbId)
    }

    /* untuk content provider */

    fun count(): Int = favoriteDao.count()

    fun insertProvider(favorite: Favorite): Long = favoriteDao.insertProvider(favorite)

    fun selectAll(): Cursor = favoriteDao.selectAll()

    fun selectMovie(): Cursor = favoriteDao.selectByType(Consts.MOVIE_TYPE)

    fun selectTvShow(): Cursor = favoriteDao.selectByType(Consts.TV_SHOW_TYPE)

    fun selectById(id: Long): Cursor = favoriteDao.selectById(id)

    fun deleteById(id: Long): Int = favoriteDao.deleteById(id)

    fun updateProvider(values: ContentValues?): Int {
        values?.let {
            return favoriteDao.updateProvider(Favorite.fromContentValues(it))
        }
        return 0
    }
}