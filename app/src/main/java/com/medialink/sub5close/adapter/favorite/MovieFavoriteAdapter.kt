package com.medialink.sub5close.adapter.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import com.medialink.sub5close.database.Favorite
import kotlinx.android.synthetic.main.movie_favorite_item.view.*

class MovieFavoriteAdapter(
    private var movies: List<Favorite>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MovieFavoriteAdapter.MyViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(movie: Favorite)
        fun onDeleteClicked(movie: Favorite)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Favorite) {
            with(itemView) {
                tv_title_movie_favorite.text = movie.title
                tv_date_movie_favorite.text = movie.date
                tv_overview_movie_favorite.text = movie.overview
                img_poster_movie_favorite.load("${Consts.TMDB_PHOTO_URL}${movie.poster}") {
                    crossfade(true)
                    placeholder(R.drawable.ic_file_download_black_24dp)
                    transformations(RoundedCornersTransformation(8F, 8F, 8F, 8F))
                }

                this.setOnClickListener { itemClickListener.onItemClicked(movie) }
                btn_delete_movie_favorite.setOnClickListener { itemClickListener.onDeleteClicked(movie) }
            }
        }

    }

    fun update(data: List<Favorite>) {
        this.movies = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_favorite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}