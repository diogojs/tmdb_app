package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie

object Cache {

    var genres = listOf<Genre>()

    var movie: Movie? = null

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    fun cacheMovie(movie: Movie) {
        this.movie = movie
    }
}
