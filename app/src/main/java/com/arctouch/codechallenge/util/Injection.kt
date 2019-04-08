package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.data.remote.MoviesRemoteSource

object Injection {

    fun provideMoviesRepository(): MoviesRepository {
        return MoviesRepository.getInstance(MoviesRemoteSource)
    }
}