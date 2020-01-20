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
import kotlinx.android.synthetic.main.tvshow_favorite_item.view.*

class TvShowFavoriteAdapter(
    private var tvShows: List<Favorite>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TvShowFavoriteAdapter.MyViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(tvShow: Favorite)
        fun onDeleteClicked(tvShow: Favorite)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: Favorite) {
            with(itemView) {
                tv_title_tvshow_favorite.text = tvShow.title
                tv_date_tvshow_favorite.text = tvShow.date
                tv_overview_tvshow_favorite.text = tvShow.overview
                img_poster_tvshow_favorite.load("${Consts.TMDB_PHOTO_URL}${tvShow.poster}") {
                    crossfade(true)
                    placeholder(R.drawable.ic_file_download_black_24dp)
                    transformations(RoundedCornersTransformation(8F, 8F, 8F, 8F))
                }

                this.setOnClickListener { itemClickListener.onItemClicked(tvShow) }
                btn_delete_tvshow_favorite.setOnClickListener {
                    itemClickListener.onDeleteClicked(
                        tvShow
                    )
                }
            }
        }
    }

    fun update(data: List<Favorite>) {
        this.tvShows = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tvshow_favorite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int  = tvShows.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tvShows[position])
    }
}