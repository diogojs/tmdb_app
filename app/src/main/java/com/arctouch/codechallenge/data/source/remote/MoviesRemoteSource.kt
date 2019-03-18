package com.arctouch.codechallenge.data.source.remote

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.source.MoviesDataSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object MoviesRemoteSource: MoviesDataSource {
    private var api: TmdbApi = Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi::class.java)

    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback) {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                }
                callback.onMoviesLoaded(moviesWithGenres)
            }, { callback.onDataNotAvailable() })
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        api.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                callback.onMovieLoaded(it)
            },
                { callback.onDataNotAvailable() })
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                Cache.cacheGenres(it.genres)
                callback.onGenresLoaded(Cache.genres)
            }, { callback.onDataNotAvailable() })
    }

    private fun displayError(error: Throwable, context: Context) {
        Log.i("Network Error", "Error while connecting to server", error)
        Toast.makeText(context.applicationContext, "Error while connecting to server", Toast.LENGTH_LONG).show()
    }
}