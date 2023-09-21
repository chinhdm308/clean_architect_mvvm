package com.base.presentation.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class GridLayoutManagerWrapper(context: Context, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Timber.tag("Here").e("RecyclerView Error")
        }
    }
}