package com.top.movies.database.favorite_movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movies")
data class SavedMovie(
    @PrimaryKey val id: String,
    val title: String,
    val year: String,
    val rating: String,
    val category: String,
    val posterUrl: String,
    val username: String
)