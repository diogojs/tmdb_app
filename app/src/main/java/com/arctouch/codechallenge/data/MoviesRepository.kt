package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Movie

class MoviesRepository private constructor(
    private var moviesRemoteDataSource: MoviesDataSource
) : MoviesDataSource {

    companion object {
        private var INSTANCE: MoviesRepository? = null

        @JvmStatic
        fun getInstance(remoteDataSource: MoviesDataSource): MoviesRepository {
            INSTANCE?.let {
                it.apply { moviesRemoteDataSource = remoteDataSource }
            }
            return INSTANCE
                ?: MoviesRepository(remoteDataSource).apply { INSTANCE = this }
        }
    }

    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback, page: Long) {
        if (Cache.cacheIsDirty) {
            getMoviesFromRemoteSource(callback, page)
        } else {
            callback.onMoviesLoaded(Cache.movies)
        }
    }

    private fun getMoviesFromRemoteSource(callback: MoviesDataSource.LoadMoviesCallback, page: Long) {
        moviesRemoteDataSource.getMovies(object : MoviesDataSource.LoadMoviesCallback {
            override fun onMoviesLoaded(movies: List<Movie>) {
                Cache.cacheMovies(movies)
                callback.onMoviesLoaded(Cache.movies)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        }, page)
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        if (isMovieCached(Cache.movie, movieId) { callback.onMovieLoaded(it) }) return
        else moviesRemoteDataSource.getMovie(movieId, callback)
    }

    private fun isMovieCached(movie: Movie?, id: Long, returnCache: (Movie) -> (Unit)): Boolean {
        if (movie?.id == id) {
            returnCache(movie)
            return true
        }
        return false
    }

    fun refreshMovies() {
        Cache.cacheIsDirty = true
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        if (Cache.genres.isNotEmpty()) {
            callback.onGenresLoaded(Cache.genres)
        } else {
            moviesRemoteDataSource.getGenres(callback)
        }
    }
}