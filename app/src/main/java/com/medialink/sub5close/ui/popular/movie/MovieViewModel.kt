package com.medialink.sub5close.ui.popular.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medialink.sub4moviedb.model.movie.Movie
import com.medialink.sub5close.Consts
import com.medialink.sub5close.data.OperationCallback
import com.medialink.sub5close.data.movie.MovieRepository
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.database.FavoriteRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val movieRepo = MovieRepository()
    private val favoriteRepo: FavoriteRepository

    private val _movies = MutableLiveData<List<Movie>>().apply { value = emptyList() }
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isDataChanged = MutableLiveData<Boolean>()
    val isDataChanged: LiveData<Boolean> = _isDataChanged

    var localLanguage: String = "en"

    init {
        val favDao = FavoriteDatabase.getDatabase(application.applicationContext)
            .favoriteDao()
        favoriteRepo = FavoriteRepository(favDao)
    }

    fun loadMovies(page: Int) {
        _isLoading.value = true

        movieRepo.page = page
        movieRepo.getMovies(object : OperationCallback {
            override fun onSuccess(obj: Any?) {
                Log.d("debug", "onSuccess :")

                _isLoading.value = false
                _isDataChanged.value = false

                if (obj != null && obj is List<*>) {
                    if (obj.isEmpty()) {
                        _isEmptyList.value = true
                    } else {
                        _isEmptyList.value = false

                        val listMovie = obj as List<Movie>
                        launch {
                            withContext(Dispatchers.IO) {
                                for (i in listMovie.indices) {
                                    val isFav = favoriteRepo.isFavorite(listMovie[i].id as Int)
                                    listMovie[i].isFavorite = isFav
                                }
                            }
                            _movies.postValue(listMovie)
                        }
                    }
                }
            }

            override fun onError(obj: Any?) {
                _isLoading.value = false
                _isEmptyList.value = false
                _onMessageError.value = obj
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun updateMovieFavorite(movie: Movie, position: Int) {
        val akanUpdate: ArrayList<Movie> = _movies.value as ArrayList<Movie>
        akanUpdate[position].id?.let {
            if (akanUpdate[position].isFavorite) {
                launch {
                    favoriteRepo.deleteOne(it)
                    Log.d("debug", "delete favorite $it")
                }
                akanUpdate[position].isFavorite = false

            } else {
                val favItem = Favorite(
                    0,
                    movie.id ?: 0,
                    movie.title ?: "",
                    movie.releaseDate ?: "2020-01-01",
                    movie.overview ?: "",
                    movie.posterPath ?: "",
                    Consts.MOVIE_TYPE
                )

                launch {
                    favoriteRepo.insert(favItem)
                    Log.d("debug", "insert favorite $it")
                }
                akanUpdate[position].isFavorite = true
            }

            _movies.value = akanUpdate
        }
    }

    fun setDataChanged() {
        _isDataChanged.value = true
    }
}