package com.arctouch.codechallenge.home

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(
    private val view: HomeContract.View
) : HomeContract.Presenter,
    MoviesDataSource.LoadMoviesCallback {

    private var firstLoad = true
    private lateinit var compositeDisposable: CompositeDisposable
    override val moviesRepository: MoviesRepository = Injection.provideMoviesRepository()
    override var paginationController = PaginationController(view)

    override fun start() {
        compositeDisposable = CompositeDisposable()
        view.setLoadingIndicator(true)
        loadGenres()
        loadMovies(false)
    }

    private fun loadGenres() {
        compositeDisposable.add(
        moviesRepository.getGenres(object : MoviesDataSource.LoadGenresCallback {
            override fun onGenresLoaded(genres: List<Genre>) {
                Cache.cacheGenres(genres)
            }

            override fun onDataNotAvailable() {
                Toast.makeText((view as AppCompatActivity).baseContext, R.string.network_error, Toast.LENGTH_LONG)
                    .show()
            }

        }))
    }

    private fun loadMovies(forceUpdate: Boolean) {
        if (forceUpdate || firstLoad) moviesRepository.refreshMovies()

        compositeDisposable.add(moviesRepository.getMovies(this))
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

    override fun onStop() {
        compositeDisposable.dispose()
    }

}