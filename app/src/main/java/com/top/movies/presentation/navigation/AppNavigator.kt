package com.top.movies.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.top.movies.database.users.SessionManager
import com.top.movies.presentation.movie_detail.MovieScreen
import com.top.movies.presentation.movie_detail.components.MovieViewModel
import com.top.movies.presentation.auth_detail.components.AuthViewModel
import com.top.movies.presentation.auth_detail.LoginScreen
import com.top.movies.presentation.auth_detail.RegistrationScreen
import com.top.movies.presentation.profile_detail.ProfileScreen
import com.top.movies.presentation.splash_detail.SplashScreen

@Composable
fun AppNavigator(movieviewModel: MovieViewModel, authViewModel: AuthViewModel, sessionManager: SessionManager) {
    val navController = rememberNavController()

    val isLoggedIn = remember { sessionManager.getUserSession() != null }

    val startDestination = if (isLoggedIn) "movies" else "login" // Устанавливаем стартовый экран


    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    NavHost(navController = navController, startDestination =  "splash") {
        composable("login") {
            LoginScreen(
                authViewModel,
                onLoginSuccess = { navController.navigate("movies") },
                onRegistrationRequired = { navController.navigate("register") },
                sessionManager, width,height
            )
        }
        composable("register") {
            RegistrationScreen(authViewModel) {
                navController.navigate("login")
            }
        }
        composable("movies") {
            MovieScreen(movieviewModel, {navController.navigate("profile")}, sessionManager, width,height)
        }
        composable("profile") {
            ProfileScreen ( {navController.navigate("login")},
                sessionManager,authViewModel, width, height, movieviewModel, {navController.popBackStack()})
            }
        composable ( "splash" )
        {
            SplashScreen(onNavigateToMain = {navController.navigate(startDestination)}, width,height)
        }
    }
}
