package com.mrprojects.gholrob.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mrprojects.gholrob.model.Puzzle
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzles")
    fun getPuzzles(): Single<List<Puzzle>>

    @Query("SELECT * FROM puzzles")
    fun getPuzzlesSimple(): List<Puzzle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: ArrayList<Puzzle>)

    @Upsert
    fun upsertAll(puzzles: ArrayList<Puzzle>)

    @Update
    fun update(puzzle: Puzzle)

    @Query("SELECT * FROM puzzles WHERE id = :puzzleId LIMIT 1")
    fun getPuzzleById(puzzleId: Int): Maybe<Puzzle>


}