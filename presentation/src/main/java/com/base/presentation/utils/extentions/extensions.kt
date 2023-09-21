package com.base.presentation.utils.extentions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

// View Extensions
fun ImageView.loadImage(source: String?) {
    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    Glide.with(context)
        .load(source ?: "")
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .into(this)
}