package com.top.movies.presentation.movie_detail.components

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.top.movies.data.repository.MovieRepository
import com.top.movies.database.favorite_movies.SavedMovie
import com.top.movies.database.favorite_movies.SavedMovieDao
import com.top.movies.database.movies.Movie
import com.top.movies.database.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository, private val savedMovieDao: SavedMovieDao
) : ViewModel() {


    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

   val loading = mutableStateOf(true)

    private val _savedmovies = MutableStateFlow<List<SavedMovie>>(emptyList())
    val savedmovies: StateFlow<List<SavedMovie>> = _savedmovies.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovies().collect { movieList ->
                if (_movies.value != movieList) {
                    _movies.value = movieList
                }
            }
        }
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSavedMovies().collect { savedmovieList ->
                if (_savedmovies.value != savedmovieList) {
                    _savedmovies.value = savedmovieList
                }
            }
        }
    }
    fun deleteSavedMovie(movie: SavedMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedMovie(movie) // Обращение к репозиторию для удаления
        }
    }
    fun fetchSavedMovies(username: String) {
        viewModelScope.launch {
            savedMovieDao.getSavedMoviesForUser(username).collect { movies ->
                _savedmovies.value = movies

            }
        }
    }
    fun saveMovie(movie: Movie, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMovie = SavedMovie(
                id = movie.id,
                title = movie.title,
                year = movie.year,
                rating = movie.rating,
                category = movie.category,
                posterUrl = movie.posterUrl,
                username = username
            )
            savedMovieDao.insertMovie(savedMovie)
        }
    }



    // Функция для получения и сохранения фильмов
    fun fetchAndSaveMovies(movieIds: List<String>, category: String) {

        viewModelScope.launch {
            repository.fetchAndSaveMovies(movieIds, category)
        }
    }
}
