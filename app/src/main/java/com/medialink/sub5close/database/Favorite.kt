package com.medialink.sub5close.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.medialink.sub5close.Consts
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
}