package com.arctouch.codechallenge.data.source

import com.arctouch.codechallenge.data.Cache
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

    var cachedMovies: HashMap<Int, Movie> = HashMap()
    var cacheIsDirty: Boolean = false

    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback) {
        if (cachedMovies.isNotEmpty() && !cacheIsDirty) {
            callback.onMoviesLoaded(ArrayList(cachedMovies.values))
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
                refreshCache(movies)
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
                refreshCache(movies)
                refreshLocalSource(movies)
                callback.onMoviesLoaded(ArrayList(cachedMovies.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(movies: List<Movie>) {
        cachedMovies.clear()
        movies.forEach { cachedMovies.put(it.id, it) }
        cacheIsDirty = false
    }

    private fun refreshLocalSource(movies: List<Movie>) {
        moviesLocalDataSource.clear()
        for (movie in movies) {
            moviesLocalDataSource.addMovie(movie)
        }

    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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