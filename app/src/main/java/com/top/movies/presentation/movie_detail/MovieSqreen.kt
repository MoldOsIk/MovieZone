package com.top.movies.presentation.movie_detail

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.top.movies.database.movies.Movie
import com.top.movies.database.users.SessionManager
import com.top.movies.presentation.auth_detail.components.AuthViewModel
import com.top.movies.presentation.button.AnimatedButton
import com.top.movies.presentation.movie_detail.components.MovieViewModel
import kotlinx.coroutines.delay

@Composable
fun MovieScreen(viewModel: MovieViewModel, openProfile:()->Unit, sessionManager: SessionManager, width:Int, height:Int) {
    val currentUser = sessionManager.getUserSession().toString()
    var currentCategory by remember { mutableStateOf("Top 50") }
    val pagerState = rememberPagerState(pageCount = { 5 }, initialPage = 0)

    var sortByRating by remember { mutableStateOf(false) }
    var sortByYear by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val movies by viewModel.movies.collectAsState()




//     Сортировка фильмов
    val sortedMovies = when {
        sortByRating -> movies.sortedByDescending { it.rating }
        sortByYear -> movies.sortedByDescending { it.year }
        else -> movies
    }
    var showLazyColumn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        if(viewModel.loading.value)
        {
            delay(6000)
            viewModel.loading.value = false
            showLazyColumn = true
        }
        else{
            showLazyColumn = true
        }



    }

    LaunchedEffect(pagerState.currentPage) {
        val category = when (pagerState.currentPage) {
            0 -> "Top 50"
            1 -> "Adventure"
            2 -> "Fantasy"
            3 -> "Horror"
            4 -> "Superheroes"
            else -> "Top 50"
        }
        currentCategory = category
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {

        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        openProfile.invoke()
                    },
                tint =Color(0xFF1F4068)
            )

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth(0.9f)) { page ->
                val category = when (page) {
                    0 -> "Top 50"
                    1 -> "Adventure"
                    2 -> "Fantasy"
                    3 -> "Horror"
                    4 -> "Superheroes"
                    else -> "Top 50"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F4068)), // Темный фон карточки
                    elevation = CardDefaults.elevatedCardElevation(8.dp) // Добавляем тень для глубины
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF1F4068), Color(0xFF0D1B2A)) // Градиент от темно-синего до черного
                                )
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            modifier = Modifier.padding(8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }



                }
            }

        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when {
                    sortByRating -> "Sorting: By rating"
                    sortByYear -> "Sorting: By date"
                    else -> "Sorting: Default   "
                },
                modifier = Modifier
                    .clickable { isDropdownExpanded = true },
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFFFD700))
            )

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                offset = DpOffset(x = (-(60).dp), y = 8.dp), // Центрируем относительно текста
                modifier = Modifier
                    .background(
                        color = Color(0xFF0D1B2A)

                    )
            ) {
                // Пункт меню "По умолчанию"
                DropdownMenuItem(
                    onClick = {
                        sortByRating = false
                        sortByYear = false
                        isDropdownExpanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (!sortByRating && !sortByYear) Color(0xFF1F4068) else Color(0xFF303F56) // Выделение фоном
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Default",
                        color = if (!sortByRating && !sortByYear) Color(0xFFFFD700) else Color.White // Выделение текста
                    )
                }

                // Пункт меню "По рейтингу"
                DropdownMenuItem(
                    onClick = {
                        sortByRating = true
                        sortByYear = false
                        isDropdownExpanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (sortByRating) Color(0xFF1F4068) else Color(0xFF303F56) // Выделение фоном
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "By rating",
                        color = if (sortByRating) Color(0xFFFFD700) else Color.White // Выделение текста
                    )
                }

                // Пункт меню "По дате"
                DropdownMenuItem(
                    onClick = {
                        sortByRating = false
                        sortByYear = true
                        isDropdownExpanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (sortByYear) Color(0xFF1F4068) else Color(0xFF303F56) // Выделение фоном
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "By date",
                        color = if (sortByYear) Color(0xFFFFD700) else Color.White // Выделение текста
                    )
                }
            }
        }



        if (showLazyColumn) {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(sortedMovies.filter { it.category == currentCategory }) { movie ->
                    MovieCard(movie, onSaveMovie = { viewModel.saveMovie(movie,currentUser) }, width,height)
                }
            }
        }
        else
        {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFFFD700), // Золотистый цвет
                    strokeWidth = 4.dp
                )
            }
        }

    }

    BackHandler { }
}



@Composable
fun MovieCard(movie: Movie, onSaveMovie: (Movie) -> Unit, width:Int, height:Int) {
    val context = LocalContext.current
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
            AnimatedButton(onClick = {
                onSaveMovie(movie)
                Toast.makeText(context, "Фильм добавлен", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Save Movie",
                    modifier = Modifier
                        .size(width = (width*0.1).dp, height = (height*0.05).dp),
                    tint = Color(0xFFFFD700) // Золотистый цвет для кнопки
                )
            }

        }
    }
}


