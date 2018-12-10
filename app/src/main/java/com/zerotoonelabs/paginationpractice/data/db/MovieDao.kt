package com.zerotoonelabs.paginationpractice.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(movieId: MovieFavorite): Long?

    @Query("DELETE from movie_favorite where movie_id = :movieId")
    fun removeFromFavorite(movieId: Int): Int

    @Query("SELECT movie_id from movie_favorite where movie_id = :movieId")
    fun isFavorite(movieId: Int): Int?
}