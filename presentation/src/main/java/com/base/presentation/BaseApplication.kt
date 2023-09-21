package com.base.presentation

import android.app.Application
import com.base.data.BuildConfig
import com.base.presentation.utils.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * extending android application class in order to use Hilt dependency injection
 */
@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

    }
}