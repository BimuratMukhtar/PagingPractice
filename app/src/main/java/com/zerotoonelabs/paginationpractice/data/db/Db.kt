package com.zerotoonelabs.paginationpractice.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DATABASE_NAME = "erap.db"

@Database(entities = arrayOf(MovieFavorite::class), version = 1, exportSchema = false)
abstract class Db : RoomDatabase() {
    abstract fun movieDao(): MovieDao


    companion object {

        @Volatile
        private var INSTANCE: Db? = null

        fun getInstance(context: Context): Db =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                Db::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}