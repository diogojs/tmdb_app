package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

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

    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback, page: Long) : Disposable {
        return if (Cache.cacheIsDirty) {
            getMoviesFromRemoteSource(callback, page)
        } else {
            callback.onMoviesLoaded(Cache.movies)
            Disposables.empty()
        }
    }

    private fun getMoviesFromRemoteSource(callback: MoviesDataSource.LoadMoviesCallback, page: Long) : Disposable {
        return moviesRemoteDataSource.getMovies(object : MoviesDataSource.LoadMoviesCallback {
            override fun onMoviesLoaded(movies: List<Movie>) {
                Cache.cacheMovies(movies)
                callback.onMoviesLoaded(Cache.movies)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        }, page)
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) : Disposable {
        return if (isMovieCached(Cache.movie, movieId) { callback.onMovieLoaded(it) }) Disposables.empty()
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

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) : Disposable {
        return if (Cache.genres.isNotEmpty()) {
            callback.onGenresLoaded(Cache.genres)
            Disposables.empty()
        } else {
            moviesRemoteDataSource.getGenres(callback)
        }
    }
}