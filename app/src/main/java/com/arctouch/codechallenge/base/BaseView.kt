package com.arctouch.codechallenge.base

interface BaseView<T> where T : BasePresenter {
    var presenter: T
}