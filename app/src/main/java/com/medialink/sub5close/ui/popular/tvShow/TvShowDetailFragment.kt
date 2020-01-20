package com.medialink.sub5close.ui.popular.tvShow


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.medialink.sub4moviedb.model.tv_show.TvShow
import com.medialink.sub5close.Consts

import com.medialink.sub5close.R
import kotlinx.android.synthetic.main.fragment_tv_show_detail.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowDetailFragment : Fragment() {

    private lateinit var args: TvShow

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        args = TvShowDetailFragmentArgs.fromBundle(arguments as Bundle).tvShow
        return inflater.inflate(R.layout.fragment_tv_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title_tvshow.text = args.name
        tv_airing_tvshow.text = args.firstAirDate
        tv_vote_tvshow.text = args.voteAverage.toString()
        img_poster_tvshow.load("${Consts.TMDB_PHOTO_URL2}${args.posterPath}") {
            crossfade(true)
            placeholder(R.drawable.ic_file_download_black_24dp)
        }
        progress_vote_tvshow.progress = args.voteAverage?.times(10)?.toInt() ?: 0
        tv_overview_tvshow.text = args.overview
    }
}
