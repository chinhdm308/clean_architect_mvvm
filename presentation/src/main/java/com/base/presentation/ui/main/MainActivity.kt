package com.base.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import com.base.presentation.R
import com.base.presentation.core.base.BaseActivity
import com.base.presentation.databinding.ActivityMainBinding
import com.base.presentation.ui.postlist.PostListFragment
import com.base.presentation.utils.extentions.addFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        addFragment(R.id.customFragment, PostListFragment())
    }
}