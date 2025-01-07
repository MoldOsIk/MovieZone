package com.top.movies.database.favorite_movies

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMovieDao {
    @Query("SELECT * FROM saved_movies WHERE username = :username")
    fun getSavedMoviesForUser(username: String): Flow<List<SavedMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: SavedMovie)

    @Query("SELECT * FROM saved_movies")
    fun getAllSavedMovies(): Flow<List<SavedMovie>>

    @Delete
    suspend fun deleteMovie(movie: SavedMovie)
}