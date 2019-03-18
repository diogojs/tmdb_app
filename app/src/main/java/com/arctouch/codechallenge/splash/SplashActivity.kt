package com.arctouch.codechallenge.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.home.HomeActivity
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.util.Injection

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        Injection.provideMoviesRepository()
            .getGenres(object : MoviesDataSource.LoadGenresCallback {
                override fun onDataNotAvailable() {
                    Toast.makeText(baseContext, R.string.network_error, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onGenresLoaded(genres: List<Genre>) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
            })
    }
}
