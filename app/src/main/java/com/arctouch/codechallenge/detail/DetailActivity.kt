package com.arctouch.codechallenge.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.ViewUtils.setRating
import com.arctouch.codechallenge.util.ViewUtils.showBackdropImage
import com.arctouch.codechallenge.util.ViewUtils.showPosterImage
import kotlinx.android.synthetic.main.detail_activity.*
import kotlinx.android.synthetic.main.layout_stars.*

class DetailActivity : AppCompatActivity(), DetailContract.View {
    companion object {
        const val MOVIE_ID_KEY = "MOVIE_ID"
    }

    override lateinit var presenter: BasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val movieId = intent.getLongExtra(MOVIE_ID_KEY, -1)

        presenter = DetailPresenter(this, movieId)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun fillContent(movie: Movie) {
        showBackdropImage(ivBackdrop, movie.backdropPath)
        showPosterImage(ivPoster, movie.posterPath)

        tvTitle.text = movie.title
        val genres = movie.genres?.joinToString(separator = ", ") { it.name }
        val release = movie.releaseDate?.split("-")?.get(0)
        tvBasicInfo.text = getString(R.string.basic_info, release, genres)
        tvOverview.text = movie.overview
        setRating(layoutStars, movie.rate)
    }



    override fun showLoadingError() {
        Toast.makeText(baseContext, R.string.load_error, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
}