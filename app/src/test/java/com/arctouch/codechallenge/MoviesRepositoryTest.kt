package com.arctouch.codechallenge

import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.data.remote.MoviesRemoteSource
import com.arctouch.codechallenge.model.Movie
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {
    @Mock private lateinit var mockedRemoteSource: MoviesRemoteSource
    @Mock private lateinit var mockedMovie: Movie

    private lateinit var callback: MoviesDataSource.GetMovieCallback
    private lateinit var myMovie: Movie

    @Before
    fun setup() {
        callback = object : MoviesDataSource.GetMovieCallback {
            override fun onMovieLoaded(movie: Movie) {
                myMovie = movie
            }

            override fun onDataNotAvailable() {
                // do nothing
            }
        }
        whenever(mockedMovie.id).thenReturn(42)
    }

    @Test
    fun whenMovieInCache_getMovie_returnsCachedMovie() {
        // Given
        Cache.cacheMovie(mockedMovie)

        // When
        val repository = MoviesRepository.getInstance(mockedRemoteSource)
        repository.getMovie(42, callback)

        // Then
        assertEquals(myMovie, mockedMovie)
        verifyZeroInteractions(mockedRemoteSource)
    }

    @Test
    fun whenCacheEmpty_getMovie_callsRemoteData() {
        // Given
        Cache.movie = null

        // When
        val repository = MoviesRepository.getInstance(mockedRemoteSource)
        repository.getMovie(42, callback)

        // Then
        verify(mockedRemoteSource).getMovie(any(), any())
    }

    @Test
    fun whenAnotherMovieInCache_getMovie_callsRemoteData() {
        // Given
        Cache.cacheMovie(mockedMovie)

        // When
        val repository = MoviesRepository.getInstance(mockedRemoteSource)
        repository.getMovie(43, callback)

        // Then
        verify(mockedRemoteSource).getMovie(any(), any())
    }
}