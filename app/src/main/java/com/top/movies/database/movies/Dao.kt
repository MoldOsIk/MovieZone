package com.top.movies.database.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE category = :category")
    fun getMoviesByCategory(category: String): Flow<List<Movie>>

    @Query("SELECT * FROM movies")
    fun getMovies(): Flow<List<Movie>>
}
