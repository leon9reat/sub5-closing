package com.medialink.sub5close.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.medialink.sub5close.Consts

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM ${Consts.TABLE_NAME} WHERE ${Consts.FIELD_TYPE} = :ptype")
    fun getFavorite(ptype: Int): LiveData<List<Favorite>>

    @Query("SELECT * FROM ${Consts.TABLE_NAME}")
    suspend fun getAllFavorite(): List<Favorite>

    @Query("SELECT COUNT(id) FROM ${Consts.TABLE_NAME} WHERE tmdb_id = :pid")
    suspend fun getCountFavorite(pid: Int): Int

    @Query("DELETE FROM ${Consts.TABLE_NAME} WHERE tmdb_id = :tmdbId")
    suspend fun deleteOneFavorite(tmdbId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    /* untuk content provider */
    @Query("SELECT COUNT(*) FROM ${Consts.TABLE_NAME}")
    fun count(): Int

    @Insert
    fun insertProvider(favorite: Favorite): Long

    @Query("SELECT * FROM ${Consts.TABLE_NAME}")
    fun selectAll(): Cursor

    @Query("SELECT * FROM ${Consts.TABLE_NAME} WHERE ${Consts.FavoriteColumn._ID} = :id")
    fun selectById(id: Long): Cursor

    @Query("SELECT * FROM ${Consts.TABLE_NAME} WHERE ${Consts.FavoriteColumn.TYPE} = :type")
    fun selectByType(type: Int): Cursor

    @Query("DELETE FROM ${Consts.TABLE_NAME} WHERE ${Consts.FavoriteColumn._ID} = :id")
    fun deleteById(id: Long): Int

    @Update
    fun updateProvider(favorite: Favorite): Int

}