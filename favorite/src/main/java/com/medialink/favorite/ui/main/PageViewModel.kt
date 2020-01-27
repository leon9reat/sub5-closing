package com.medialink.favorite.ui.main

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.medialink.favorite.Consts
import com.medialink.favorite.database.Favorite
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PageViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val context: Context = application.applicationContext

    val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _index = MutableLiveData<Int>()
    val text: LiveData<Int> = Transformations.map(_index) {
        it
    }

    private val _favorite = MutableLiveData<List<Favorite>>().apply { value = emptyList() }
    val favorite: LiveData<List<Favorite>> = _favorite

    private val _movie = MutableLiveData<List<Favorite>>().apply { value = emptyList() }
    val movie: LiveData<List<Favorite>> = _movie

    private val _tvshow = MutableLiveData<List<Favorite>>().apply { value = emptyList() }
    val tvshow: LiveData<List<Favorite>> = _tvshow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getAllFavorite() {

        launch {
            _isLoading.postValue(true)
            val defferedFavorite = async(Dispatchers.IO) {
                Log.d("debug", Consts.FavoriteColumn.URI_FAVORITE.toString())
                val cursor = context.contentResolver.query(
                    Consts.FavoriteColumn.URI_FAVORITE,
                    null,
                    null,
                    null,
                    null
                ) as Cursor

                return@async Favorite.mapCursorToArrayList(cursor)
            }
            _isLoading.postValue(false)

            val favList = defferedFavorite.await()

            if (favList.size > 0) {
                _isEmptyList.postValue(false)
                _favorite.postValue(favList)
            } else {
                _isEmptyList.postValue(true)
                _favorite.postValue(emptyList())
            }
        }
    }

    fun getAllMovie() {
        launch {
            _isLoading.postValue(true)
            val defferedFavorite = async(Dispatchers.IO) {
                val cursor = context.contentResolver.query(
                    Consts.FavoriteColumn.URI_MOVIE,
                    null,
                    null,
                    null,
                    null
                ) as Cursor

                return@async Favorite.mapCursorToArrayList(cursor)
            }
            _isLoading.postValue(false)

            val favList = defferedFavorite.await()

            Log.d("debug", Consts.FavoriteColumn.URI_MOVIE.toString() + " ${favList.size}")

            if (favList.size > 0) {
                _isEmptyList.postValue(false)
                _movie.postValue(favList)
            } else {
                _isEmptyList.postValue(true)
                _movie.postValue(emptyList())
            }
        }
    }

    fun getAllTvShow() {
        launch {
            _isLoading.postValue(true)
            val defferedFavorite = async(Dispatchers.IO) {
                Log.d("debug", Consts.FavoriteColumn.URI_TVSHOW.toString())
                val cursor = context.contentResolver.query(
                    Consts.FavoriteColumn.URI_TVSHOW,
                    null,
                    null,
                    null,
                    null
                ) as Cursor

                return@async Favorite.mapCursorToArrayList(cursor)
            }
            _isLoading.postValue(false)

            val favList = defferedFavorite.await()

            if (favList.size > 0) {
                _isEmptyList.postValue(false)
                _tvshow.postValue(favList)
            } else {
                _isEmptyList.postValue(true)
                _tvshow.postValue(emptyList())
            }
        }
    }

    fun deleteFavorite(uriWithId: Uri) {
        launch { context.contentResolver.delete(uriWithId, null, null) }
    }

    override fun onCleared() {
        job.cancel()
    }
}