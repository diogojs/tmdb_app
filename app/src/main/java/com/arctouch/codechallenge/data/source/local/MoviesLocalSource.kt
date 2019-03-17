package com.arctouch.codechallenge.data.source.local

import android.util.Log
import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.model.Movie

class MoviesLocalSource: MoviesDataSource {
    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback) {
        Log.d("LocalSource", "getMovies")
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        Log.d("LocalSource", "getMovie")
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        Log.d("LocalSource", "getGenres")
    }

    fun refreshMovies() {
        Log.d("LocalSource", "refreshMovies")
    }

    fun addMovie(movie: Movie) {
        Log.d("LocalSource", "addMovie")
    }

    fun removeMovie(movie: Movie) {
        Log.d("LocalSource", "removeMovie")
    }

    fun clear() {
        Log.d("LocalSource", "clear()")
    }

}
