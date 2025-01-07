package com.top.movies.presentation.auth_detail

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.top.movies.R
import com.top.movies.database.users.SessionManager
import com.top.movies.presentation.auth_detail.components.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit, onRegistrationRequired: () -> Unit, sessionManager: SessionManager,width:Int, height:Int) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val entered  = viewModel.entered.collectAsState()
    val errorMessage by viewModel.loginError.collectAsState()

    LaunchedEffect(Unit) {
        sessionManager.clearSession()
        viewModel.entered.value = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding((width*0.01).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Логотип
            Image(
                painter = painterResource(id = R.drawable.movie_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(width = (width*0.38).dp, height = (height*0.2).dp)
                    .padding(bottom = 24.dp)
            )

            // Приветственный текст
            Text(
                text = "Welcome to MovieZone!",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле ввода логина
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Цвет контейнера при фокусе
                    unfocusedContainerColor = Color.Transparent, // Цвет контейнера без фокуса
                    focusedTextColor = Color.White, // Цвет текста при фокусе
                    unfocusedTextColor = Color.White, // Цвет текста без фокуса
                    focusedIndicatorColor = Color(0xFFFFD700), // Золотой акцент для индикатора
                    unfocusedIndicatorColor = Color.Gray // Серый акцент для индикатора
                )
            )

            Spacer(modifier = Modifier.height((height*0.02).dp))

            // Поле ввода пароля
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Цвет контейнера при фокусе
                    unfocusedContainerColor = Color.Transparent, // Цвет контейнера без фокуса
                    focusedTextColor = Color.White, // Цвет текста при фокусе
                    unfocusedTextColor = Color.White, // Цвет текста без фокуса
                    focusedIndicatorColor = Color(0xFFFFD700), // Золотой акцент для индикатора
                    unfocusedIndicatorColor = Color.Gray // Серый акцент для индикатора
                )
            )

            Spacer(modifier = Modifier.height((height*0.02).dp))

            // Кнопка входа
            Button(
                onClick = { viewModel.loginUser(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((height*0.07).dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4068)) // Темно-синий акцент
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp)
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {

                    onRegistrationRequired()
                    viewModel.loginError.value =null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((height*0.07).dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4068)) // Темно-синий акцент
            ) {
                Text("I don't have an account. ", color = Color(0xFFFFD700), fontSize = 16.sp)
            }
        }


        // Проверка успешного входа
        LaunchedEffect(entered.value) {
            if (entered.value) {
                onLoginSuccess()
            }
        }
    }
    BackHandler {

    }
}

