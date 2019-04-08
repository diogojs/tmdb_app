package com.arctouch.codechallenge.detail

import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection
import io.reactivex.disposables.CompositeDisposable

class DetailPresenter(
    private val view: DetailContract.View,
    private val movieId: Long
) : BasePresenter, MoviesDataSource.GetMovieCallback {
    override val moviesRepository: MoviesRepository
        get() = Injection.provideMoviesRepository()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun start() {
        compositeDisposable = CompositeDisposable()
        loadMovie()
    }

    private fun loadMovie() {
        view.setLoadingIndicator(true)

        compositeDisposable.add(moviesRepository.getMovie(movieId, this))
    }

    override fun onMovieLoaded(movie: Movie) {
        view.setLoadingIndicator(false)
        view.fillContent(movie)
    }

    override fun onDataNotAvailable() {
        view.showLoadingError()
    }

    override fun onStop() {
        compositeDisposable.dispose()
    }
}