package com.medialink.sub5close.ui.popular

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.medialink.sub5close.R
import com.medialink.sub5close.adapter.popular.PopularPagerAdapter
import com.medialink.sub5close.ui.popular.movie.MovieViewModel
import com.medialink.sub5close.ui.popular.tvShow.TvShowViewModel
import kotlinx.android.synthetic.main.fragment_popular.*

class PopularFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private var searchText : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            movieViewModel = ViewModelProvider(it).get(MovieViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_popular, container, false)
        (activity as AppCompatActivity?)?.let {
            val toolbar: Toolbar = root.findViewById(R.id.toolbar_popular)
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.title = getString(R.string.title_popular_movie)
        }
        setHasOptionsMenu(true)
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
                        0 -> (activity as AppCompatActivity?)?.supportActionBar?.title =
                            getString(R.string.title_popular_movie)
                        1 -> (activity as AppCompatActivity?)?.supportActionBar?.title =
                            getString(R.string.title_popular_tvshow)
                    }
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_popular, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_popular).actionView as SearchView
        val searchItem = menu.findItem(R.id.search_popular)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = "Entry Title"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItem.collapseActionView()
                val posisi = tab_popular.selectedTabPosition
                searchText = query ?: ""

                if (posisi == 0) {
                    // cari tab movie
                    movieViewModel.findMovie(1, query ?: "")
                } else {
                    // cari tab tv show
                    activity?.let {
                        val tvShowViewModel = ViewModelProvider(it).get(TvShowViewModel::class.java)
                        tvShowViewModel.setSearchText(searchText)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}