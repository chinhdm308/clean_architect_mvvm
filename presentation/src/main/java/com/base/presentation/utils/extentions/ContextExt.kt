package com.base.presentation.utils.extentions

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import timber.log.Timber


// Context Extensions
fun Context.isNetworkConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        manager?.getNetworkCapabilities(manager.activeNetwork)?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } ?: false
    else manager?.activeNetworkInfo?.isConnectedOrConnecting == true
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager?.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                Timber.tag("Internet").i("NetworkCapabilities.TRANSPORT_WIFI")
                true
            }

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                Timber.tag("Internet").i("NetworkCapabilities.TRANSPORT_CELLULAR")
                true
            }

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                Timber.tag("Internet").i("NetworkCapabilities.TRANSPORT_ETHERNET")
                true
            }

            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager?.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        Timber.tag("Internet").i("ConnectivityManager.TYPE_WIFI")
                        true
                    }

                    ConnectivityManager.TYPE_MOBILE -> {
                        Timber.tag("Internet").i("ConnectivityManager.TYPE_MOBILE")
                        true
                    }

                    ConnectivityManager.TYPE_ETHERNET -> {
                        Timber.tag("Internet").i("ConnectivityManager.TYPE_ETHERNET")
                        true
                    }

                    else -> false
                }

            }
        }
    }

    return result
}

/*
* To open app notification permission screen instead of setting
*/
fun Context.openAppNotificationSettings() {
    val intent = Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }

            else -> {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.fromParts("package", packageName, null)
            }
        }
    }
    startActivity(intent)
}

fun Context.openAppSystemSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    })
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name && it.foreground }
}