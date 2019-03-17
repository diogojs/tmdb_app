package com.arctouch.codechallenge.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private val layoutId: Int = R.layout.home_activity

    override lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        presenter = HomePresenter(Injection.provideMoviesRepository(), this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showMovies(movies: List<Movie>) {
        recyclerView.adapter = HomeAdapter(movies)
    }

    override fun showLoadingMoviesError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
