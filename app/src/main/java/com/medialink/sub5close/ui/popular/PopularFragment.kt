package com.medialink.sub5close.ui.popular

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
import com.medialink.sub5close.adapter.popular.PopularPagerAdapter

class PopularFragment : Fragment() {

    private lateinit var popularViewModel: PopularViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_popular, container, false)
        (activity as AppCompatActivity?)?.let {
            val toolbar : Toolbar = root.findViewById(R.id.toolbar_popular)
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.title = getString(R.string.title_popular_movie)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // setting tab layout
        activity?.let {
            val popularPagerAdapter = PopularPagerAdapter(it, childFragmentManager)
            val popularPager: ViewPager = view.findViewById(R.id.pager_popular)
            popularPager.adapter = popularPagerAdapter

            val tabs: TabLayout = view.findViewById(R.id.tab_popular)
            tabs.setupWithViewPager(popularPager)
            for (i in 0 until popularPagerAdapter.count) {
                tabs.getTabAt(i)?.setIcon(PopularPagerAdapter.TAB_IMAGE[i])
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
                        0 ->(activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.title_popular_movie)
                        1 ->(activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.title_popular_tvshow)
                    }
                }

            })
        }
    }
}