package com.arctouch.codechallenge.data.source

import android.content.Context
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie

interface MoviesDataSource {
    interface LoadMoviesCallback {
        fun onMoviesLoaded(movies: List<Movie>)

        fun onDataNotAvailable()
    }

    interface GetMovieCallback {
        fun onMovieLoaded(movie: Movie)

        fun onDataNotAvailable()
    }

    interface LoadGenresCallback {
        fun onGenresLoaded(genres: List<Genre>)

        fun onDataNotAvailable()
    }

    fun getMovies(callback: LoadMoviesCallback)

    fun getMovie(movieId: Long, callback: GetMovieCallback)

    fun getGenres(callback: LoadGenresCallback)
}