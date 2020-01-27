package com.medialink.favorite.ui.main

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medialink.favorite.Consts
import com.medialink.favorite.R
import com.medialink.favorite.adapter.FavoriteAdapter
import com.medialink.favorite.database.Favorite
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), FavoriteAdapter.ItemClickListener {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
            .apply {
                setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUi()
    }

    private fun setupViewModel() {
        with(pageViewModel) {
            text.observe(viewLifecycleOwner, Observer<Int> {
                if (it == 1) {
                    pageViewModel.getAllMovie()

                    /*GlobalScope.launch(Dispatchers.Main) {
                        val deferredNotes = async(Dispatchers.IO) {
                            //                val cursor = noteHelper.queryAll()
                            Log.d("debug", Consts.FavoriteColumn.FAVORITE_URI.toString())
                            val cursor = context?.contentResolver?.query(Consts.FavoriteColumn.FAVORITE_URI,
                                null,
                                null,
                                null,
                                null) as Cursor

                            return@async Favorite.mapCursorToArrayList(cursor)
                        }

                        val notes = deferredNotes.await()
                        Log.d("debug", "${notes.size}")
                        if (notes.size > 0) {
                            adapter.replaceAll(notes)
                        }
                    }*/

                } else {
                    pageViewModel.getAllTvShow()
                }
            })


            movie.observe(viewLifecycleOwner, Observer {
                val arrayMovies: ArrayList<Favorite> = ArrayList(it ?: emptyList())

                Log.d("debug", "movie.observe ${arrayMovies.size}")

                adapter.replaceAll(arrayMovies)
                val visibility = if (arrayMovies.isNotEmpty()) View.GONE else View.VISIBLE
                layout_empty.visibility = visibility
            })

            tvshow.observe(viewLifecycleOwner, Observer {
                val arrayTvShow: ArrayList<Favorite> = ArrayList(it ?: emptyList())

                Log.d("debug", "tvshow.observe ${arrayTvShow.size}")

                adapter.replaceAll(arrayTvShow)
                val visibility = if (arrayTvShow.isNotEmpty()) View.GONE else View.VISIBLE
                layout_empty.visibility = visibility
            })
        }
    }

    private fun setupUi() {
        rv_favorite.setHasFixedSize(true)
        val i = resources.configuration.orientation
        if (i == Configuration.ORIENTATION_PORTRAIT) {
            rv_favorite.layoutManager = LinearLayoutManager(context)
        } else {
            rv_favorite.layoutManager = GridLayoutManager(context, 2)
        }
        rv_favorite.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        val dataPertama: ArrayList<Favorite> =
            ArrayList(pageViewModel.favorite.value ?: emptyList())
        adapter = FavoriteAdapter(
            dataPertama,
            this
        )
        rv_favorite.adapter = adapter
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onItemClicked(favorite: Favorite, position: Int) {
        Log.d("debug", "onItemClicked :")
    }

    override fun onDeleteClicked(favorite: Favorite, position: Int) {
        val uriWithId = Uri.parse(
            Consts.FavoriteColumn.URI_FAVORITE.toString() +
                    "/" + favorite.id
        )

        val alertDialog = AlertDialog.Builder(context as Context)
        alertDialog.setTitle("Hapus Favorite")
            .setMessage("Anda Yakin Ingin Hapus Item Ini?")
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                pageViewModel.deleteFavorite(uriWithId)
                adapter.removeItem(position)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.cancel()
            }

        val alert = alertDialog.create()
        alert.show()
    }
}