package com.medialink.sub5close.ui.popular.movie


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import coil.api.load
import com.medialink.sub4moviedb.model.movie.Movie
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.progress_vote_movie
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_overview_movie
import kotlinx.android.synthetic.main.fragment_movie_detail.tv_vote_movie

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailFragment : Fragment() {

    private lateinit var args: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        args = MovieDetailFragmentArgs.fromBundle(arguments as Bundle).movie
        val root = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        (activity as AppCompatActivity?)?.let {
            val toolbar: Toolbar = root.findViewById(R.id.toolbar_global)
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.title = getString(R.string.title_movie_detail)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title_movie_detail.text = args.title
        tv_release_movie.text = args.releaseDate
        tv_vote_movie.text = args.voteAverage?.toString()
        img_poster_movie_detail.load("${Consts.TMDB_PHOTO_URL2}${args.posterPath}") {
            crossfade(true)
            placeholder(R.drawable.ic_file_download_black_24dp)
        }
        progress_vote_movie.progress = args.voteAverage?.times(10)?.toInt() ?: 0
        tv_overview_movie.text = args.overview
    }
}
