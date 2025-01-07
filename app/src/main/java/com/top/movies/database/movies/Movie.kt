package com.top.movies.database.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String,
    val title: String,
    val year: String,
    val category: String,
    val posterUrl: String,
    val rating: String
)
