package com.base.presentation.ui.postlist

import com.base.presentation.core.base.BaseViewModel
import com.base.domain.usecases.PostListUseCase
import com.base.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postListUseCase: PostListUseCase
) : BaseViewModel() {

    init {
        executeDataFlow(data = { postListUseCase.execute() }) {
            when (it) {
                is Resource.Success -> {}
                is Resource.Error -> {}
                else -> {}
            }
        }
    }
}