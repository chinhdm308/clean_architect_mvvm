package com.base.presentation.core.log

import android.util.Log
import com.base.presentation.BuildConfig

private const val KEY_LOG = "DEBUG_LOG"

fun Logd(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(KEY_LOG, message)
    }
}

fun Loge(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(KEY_LOG, message)
    }
}

fun Logi(message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(KEY_LOG, message)
    }
}

fun Logv(message: String) {
    if (BuildConfig.DEBUG) {
        Log.v(KEY_LOG, message)
    }
}

fun Logw(message: String) {
    if (BuildConfig.DEBUG) {
        Log.w(KEY_LOG, message)
    }
}

fun String.showLogd(tag: String = "DEBUG_LOG") {
    if (BuildConfig.DEBUG) {
        Log.d(tag, this)
    }
}

fun String.showLoge(tag: String = "DEBUG_LOG") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, this)
    }
}

fun String.showLogi(tag: String = "DEBUG_LOG") {
    if (BuildConfig.DEBUG) {
        Log.i(tag, this)
    }
}

fun String.showLogv(tag: String = "DEBUG_LOG") {
    if (BuildConfig.DEBUG) {
        Log.v(tag, this)
    }
}

fun String.showLogw(tag: String = "DEBUG_LOG") {
    if (BuildConfig.DEBUG) {
        Log.w(tag, this)
    }
}