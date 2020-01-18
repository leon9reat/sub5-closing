package com.medialink.sub5close.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.medialink.sub5close.Consts

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM ${Consts.TABLE_NAME} WHERE ${Consts.FIELD_TYPE} = :ptype")
    fun getFavorite(ptype: Int): LiveData<List<Favorite>>

    @Query("SELECT COUNT(id) FROM ${Consts.TABLE_NAME} WHERE tmdb_id = :pid")
    suspend fun getCountFavorite(pid: Int): Int

    @Query("DELETE FROM ${Consts.TABLE_NAME} WHERE tmdb_id = :tmdbId")
    suspend fun deleteOneFavorite(tmdbId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)
}