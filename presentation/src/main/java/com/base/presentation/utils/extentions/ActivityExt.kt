package com.base.presentation.utils.extentions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun FragmentActivity.popBackStack() = supportFragmentManager.popBackStack()

private const val THRESHOLD_FINISH_TIME = 2000
private var backPressedTime = 0L
val isBackPressFinish: Boolean
    get() {
        // preventing finish, using threshold of 2000 ms
        if (backPressedTime + THRESHOLD_FINISH_TIME > SystemClock.elapsedRealtime()) {
            return true
        }

        backPressedTime = SystemClock.elapsedRealtime()
        return false
    }


inline fun <T : Any> AppCompatActivity.collectFlowOn(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline onResult: (t: T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(lifecycleState) {
            flow.collect {
                onResult.invoke(it)
            }
        }
    }
}

fun <T> AppCompatActivity.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun Activity.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.doTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.doTransaction { replace(frameId, fragment) }
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.doTransaction { remove(fragment) }
}