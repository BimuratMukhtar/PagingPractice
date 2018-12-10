package com.zerotoonelabs.paginationpractice.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_favorite")
data class MovieFavorite(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    var movieId: Int
)