package com.top.movies.data.repository

import com.top.movies.database.users.SessionManager
import com.top.movies.database.users.User
import com.top.movies.database.users.UserDao

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    suspend fun registerUser(username: String, password: String): Unit {
        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            throw Exception("Username already exists")
        }
        userDao.insertUser(User(username = username, password = password))
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        val user = userDao.loginUser(username, password)
        if (user != null) {
            sessionManager.saveUserSession(user.username)
            return true
        }
        return false
    }
}