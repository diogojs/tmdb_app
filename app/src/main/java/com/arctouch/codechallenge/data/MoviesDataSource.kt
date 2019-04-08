package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.Disposable

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

    fun getMovies(callback: LoadMoviesCallback, page: Long = 1) : Disposable

    fun getMovie(movieId: Long, callback: GetMovieCallback) : Disposable

    fun getGenres(callback: LoadGenresCallback) : Disposable
}