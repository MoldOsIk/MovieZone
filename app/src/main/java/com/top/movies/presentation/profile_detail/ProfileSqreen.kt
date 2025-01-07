package com.top.movies.presentation.profile_detail


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.top.movies.database.users.SessionManager
import com.top.movies.presentation.auth_detail.components.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.top.movies.database.favorite_movies.SavedMovie
import com.top.movies.database.movies.Movie
import com.top.movies.presentation.button.AnimatedButton
import com.top.movies.presentation.movie_detail.MovieCard
import com.top.movies.presentation.movie_detail.components.MovieViewModel


@Composable
fun ProfileScreen(
    logOut: () -> Unit,
    sessionManager: SessionManager,
    authViewModel: AuthViewModel,
    width: Int,
    height: Int,
    viewModel: MovieViewModel,
    back:()->Unit
) {
    val currentUser = sessionManager.getUserSession()




    LaunchedEffect(currentUser)
    {
        if (currentUser != null) {
            viewModel.fetchSavedMovies(currentUser)
        }
    }
    val savedMoves by viewModel.savedmovies.collectAsState()
    val userSavedMovies = savedMoves.filter { it.username == currentUser }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.TopCenter
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Log Out",
            modifier = Modifier
                .size((width * 0.2f).dp)
                .align(Alignment.TopStart)
                .padding((width*0.04).dp)
                .clickable {
                    logOut.invoke()
                    sessionManager.clearSession()
                    authViewModel.entered.value = false

                }
                .rotate(180f),
            tint = Color.White
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size((width * 0.2f).dp)
                .align(Alignment.TopEnd)
                .padding((width*0.04).dp)
                .clickable {
                    back.invoke()
                }
                .rotate(180f),
            tint = Color.White
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = (height*0.1).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



                // Приветственный текст с логином пользователя
                Text(
                    text = "Welcome, $currentUser!",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                    textAlign = TextAlign.Center
                )





            Spacer(modifier = Modifier.height((height*0.1).dp))
            Text(
                text = "Your favourites movies:",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(24.dp))


            LazyColumn(modifier = Modifier.padding((width*0.06).dp)) {
                items(userSavedMovies) { SavedMovie ->
                    SavedMovieCard(SavedMovie,
                        onDelete = { viewModel.deleteSavedMovie(SavedMovie)},
                        width, height
                        )
                }
            }

        }
        }
    }


@Composable
fun SavedMovieCard(
    movie: SavedMovie,
    onDelete: () -> Unit,
    width: Int,
    height: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F4068)), // Установить цвет фона карточки
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .size(width = (width*0.225).dp, height = (height*0.13).dp)
                    .padding(end = (width*0.03).dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height((height*0.015).dp))
                Text(
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Spacer(modifier = Modifier.height((height*0.0075).dp))
                Text(
                    text = "Rating: ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }

            AnimatedButton(onClick =
            {
                onDelete()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete, // Иконка мусорки
                    contentDescription = "Delete Movie",
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color(0xFFB71C1C)
                )
            }


        }
    }
}
