package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.data.source.MoviesRepository
import com.arctouch.codechallenge.data.source.local.MoviesLocalSource
import com.arctouch.codechallenge.data.source.remote.MoviesRemoteSource

object Injection {

    fun provideMoviesRepository(): MoviesRepository {
        return MoviesRepository.getInstance(MoviesLocalSource(), MoviesRemoteSource)
    }
}