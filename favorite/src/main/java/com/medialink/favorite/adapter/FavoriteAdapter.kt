package com.medialink.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.medialink.favorite.Consts
import com.medialink.favorite.R
import com.medialink.favorite.database.Favorite
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteAdapter(
    private var favorites: ArrayList<Favorite>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(favorite: Favorite, position: Int)
        fun onDeleteClicked(favorite: Favorite, position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite, position: Int) {
            with(itemView) {
                tv_title_favorite.text = favorite.title
                tv_date_favorite.text = favorite.date
                tv_overview_favorite.text = favorite.overview
                img_poster_favorite.load("${Consts.TMDB_PHOTO_URL}${favorite.poster}") {
                    crossfade(true)
                    placeholder(R.drawable.ic_timelapse_black_24dp)
                    transformations(RoundedCornersTransformation(8f, 8f, 8f, 8f))
                }

                this.setOnClickListener { itemClickListener.onItemClicked(favorite, position) }
                btn_delete_favorite.setOnClickListener { itemClickListener.onDeleteClicked(favorite, position) }
            }
        }
    }

    fun replaceAll(data: ArrayList<Favorite>) {
        this.favorites = data
        notifyDataSetChanged()
    }

    fun addItem(data: Favorite) {

        this.favorites.add(data)
        notifyItemInserted(this.favorites.size - 1)
    }

    fun updateItem(position: Int, data: Favorite) {
        this.favorites[position] = data
        notifyItemChanged(position, data)
    }

    fun removeItem(position: Int) {
        this.favorites.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.favorites.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(favorites[position], position)
    }


}