package com.base.presentation.ui.postlist

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.base.presentation.core.base.BaseVMFragment
import com.base.presentation.databinding.FragmentPostListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : BaseVMFragment<FragmentPostListBinding, PostListViewModel>(FragmentPostListBinding::inflate) {

    private val viewModel by viewModels<PostListViewModel>()

    override fun getVM(): PostListViewModel = viewModel

}