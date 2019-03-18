package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.base.BaseView
import com.arctouch.codechallenge.model.Movie

interface HomeContract {
    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showMovies(movies: List<Movie>)

        fun showLoadingMoviesError()

        fun showNoMovies()
    }

    interface Presenter : BasePresenter {

        fun loadMovies(forceUpdate: Boolean)
    }
}