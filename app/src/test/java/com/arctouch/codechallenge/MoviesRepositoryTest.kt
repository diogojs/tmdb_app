package com.arctouch.codechallenge

import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.data.remote.MoviesRemoteSource
import com.arctouch.codechallenge.model.Movie
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
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
    }

    @Test
    fun whenMovieInCache_getMovie_returnsCachedMovie() {
        // Given
        whenever(mockedMovie.id).thenReturn(42)
        Cache.cacheMovie(mockedMovie)

        // When
        val repository = MoviesRepository.getInstance(mockedRemoteSource)
        repository.getMovie(42, callback)

        // Then
        assertEquals(myMovie, mockedMovie)
        verifyZeroInteractions(mockedRemoteSource)
    }

    @Test
    fun whenCacheEmpty_getMovie_returnsRemoteData() {
        // Given
        whenever(mockedRemoteSource.getMovie(eq(42), any())).then { callback.onMovieLoaded(mockedMovie) }

        // When
        val repository = MoviesRepository.getInstance(mockedRemoteSource)
        repository.getMovie(42, callback)

        // Then
        assertEquals(myMovie, mockedMovie)
        verify(mockedRemoteSource).getMovie(any(), any())
    }
}