package com.arctouch.codechallenge.detail

import com.arctouch.codechallenge.base.BasePresenter
import com.arctouch.codechallenge.base.BaseView
import com.arctouch.codechallenge.model.Movie

interface DetailContract {
    interface View : BaseView<BasePresenter> {

        fun setLoadingIndicator(active: Boolean)

        fun fillContent(movie: Movie)

        fun showLoadingError()
    }
}