package com.base.presentation.core.base

import android.os.Bundle

interface ViewInterface {
    fun initView(savedInstanceState: Bundle?)
    fun viewListener()
    fun dataObservable()
}
