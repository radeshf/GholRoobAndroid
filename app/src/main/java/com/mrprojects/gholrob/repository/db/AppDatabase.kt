package com.mrprojects.gholrob.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.converters.StringArrayConverter
import com.mrprojects.gholrob.repository.db.dao.UserDao

@Database(
    entities = [User::class], version = AppConfig.DATABASE_VERSION, exportSchema = false

)
@TypeConverters(StringArrayConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}