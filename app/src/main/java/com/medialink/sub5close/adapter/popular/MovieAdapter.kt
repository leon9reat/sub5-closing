package com.medialink.sub5close.adapter.popular

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.medialink.sub4moviedb.model.movie.Movie
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.coroutines.*

class MovieAdapter(
    private var movies: List<Movie>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(movie: Movie, position: Int)
        fun onLikeClicked(movie: Movie, position: Int)
        fun onShareClicked(movie: Movie)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie, position: Int) {
            with(itemView) {
                tv_title_movie_list.text = movie.title
                tv_date_movie.text = movie.releaseDate
                tv_overview_movie.text = movie.overview
                img_poster_movie_list.load("${Consts.TMDB_PHOTO_URL}${movie.posterPath}") {
                    crossfade(true)
                    placeholder(R.drawable.ic_file_download_black_24dp)
                    error(R.drawable.no_image)
                    transformations(RoundedCornersTransformation(8f, 8f, 8f, 8f))
                }

                tv_vote_movie.text = movie.voteAverage.toString()
                progress_vote_movie.progress = movie.voteAverage?.times(10)?.toInt() ?: 0

                btn_like.setOnClickListener { itemClickListener.onLikeClicked(movie, position) }
                btn_share.setOnClickListener { itemClickListener.onShareClicked(movie) }
                this.setOnClickListener { itemClickListener.onItemClicked(movie, position) }

                val warna = if (movie.isFavorite)
                    ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
                else ColorStateList.valueOf(resources.getColor(R.color.colorSecondaryText))

                btn_like.iconTint = warna
                btn_like.setTextColor(warna)
            }
        }
    }

    fun replaceAll(data: List<Movie>) {
        Log.d("debug", "replace all")
        this.movies = data
        notifyDataSetChanged()
    }

    fun updateData(data: List<Movie>) {

        Log.d("debug", "update all")

        CoroutineScope(Dispatchers.IO).launch {
            val a = async {
                val tempData : MutableList<Movie> = movies as MutableList<Movie>
                tempData.addAll(data)
                return@async tempData
            }

            withContext(Dispatchers.Main) {
                movies = a.await()
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(movies[position], position)
    }


}