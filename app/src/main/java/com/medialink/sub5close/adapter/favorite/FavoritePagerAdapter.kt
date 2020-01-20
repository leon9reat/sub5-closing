package com.medialink.sub5close.adapter.favorite

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.medialink.sub5close.R
import com.medialink.sub5close.ui.favorite.movie.MovieFavoriteFragment
import com.medialink.sub5close.ui.favorite.tvShow.TvShowFavoriteFragment

class FavoritePagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        val TAB_TITLE = intArrayOf(R.string.tab_movie, R.string.tab_tv_show)
        @StringRes
        val TAB_IMAGE = intArrayOf(R.drawable.ic_movie_black_24dp, R.drawable.ic_tv_black_24dp)
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFavoriteFragment()
            1 -> fragment = TvShowFavoriteFragment()
        }

        return fragment as Fragment
    }

    override fun getCount(): Int = TAB_TITLE.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLE[position])
    }
}