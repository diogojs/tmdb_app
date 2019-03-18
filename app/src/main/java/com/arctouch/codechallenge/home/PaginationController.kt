package com.arctouch.codechallenge.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.source.MoviesDataSource
import com.arctouch.codechallenge.data.source.MoviesRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection


class PaginationController(
    private val activity: HomeActivity,
    private val layoutManager: LinearLayoutManager
): RecyclerView.OnScrollListener(),
    MoviesDataSource.LoadMoviesCallback {

    companion object {
        const val PAGE_SIZE: Int = 20
        const val KEY_TRANSITION: String = "KEY_TRANSITION"
    }

    val moviesRepository: MoviesRepository = Injection.provideMoviesRepository()

    private var isLoading: Boolean = true
    private var isLastPage: Boolean = false
    private var currentPage: Int = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItem = layoutManager.childCount
        val totalItem = layoutManager.itemCount
        var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if ((visibleItem + firstVisiblePosition >= totalItem)
                && firstVisiblePosition >= 0
                && totalItem >= PAGE_SIZE) {
                loadMoreMovies()
            }
        }
    }

    private fun loadMoreMovies() {
        isLoading = true

        currentPage += 1

        moviesRepository.getMovies(this)
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        activity.setLoadingIndicator(false)
        isLoading = false

        activity.showMoreMovies(movies)

        if (movies.size < PAGE_SIZE) {
            isLastPage = true
        }
    }

    override fun onDataNotAvailable() {
        Toast.makeText(activity, R.string.load_error, Toast.LENGTH_LONG).show()
    }
}