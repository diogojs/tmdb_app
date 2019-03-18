package com.arctouch.codechallenge.base

import com.arctouch.codechallenge.data.source.MoviesRepository

interface BasePresenter {
    fun start()

    val moviesRepository: MoviesRepository
}