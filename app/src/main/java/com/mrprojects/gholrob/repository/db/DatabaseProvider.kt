package com.mrprojects.gholrob.repository.db

import android.content.Context
import androidx.room.Room
import com.mrprojects.gholrob.AppConfig

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, AppConfig.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }

}