package com.medialink.sub5close.ui.favorite.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieFavoriteViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private val repository: FavoriteRepository

    init {
        val favDao = FavoriteDatabase.getDatabase(application.applicationContext).favoriteDao()
        repository = FavoriteRepository(favDao)
    }

    val favMovie: LiveData<List<Favorite>>
        get() = repository.favMovie

    fun deleteFavorite(aFavorite: Favorite) {
        launch { repository.delete(aFavorite) }
    }
}