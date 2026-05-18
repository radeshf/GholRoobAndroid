package com.mrprojects.gholrob.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mrprojects.gholrob.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    suspend fun getUser(): User

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}