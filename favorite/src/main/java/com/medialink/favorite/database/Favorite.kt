package com.medialink.favorite.database

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.medialink.favorite.Consts
import kotlinx.android.parcel.Parcelize

@Entity(tableName = Consts.TABLE_NAME)
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tmdb_id") val tmdb_id: Int,
    @ColumnInfo(name = "tmdb_title") val title: String,
    @ColumnInfo(name = "tmdb_date") val date: String,
    @ColumnInfo(name = "tmdb_overview") val overview: String,
    @ColumnInfo(name = "tmdb_poster") val poster: String,
    @ColumnInfo(name = "tmdb_type") val type: Int // 1 movie, 2 tv
) : Parcelable {

    companion object {
        fun fromContentValues(values: ContentValues): Favorite {
            val favorite: Favorite = Favorite(
                values.getAsInteger(Consts.FavoriteColumn._ID),
                values.getAsInteger(Consts.FavoriteColumn.TMDB_ID),
                values.getAsString(Consts.FavoriteColumn.TITLE),
                values.getAsString(Consts.FavoriteColumn.DATE),
                values.getAsString(Consts.FavoriteColumn.OVERVIEW),
                values.getAsString(Consts.FavoriteColumn.POSTER),
                values.getAsInteger(Consts.FavoriteColumn.TYPE)

            )
            return favorite
        }

        fun mapCursorToArrayList(cursorFavorite: Cursor): ArrayList<Favorite> {
            val favList = ArrayList<Favorite>()
            with(cursorFavorite) {
                //moveToFirst()
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn._ID))
                    val tmdb_id = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn.TMDB_ID))
                    val title = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.TITLE))
                    val date = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.DATE))
                    val overview = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.OVERVIEW))
                    val poster = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.POSTER))
                    val type = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn.TYPE))
                    favList.add(Favorite(id, tmdb_id, title, date, overview, poster, type))
                }
            }
            return favList
        }

        fun mapCursorToObject(favCursor: Cursor): Favorite {
            with(favCursor) {
                favCursor.moveToNext()
                val id = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn._ID))
                val tmdb_id = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn.TMDB_ID))
                val title = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.TITLE))
                val date = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.DATE))
                val overview = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.OVERVIEW))
                val poster = getString(getColumnIndexOrThrow(Consts.FavoriteColumn.POSTER))
                val type = getInt(getColumnIndexOrThrow(Consts.FavoriteColumn.TYPE))

                return Favorite(id, tmdb_id, title, date, overview, poster, type)
            }

        }
    }
}