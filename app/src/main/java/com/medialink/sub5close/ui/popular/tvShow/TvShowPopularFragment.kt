package com.medialink.sub5close.ui.popular.tvShow


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import com.medialink.sub4moviedb.model.tv_show.TvShow
import com.medialink.sub5close.R
import com.medialink.sub5close.adapter.popular.TvShowAdapter
import kotlinx.android.synthetic.main.fragment_tv_show_popular.*
import kotlinx.android.synthetic.main.layout_error.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowPopularFragment : Fragment(), TvShowAdapter.ItemClickListener {

    private lateinit var tvShowViewModel: TvShowViewModel
    private lateinit var adapter: TvShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            tvShowViewModel = ViewModelProvider(it).get(TvShowViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_popular, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUi()

        if (!tvShowViewModel.localLanguage.equals(Locale.getDefault().country, ignoreCase = true)) {
            tvShowViewModel.loadTvShow(1)
            tvShowViewModel.localLanguage = Locale.getDefault().country
        }
    }

    private fun setupViewModel() {
        with(tvShowViewModel) {
            tvShows.observe(viewLifecycleOwner, Observer {
                layout_error.visibility = View.GONE
                layout_empty.visibility = View.GONE
                adapter.update(it)
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                val visibility = if (it) View.VISIBLE else View.GONE
                progress_tv_show.visibility = visibility
            })

            isEmptyList.observe(viewLifecycleOwner, Observer {
                val visibility = if (it) View.VISIBLE else View.GONE
                layout_empty.visibility = visibility
            })

            onMessageError.observe(viewLifecycleOwner, Observer {
                layout_empty.visibility = View.GONE
                layout_error.visibility = View.VISIBLE
                tv_error.text = "Error $it"
            })

            isDataChanged.observe(viewLifecycleOwner, Observer {
                if (it) {
                    loadTvShow(1)
                }
            })
        }
    }

    private fun setupUi() {
        rv_tv_show.setHasFixedSize(true)
        val i = resources.configuration.orientation
        if (i == Configuration.ORIENTATION_PORTRAIT) {
            rv_tv_show.layoutManager = LinearLayoutManager(context)
            rv_tv_show.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        } else {
            rv_tv_show.layoutManager = GridLayoutManager(context, 2)
            rv_tv_show.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        adapter = TvShowAdapter(
            tvShowViewModel.tvShows.value ?: emptyList(),
            itemClickListener = this
        )

        rv_tv_show.adapter = adapter
    }

    override fun onItemClicked(tvShow: TvShow, position: Int) {
        val toTvShowDetailFragment = TvShowPopularFragmentDirections
            .actionTvShowPopularFragmentToTvShowDetailFragment(tvShow)
        findNavController().navigate(toTvShowDetailFragment)
    }

    override fun onLikeClicked(tvShow: TvShow, position: Int) {
        tvShowViewModel.updateTvShowFavorite(tvShow, position)
        adapter.notifyItemChanged(position)
    }

    override fun onShareClicked(tvShow: TvShow) {
        Log.d("debug", "onShareClicked :")
    }
}
