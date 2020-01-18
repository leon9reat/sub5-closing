package com.medialink.sub5close.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
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
}