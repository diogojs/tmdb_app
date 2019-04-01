package com.arctouch.codechallenge.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.ViewUtils.setRating
import com.arctouch.codechallenge.util.ViewUtils.showPosterImage
import kotlinx.android.synthetic.main.layout_stars.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(
    private val movies: List<Movie>,
    private val itemListener: MovieClickListener
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate?.split("-")?.get(0)
            itemView.setOnClickListener { itemListener.onMovieClick(movie) }
            setRating(itemView.layoutStars, movie.rate)

            showPosterImage(itemView.posterImageView, movie.posterPath)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    fun addMovies(movies: List<Movie>) {
        movies.forEach { add(it) }
    }

    private fun add(movie: Movie) {
        (movies as ArrayList).add(movie)
        notifyItemInserted(movies.size - 1)
    }

    interface MovieClickListener {
        fun onMovieClick(item: Movie)
    }
}
