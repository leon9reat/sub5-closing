package com.medialink.sub5close.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    /** coroutine **/
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /* room */
    private val favoriteRepo: FavoriteRepository

    init {
        val favoriteDao = FavoriteDatabase.getDatabase(application.applicationContext)
            .favoriteDao()
        favoriteRepo = FavoriteRepository(favoriteDao)
    }

    val favMovies: LiveData<List<Favorite>>
        get() = favoriteRepo.favMovie

    val favTvShow: LiveData<List<Favorite>>
        get() = favoriteRepo.favTvShow

    fun insert(aFav: Favorite) {
        launch { favoriteRepo.insert(aFav) }
    }

    fun delete(aFav: Favorite) {
        launch { favoriteRepo.delete(aFav) }
    }
}