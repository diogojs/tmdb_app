package com.arctouch.codechallenge.base

import com.arctouch.codechallenge.data.MoviesRepository

interface BasePresenter {
    fun start()
    fun onStop()

    val moviesRepository: MoviesRepository
}