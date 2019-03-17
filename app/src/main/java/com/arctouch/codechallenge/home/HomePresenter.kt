package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.data.source.MoviesRepository
import com.arctouch.codechallenge.model.Movie

class HomePresenter(
    private val moviesRepository: MoviesRepository,
    private val view: HomeContract.View
): HomeContract.Presenter,
    MoviesDataSource.LoadMoviesCallback {

    private var firstLoad = true

    override fun start() {
        loadMovies(false)
    }

    override fun loadMovies(forceUpdate: Boolean) {
        view.setLoadingIndicator(true)

        if (forceUpdate || firstLoad) moviesRepository.refreshMovies()

        moviesRepository.getMovies(this)
        firstLoad = false
    }

    override fun openMovieDetails(requestedMovie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        view.setLoadingIndicator(false)

        if (movies.isEmpty()) {
            view.showNoMovies()
        } else {
            view.showMovies(movies)
        }
    }

    override fun onDataNotAvailable() {
        view.showLoadingMoviesError()
    }
}