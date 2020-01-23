package com.medialink.sub5close.ui.favorite.movie


import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import com.medialink.sub5close.adapter.favorite.MovieFavoriteAdapter
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.ui.popular.movie.MovieViewModel
import com.medialink.sub5close.widget.Sub5Widget
import kotlinx.android.synthetic.main.fragment_movie_favorite.*


/**
 * A simple [Fragment] subclass.
 */
class MovieFavoriteFragment : Fragment(), MovieFavoriteAdapter.ItemClickListener {

    private lateinit var movieFavoriteViewModel: MovieFavoriteViewModel
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            movieFavoriteViewModel = ViewModelProvider(it).get(MovieFavoriteViewModel::class.java)
            movieViewModel = ViewModelProvider(it).get(MovieViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUi()
    }

    private fun setupViewModel() {
        with(movieFavoriteViewModel) {
            favMovie.observe(viewLifecycleOwner, Observer {
                adapter.update(it)
                val visibility = if (it.size > 0) View.GONE else View.VISIBLE
                layout_empty.visibility = visibility
            })
        }
    }

    private fun setupUi() {
        rv_movie_favorite.setHasFixedSize(true)
        val i = resources.configuration.orientation
        if (i == Configuration.ORIENTATION_PORTRAIT) {
            rv_movie_favorite.layoutManager = LinearLayoutManager(context)
        } else {
            rv_movie_favorite.layoutManager = GridLayoutManager(context, 2)
        }
        rv_movie_favorite.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))

        adapter = MovieFavoriteAdapter(
            movieFavoriteViewModel.favMovie.value ?: emptyList(),
            this
        )
        rv_movie_favorite.adapter = adapter
    }

    override fun onItemClicked(movie: Favorite) {
        val toFavoriteDetailFragment = MovieFavoriteFragmentDirections
            .actionMovieFavoriteFragmentToFavoriteDetailFragment(movie)
            .setType(Consts.MOVIE_TYPE)
        findNavController().navigate(toFavoriteDetailFragment)
    }

    override fun onDeleteClicked(movie: Favorite) {
        movieFavoriteViewModel.deleteFavorite(movie)
        movieViewModel.setDataChanged()
    }
}
