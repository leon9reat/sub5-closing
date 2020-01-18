package com.medialink.sub5close.ui.popular.tvShow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medialink.sub4moviedb.model.tv_show.TvShow
import com.medialink.sub5close.Consts
import com.medialink.sub5close.data.OperationCallback
import com.medialink.sub5close.data.tvShow.TvShowRepository
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TvShowViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val tvShowRepo: TvShowRepository
    private val favoriteRepo: FavoriteRepository

    init {
        tvShowRepo = TvShowRepository()
        val favoriteDao = FavoriteDatabase.getDatabase(application.applicationContext)
            .favoriteDao()
        favoriteRepo = FavoriteRepository(favoriteDao)
    }

    private val _tvShows = MutableLiveData<List<TvShow>>().apply { value = emptyList() }
    val tvShows: LiveData<List<TvShow>> = _tvShows

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isDataChanged = MutableLiveData<Boolean>()
    val isDataChanged: LiveData<Boolean> = _isDataChanged

    var localLanguage: String = "en"

    fun loadTvShow(page: Int) {
        _isLoading.value = true

        tvShowRepo.page = page
        tvShowRepo.getTvShows(object : OperationCallback {
            override fun onSuccess(obj: Any?) {
                _isLoading.value = false

                if (obj != null && obj is List<*>) {
                    if (obj.isEmpty()) {
                        _isEmptyList.value = true
                        return
                    }

                    _isEmptyList.value = false

                    val listTv = obj as List<TvShow>
                    launch {
                        withContext(Dispatchers.IO) {
                            for (i in listTv.indices) {
                                val isFav = favoriteRepo.isFavorite(listTv[i].id as Int)
                                listTv[i].isFavorite = isFav
                            }
                            _tvShows.postValue(listTv)
                        }
                    }
                }
            }

            override fun onError(obj: Any?) {
                _isEmptyList.value = false
                _isLoading.value = false
                _onMessageError.value = obj
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun updateTvShowFavorite(tvShow: TvShow, position: Int) {
        val akanUpdate: ArrayList<TvShow> = _tvShows.value as ArrayList<TvShow>

        akanUpdate[position].id?.let {
            if (akanUpdate[position].isFavorite) {
                launch { favoriteRepo.deleteOne(it) }
                akanUpdate[position].isFavorite = false
            } else {
                val favItem = Favorite(
                    0,
                    tvShow.id ?: 0,
                    tvShow.name ?: "",
                    tvShow.firstAirDate ?: "2020-01-01",
                    tvShow.overview ?: "",
                    tvShow.posterPath ?: "",
                    Consts.TV_SHOW_TYPE
                )
                launch { favoriteRepo.insert(favItem) }
                akanUpdate[position].isFavorite = true
            }
            _tvShows.value = akanUpdate
        }
    }

    fun setDataChanged() {
        _isDataChanged.value = true
    }
}