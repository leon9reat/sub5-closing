package com.medialink.sub5close.ui.popular.movie

import android.app.Application
import android.util.Log
import android.widget.Toast
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

    var page: Int
        get() = movieRepo.page
        set(value) {
            movieRepo.page = value
        }

    var searchText: String
        get() = movieRepo.searchText
        set(value) {
            movieRepo.searchText = value
        }

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

                    val tempList: MutableList<Movie>? = _movies.value?.toMutableList()

                    if (obj.isEmpty() && (tempList?.size ?: 0) < 1) {
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

                            Log.d("debug", "page: ${movieRepo.page}")

                            if (movieRepo.page > 1) {
                                tempList?.addAll(listMovie)
                                _movies.postValue(tempList)
                            } else {
                                _movies.postValue(listMovie)
                            }

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

    fun findMovie(page: Int, query: String) {
        _isLoading.value = true
        movieRepo.page = page
        movieRepo.searchText = query

        movieRepo.findMovies(object : OperationCallback {
            override fun onSuccess(obj: Any?) {
                _isLoading.value = false
                _isDataChanged.value = false

                if (obj != null && obj is List<*>) {
                    val tempList: MutableList<Movie>? = _movies.value?.toMutableList()

                    if (obj.isEmpty() && (tempList?.size ?: 0) < 1) {
                        _isEmptyList.value = true
                        return
                    }

                    _isEmptyList.value = false

                    val newList = obj as List<Movie>

                    // jika data yang load baru tidak ada, berarti udah page terakhir
                    /*if (newList.size < 1) {
                        Log.d("debug", "end of page")
                        return
                    }*/


                    launch {
                        withContext(Dispatchers.IO) {
                            for (i in newList.indices) {
                                val isFav = favoriteRepo.isFavorite(newList[i].id as Int)
                                newList[i].isFavorite = isFav
                            }
                        }
                    }

                    if (movieRepo.page > 1) {
                        tempList?.addAll(newList)
                        _movies.postValue(tempList)
                    } else {
                        _movies.postValue(newList)
                    }
                }
            }

            override fun onError(obj: Any?) {
                _isLoading.value = false
                _isEmptyList.value = false
                _onMessageError.value = obj
            }

        }, query)
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