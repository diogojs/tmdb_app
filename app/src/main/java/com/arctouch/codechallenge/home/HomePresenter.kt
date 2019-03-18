package com.arctouch.codechallenge.home

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection

class HomePresenter(
    private val view: HomeContract.View
) : HomeContract.Presenter,
    MoviesDataSource.LoadMoviesCallback {

    private var firstLoad = true
    override val moviesRepository: MoviesRepository = Injection.provideMoviesRepository()
    override var paginationController = PaginationController(view)

    override fun start() {
        view.setLoadingIndicator(true)
        loadGenres()
        loadMovies(false)
    }

    private fun loadGenres() {
        moviesRepository.getGenres(object : MoviesDataSource.LoadGenresCallback {
            override fun onGenresLoaded(genres: List<Genre>) {
                // does nothing
            }

            override fun onDataNotAvailable() {
                Toast.makeText((view as AppCompatActivity).baseContext, R.string.network_error, Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    private fun loadMovies(forceUpdate: Boolean) {
        if (forceUpdate || firstLoad) moviesRepository.refreshMovies()

        moviesRepository.getMovies(this)
        firstLoad = false
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        view.setLoadingIndicator(false)
        paginationController.isLoading = false

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