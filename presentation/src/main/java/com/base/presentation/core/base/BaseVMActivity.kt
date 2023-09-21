package com.base.presentation.core.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel>(
    inflate: ActivityBindingInflater<VB>
) : BaseActivity<VB>(inflate) {

    private lateinit var viewModel: VM

    abstract fun getVM(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }
}
