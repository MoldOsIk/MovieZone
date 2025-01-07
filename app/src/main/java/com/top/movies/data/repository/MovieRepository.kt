package com.top.movies.data.repository

import com.top.movies.data.remote.OmdbApi
import com.top.movies.database.favorite_movies.SavedMovie
import com.top.movies.database.favorite_movies.SavedMovieDao
import com.top.movies.database.movies.Movie
import com.top.movies.database.movies.MovieDao
import com.top.movies.database.users.SessionManager
import com.top.movies.database.users.User
import com.top.movies.database.users.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val savedmovieDao: SavedMovieDao,
    private val api: OmdbApi,

) {

    fun getMovies(): Flow<List<Movie>> {
        return movieDao.getMovies()
    }

    fun getSavedMovies(): Flow<List<SavedMovie>> {
        return savedmovieDao.getAllSavedMovies()
    }
    suspend fun deleteSavedMovie(movie: SavedMovie) {
        savedmovieDao.deleteMovie(movie)
    }

    suspend fun fetchAndSaveMovies(movieIds: List<String>, category: String) {
        movieIds.forEach { imdbId ->
            try {
                val response = api.getMovieDetails(imdbId)
                val movie = Movie(
                    id = response.id,
                    title = response.title,
                    year = response.year,
                    category = category,
                    posterUrl = response.posterUrl,
                    rating = response.rating
                )
                movieDao.insertMovies(listOf(movie))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}