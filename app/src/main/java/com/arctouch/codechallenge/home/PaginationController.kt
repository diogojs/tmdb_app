package com.arctouch.codechallenge.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.MoviesDataSource
import com.arctouch.codechallenge.data.MoviesRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Injection


class PaginationController(
    private val view: HomeContract.View
): RecyclerView.OnScrollListener(),
    MoviesDataSource.LoadMoviesCallback {

    companion object {
        const val PAGE_SIZE: Int = 20
    }

    private val moviesRepository: MoviesRepository = Injection.provideMoviesRepository()

    var isLoading: Boolean = true
    private var isLastPage: Boolean = false
    private var currentPage: Long = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItem = layoutManager.childCount
        val totalItem = layoutManager.itemCount
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
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

        moviesRepository.getMovies(this, currentPage)
    }

    override fun onMoviesLoaded(movies: List<Movie>) {
        view.setLoadingIndicator(false)
        isLoading = false

        view.showMoreMovies(movies)

        if (movies.size < PAGE_SIZE) {
            isLastPage = true
        }
    }

    override fun onDataNotAvailable() {
        Toast.makeText((view as HomeActivity).baseContext, R.string.load_error, Toast.LENGTH_LONG).show()
    }
}