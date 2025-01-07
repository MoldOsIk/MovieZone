package com.top.movies.data.remote

import com.top.movies.database.movies.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = "cc85e540"
    ): MovieResponse
}
