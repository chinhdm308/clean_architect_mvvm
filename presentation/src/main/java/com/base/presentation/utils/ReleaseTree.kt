package com.base.presentation.utils

import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // don't log anything with release mode
    }
}