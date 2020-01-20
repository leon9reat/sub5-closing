package com.medialink.sub5close.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.medialink.sub5close.R
import com.medialink.sub5close.adapter.favorite.FavoritePagerAdapter

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        (activity as AppCompatActivity?)?.let {
            val toolbar: Toolbar = root.findViewById(R.id.toolbar_favorite)
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.title = getString(R.string.title_movie_favorite)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val favoritePagerAdapter = FavoritePagerAdapter(it, childFragmentManager)
            val favoritePager: ViewPager = view.findViewById(R.id.pager_favorite)
            favoritePager.adapter = favoritePagerAdapter

            val tabs: TabLayout = view.findViewById(R.id.tab_favorite)
            tabs.setupWithViewPager(favoritePager)
            for (i in 0 until favoritePagerAdapter.count) {
                tabs.getTabAt(i)?.setIcon(FavoritePagerAdapter.TAB_IMAGE[i])
            }

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    Log.d("debug", "onTabReselected :")
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    Log.d("debug", "onTabUnselected :")
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    when (p0?.position) {
                        0 -> (activity as AppCompatActivity?)?.supportActionBar?.title =
                            getString(R.string.title_movie_favorite)
                        1 -> (activity as AppCompatActivity?)?.supportActionBar?.title =
                            getString(R.string.title_tv_show_favorite)
                    }
                }


            })
        }
    }
}