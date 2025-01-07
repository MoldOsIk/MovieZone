package com.top.movies.presentation.auth_detail

import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.top.movies.R
import com.top.movies.presentation.auth_detail.components.AuthViewModel

@Composable
fun RegistrationScreen(viewModel: AuthViewModel, onRegistered: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val toastMessage by viewModel.toastMessage.collectAsState()
    val context = LocalContext.current

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.toastMessage.value = null
        }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Логотип
            Image(
                painter = painterResource(id = R.drawable.movie_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 24.dp)
            )

            // Приветственный текст
            Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле ввода имени пользователя
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
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка регистрации
            Button(
                onClick = {
                    if (viewModel.validateFields(username,password)) {
                        viewModel.registerUser(username, password) { result ->
                            result.fold(
                                onSuccess = {
                                    onRegistered() // Успешная регистрация
                                    showToast("Registration successful!")
                                },
                                onFailure = { exception ->
                                    showToast(exception.message ?: "Registration failed") // Ошибка регистрации
                                }
                            )
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4068)) // Темно-синий акцент
            ) {
                Text("Register", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))



        }
    }

}
