package com.top.movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.top.movies.data.remote.OmdbApi
import com.top.movies.data.repository.AuthRepository
import com.top.movies.data.repository.MovieRepository
import com.top.movies.database.favorite_movies.SavedMovie
import com.top.movies.database.favorite_movies.SavedMovieDao
import com.top.movies.database.movies.Movie
import com.top.movies.database.movies.MovieDao
import com.top.movies.database.users.SessionManager
import com.top.movies.database.users.User
import com.top.movies.database.users.UserDao
import com.top.movies.domain.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideUserDao(database: MovieDatabase): UserDao
    {
        return database.userDao()
    }

    @Provides
    fun provideApi(): OmdbApi {
        return RetrofitClient.api
    }

    @Provides
    fun provideSavedMovieDao(database: MovieDatabase): SavedMovieDao {
        return database.savedMovieDao()
    }

    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
    @Provides
    fun provideAuthRepository(userDao: UserDao, sessionManager: SessionManager): AuthRepository =
        AuthRepository(userDao, sessionManager)
    @Provides
    fun provideRepository(
        movieDao: MovieDao,
        savedMovieDao: SavedMovieDao,
        api: OmdbApi,
    ): MovieRepository {
        return MovieRepository(movieDao, savedMovieDao,api)
    }
}

@Database(entities = [Movie::class, User::class, SavedMovie::class], version = 4)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun userDao(): UserDao
    abstract fun savedMovieDao(): SavedMovieDao

}
