package com.medialink.favorite

import android.media.DrmInitData
import android.net.Uri
import android.provider.BaseColumns

object Consts {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_PHOTO_URL = "https://image.tmdb.org/t/p/w185"
    const val TMDB_PHOTO_URL2 = "https://image.tmdb.org/t/p/w342"


    const val DATABASE_NAME = "db_favorite"
    const val TABLE_NAME = "table_favorite"
    const val FIELD_TYPE = "tmdb_type"
    const val MOVIE_TYPE = 1
    const val TV_SHOW_TYPE = 2

    /* digunakan untuk content provider */
    const val AUTHORITY = "com.medialink.sub5close"
    const val SCHEME = "content"

    class FavoriteColumn: BaseColumns {
        companion object {
            const val _ID = "id"
            const val TMDB_ID = "tmdb_id"
            const val TITLE = "tmdb_title"
            const val DATE = "tmdb_date"
            const val OVERVIEW = "tmdb_overview"
            const val POSTER = "tmdb_poster"
            const val TYPE = "tmdb_type"  // 1 movie, 2 tv


            val URI_FAVORITE: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

            val URI_MOVIE : Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("movie")
                .build()

            val URI_TVSHOW : Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("tvshow")
                .build()
        }
    }
}