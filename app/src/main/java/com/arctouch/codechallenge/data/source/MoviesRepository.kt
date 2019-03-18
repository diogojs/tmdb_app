package com.arctouch.codechallenge.data.source

import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.Cache.cacheIsDirty
import com.arctouch.codechallenge.data.source.local.MoviesLocalSource
import com.arctouch.codechallenge.model.Movie

class MoviesRepository private constructor(
    private val moviesLocalDataSource: MoviesLocalSource,
    private val moviesRemoteDataSource: MoviesDataSource
): MoviesDataSource {

    companion object {
        private var INSTANCE: MoviesRepository? = null

        @JvmStatic
        fun getInstance(localDataSource: MoviesLocalSource, remoteDataSource: MoviesDataSource): MoviesRepository {
            return INSTANCE ?: MoviesRepository(localDataSource, remoteDataSource).apply { INSTANCE = this }
        }
    }

    var cachedMovies: HashMap<Long, Movie> = HashMap()

    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback, page: Long) {
        if (Cache.movies.isNotEmpty() && !cacheIsDirty) {
            callback.onMoviesLoaded(Cache.movies)
            return
        }

        if (cacheIsDirty) {
            getMoviesFromRemoteSource(callback)
        } else {
            getMoviesFromLocalSource(callback)
        }
    }

    private fun getMoviesFromLocalSource(callback: MoviesDataSource.LoadMoviesCallback) {
        moviesLocalDataSource.getMovies(object : MoviesDataSource.LoadMoviesCallback {
            override fun onMoviesLoaded(movies: List<Movie>) {
                callback.onMoviesLoaded(movies)
            }

            override fun onDataNotAvailable() {
                getMoviesFromRemoteSource(callback)
            }
        })
    }

    private fun getMoviesFromRemoteSource(callback: MoviesDataSource.LoadMoviesCallback) {
        moviesRemoteDataSource.getMovies(object : MoviesDataSource.LoadMoviesCallback {
            override fun onMoviesLoaded(movies: List<Movie>) {
                moviesLocalDataSource.refreshMovies(movies)
                callback.onMoviesLoaded(ArrayList(cachedMovies.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        if (Cache.movie?.id == movieId) {
            callback.onMovieLoaded(Cache.movie!!)
        } else {
            moviesRemoteDataSource.getMovie(movieId, callback)
        }
    }

    fun refreshMovies() {
        cacheIsDirty = true
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        if (Cache.genres.isNotEmpty()) {
            callback.onGenresLoaded(Cache.genres)
        } else {
            moviesRemoteDataSource.getGenres(callback)
        }
    }
}