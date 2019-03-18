package com.arctouch.codechallenge.data.source.local

import android.util.Log
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.model.Movie

class MoviesLocalSource: MoviesDataSource {
    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback, page: Long) {
        callback.onMoviesLoaded(Cache.movies)
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        callback.onMovieLoaded(Cache.movie!!)
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        callback.onGenresLoaded(Cache.genres)
    }

    fun refreshMovies(movies: List<Movie>) {
        clear()
        for (movie in movies) {
            addMovie(movie)
        }
        Cache.cacheIsDirty = false
    }

    fun addMovie(movie: Movie) {
        (Cache.movies as ArrayList<Movie>).add(movie)
    }

    fun removeMovie(movie: Movie) {
        (Cache.movies as ArrayList<Movie>).remove(movie)
    }

    fun clear() {
        Cache.movies = listOf()
        Cache.genres = listOf()
        Cache.movie = null
    }

}