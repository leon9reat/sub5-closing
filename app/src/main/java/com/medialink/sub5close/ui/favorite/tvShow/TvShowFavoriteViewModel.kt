package com.medialink.sub5close.ui.favorite.tvShow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TvShowFavoriteViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {

    val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    val repository: FavoriteRepository

    init {
        val favDao = FavoriteDatabase.getDatabase(application.applicationContext)
            .favoriteDao()
        repository = FavoriteRepository(favDao)
    }

    val favTvShow: LiveData<List<Favorite>>
        get() = repository.favTvShow

    fun deleteFavorite(aFavorite: Favorite) {
        launch { repository.delete(aFavorite) }
    }


}