package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity :
    AppCompatActivity(),
    HomeContract.View,
    HomeAdapter.MovieClickListener {

    override lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = HomePresenter(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showMovies(movies: List<Movie>) {
        recyclerView.adapter = HomeAdapter(movies, this)
        recyclerView.addOnScrollListener(presenter.paginationController)
    }

    override fun showMoreMovies(movies: List<Movie>) {
        (recyclerView.adapter as HomeAdapter).addMovies(movies)
    }

    override fun showLoadingMoviesError() {
        Toast.makeText(baseContext, R.string.load_error, Toast.LENGTH_LONG).show()
    }

    override fun showNoMovies() {
        Toast.makeText(baseContext, "There is no movie with this filter", Toast.LENGTH_LONG).show()
    }

    override fun onMovieClick(item: Movie) {
        val intent = Intent(baseContext, DetailActivity::class.java)
        intent.putExtra(DetailActivity.MOVIE_ID_KEY, item.id)
        startActivity(intent)
    }
}
