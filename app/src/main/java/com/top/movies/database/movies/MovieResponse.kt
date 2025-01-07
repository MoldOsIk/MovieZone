package com.top.movies.database.movies

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("imdbID") val id: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val posterUrl: String,
    @SerializedName("imdbRating") val rating: String
)
