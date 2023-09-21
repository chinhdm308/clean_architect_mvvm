package com.base.presentation.utils.extentions

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.base.presentation.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline val Fragment.windowHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.height() - insets.bottom - insets.top
        } else {
            val view = requireActivity().window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.heightPixels - insets.bottom - insets.top
        }
    }

inline val Fragment.windowWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.width() - insets.left - insets.right
        } else {
            val view = requireActivity().window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.widthPixels - insets.left - insets.right
        }
    }

inline fun FragmentManager.doTransaction(
    func: FragmentTransaction.() -> FragmentTransaction
) {
    beginTransaction().func().commit()
}

fun FragmentManager.dismissAllDialogs() {
    for (fragment in fragments) {
        if (fragment is DialogFragment) {
            val dialogFragment = fragment as DialogFragment
            dialogFragment.dismissAllowingStateLoss()
        }

        val childFragmentManager = fragment.childFragmentManager
        if (childFragmentManager != null) {
            childFragmentManager.dismissAllDialogs()
        }
    }
}

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun <T : Any?> Fragment.collectFlowOn(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
    onResult: suspend (t: T) -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
            flow.collectLatest {
                onResult.invoke(it)
            }
        }
    }
}

fun <T : Any?> Fragment.collectFlowOn(
    stateFlow: StateFlow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onResult: suspend (t: T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
            stateFlow.collect {
                onResult.invoke(it)
            }
        }
    }
}

fun Fragment.runLifecycleCoroutine(lifecycleState: Lifecycle.State = Lifecycle.State.CREATED, onHandle: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
            onHandle.invoke()
        }
    }
}

fun Fragment.rateApp() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data =
        Uri.parse("http://play.google.com/store/apps/details?id=" + com.base.presentation.BuildConfig.APPLICATION_ID)
    startActivity(intent)
}

fun Fragment.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

enum class TransitionType {
    SIBLING, DETAIL, MODAL
}

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    containerId: Int,
    transitionType: TransitionType? = TransitionType.DETAIL,
    fragment: T,
    sharedElement: View? = null,
    isBackStack: Boolean = true
) {
    val tag: String = fragment::class.java.name
    beginTransaction().apply {
        transitionType?.let { setTransition(it) }
        sharedElement?.let {
            addSharedElement(sharedElement, sharedElement.transitionName)
        }
        setReorderingAllowed(true)
        replace(containerId, fragment, tag)
        if (isBackStack) addToBackStack(tag)
        commitAllowingStateLoss()
    }
}

inline fun <reified T : Fragment> FragmentManager.handleAdd(
    containerId: Int,
    transitionType: TransitionType? = TransitionType.DETAIL,
    fragment: T,
    sharedElement: View? = null
) {
    val tag: String = fragment::class.java.name
    beginTransaction().apply {
        transitionType?.let { setTransition(it) }
        sharedElement?.let {
            addSharedElement(sharedElement, sharedElement.transitionName)
        }
        setReorderingAllowed(true)
        add(containerId, fragment, tag)
        addToBackStack(tag)
        commitAllowingStateLoss()
    }
}

fun FragmentTransaction.setTransition(transitionType: TransitionType) {
    setCustomAnimations(
        when (transitionType) {
            TransitionType.SIBLING -> R.anim.fade_in
            TransitionType.DETAIL -> R.anim.slide_in
            TransitionType.MODAL -> R.anim.slide_in
        },
        R.anim.fade_out,
        R.anim.fade_in,
        when (transitionType) {
            TransitionType.SIBLING -> R.anim.fade_out
            TransitionType.DETAIL -> R.anim.slide_out
            TransitionType.MODAL -> R.anim.slide_out
        }
    )
}

inline fun <reified T : Fragment> FragmentManager.remove(
    fragment: T
) {
    beginTransaction().apply {
        remove(fragment)
        commitAllowingStateLoss()
    }
}

//fun Fragment.fragmentLogEventTracking(firebaseAnalytics: FirebaseAnalytics) {
//    val bundle = Bundle()
//    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, this.javaClass.simpleName)
//    bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
//
//    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
//}