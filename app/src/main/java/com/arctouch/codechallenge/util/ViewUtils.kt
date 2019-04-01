package com.arctouch.codechallenge.util

import android.widget.ImageView
import android.widget.LinearLayout
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_stars.view.*

object ViewUtils {

    fun showBackdropImage(imageView: ImageView, path: String?) {
        Glide.with(imageView)
            .load(path?.let { MovieImageUrlBuilder.buildBackdropUrl(it) })
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(imageView)
    }

    fun showPosterImage(imageView: ImageView, path: String?) {
        Glide.with(imageView)
            .load(path?.let { MovieImageUrlBuilder.buildPosterUrl(it) })
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(imageView)
    }

    fun setRating(ratingBarLayout: LinearLayout, rate: Float) {
        ratingBarLayout.ratingBar.rating = rate/2
        ratingBarLayout.tvRate.text = ratingBarLayout.context.getString(R.string.movie_rate, rate)
    }
}