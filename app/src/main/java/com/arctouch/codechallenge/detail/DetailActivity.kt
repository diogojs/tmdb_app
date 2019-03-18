package com.arctouch.codechallenge.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
        showImage(ivBackdrop, movie.backdropPath)
        showImage(ivPoster, movie.posterPath)

        tvTitle.text = movie.title
        val genres = movie.genres?.joinToString(separator = ", ") { it.name }
        val release = movie.releaseDate?.split("-")?.get(0)
        tvBasicInfo.text = getString(R.string.basic_info, release, genres)
        tvOverview.text = movie.overview
        ratingBar.rating = movie.rate/2
        tvRate.text = getString(R.string.movie_rate, movie.rate)
    }

    fun showImage(imageView: ImageView, path: String?) {
        Glide.with(imageView)
            .load(path?.let { MovieImageUrlBuilder.buildBackdropUrl(it) })
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(imageView)
    }

    override fun showLoadingError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}