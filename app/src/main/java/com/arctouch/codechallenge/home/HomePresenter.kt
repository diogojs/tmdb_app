package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.data.source.MoviesRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection

class HomePresenter(
    private val view: HomeContract.View
) : HomeContract.Presenter,
    MoviesDataSource.LoadMoviesCallback {

    private var firstLoad = true
    override val moviesRepository: MoviesRepository = Injection.provideMoviesRepository()

    override fun start() {
        loadMovies(false)
    }

    override fun loadMovies(forceUpdate: Boolean) {
        view.setLoadingIndicator(true)

        if (forceUpdate || firstLoad) moviesRepository.refreshMovies()

        moviesRepository.getMovies(this)
        firstLoad = false
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

    override fun loadMoreMovies() {

    }
}