package com.top.movies.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.top.movies.common.adventureIds
import com.top.movies.common.fantasyIds
import com.top.movies.common.horrorIds
import com.top.movies.common.superheroIds
import com.top.movies.common.top50Ids
import com.top.movies.database.users.SessionManager
import com.top.movies.presentation.movie_detail.components.MovieViewModel
import com.top.movies.presentation.auth_detail.components.AuthViewModel
import com.top.movies.presentation.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val movieViewModel: MovieViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                // Обновляем список ID фильмов
                movieViewModel.fetchAndSaveMovies(top50Ids, "Top 50")
                movieViewModel.fetchAndSaveMovies(superheroIds, "Superheroes")
                movieViewModel.fetchAndSaveMovies(adventureIds, "Adventure")
                movieViewModel.fetchAndSaveMovies(fantasyIds, "Fantasy")
                movieViewModel.fetchAndSaveMovies(horrorIds, "Horror")

            }

            AppNavigator(movieViewModel,authViewModel, sessionManager)
        }
    }
}


