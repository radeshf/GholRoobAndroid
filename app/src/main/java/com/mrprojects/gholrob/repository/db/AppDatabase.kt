package com.mrprojects.gholrob.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.model.Puzzle
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.PuzzleDao
import com.mrprojects.gholrob.repository.db.dao.UserDao

@Database(
    entities = [Puzzle::class, User::class], version = AppConfig.DATABASE_VERSION, exportSchema = false

)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun puzzleDao(): PuzzleDao

}