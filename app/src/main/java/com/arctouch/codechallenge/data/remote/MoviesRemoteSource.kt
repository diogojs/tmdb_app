package com.arctouch.codechallenge.data.remote

import android.annotation.SuppressLint
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.MoviesDataSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object MoviesRemoteSource: MoviesDataSource {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val originalRequest = it.request()
            val newUrl = originalRequest.url().newBuilder().addQueryParameter("api_key", TmdbApi.API_KEY).build()
            val request = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            it.proceed(request)
        }
        .build()

    private val api: TmdbApi = Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi::class.java)

    @SuppressLint("CheckResult")
    override fun getMovies(callback: MoviesDataSource.LoadMoviesCallback, page: Long) {
        api.upcomingMovies(TmdbApi.DEFAULT_LANGUAGE, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { g -> movie.genreIds?.contains(g.id) == true })
                    }
                    Cache.cacheMovies(moviesWithGenres)
                    callback.onMoviesLoaded(Cache.movies)
            },
                { callback.onDataNotAvailable() })
    }

    override fun getMovie(movieId: Long, callback: MoviesDataSource.GetMovieCallback) {
        api.movie(movieId, TmdbApi.DEFAULT_LANGUAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                Cache.cacheMovie(it)
                callback.onMovieLoaded(it)
            },
                { callback.onDataNotAvailable() })
    }

    override fun getGenres(callback: MoviesDataSource.LoadGenresCallback) {
        api.genres(TmdbApi.DEFAULT_LANGUAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                Cache.cacheGenres(it.genres)
                callback.onGenresLoaded(Cache.genres)
            },
                { callback.onDataNotAvailable() })
    }
}